package org.smile.orm.dao;

import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.smile.annotation.AnnotationUtils;
import org.smile.collection.ArrayUtils;
import org.smile.collection.CollectionUtils;
import org.smile.commons.ann.Sington;
import org.smile.db.Db;
import org.smile.db.PageModel;
import org.smile.log.LoggerHandler;
import org.smile.orm.OrmApplication;
import org.smile.orm.OrmInitException;
import org.smile.orm.ann.ArrayBound;
import org.smile.orm.ann.Sql;
import org.smile.orm.executor.MappedOperator;
import org.smile.orm.parameter.ParameterType;
import org.smile.orm.xml.execut.IOperator;
import org.smile.orm.xml.execut.MapperXml;
import org.smile.orm.xml.execut.Operator;
import org.smile.orm.xml.execut.Snippet;
import org.smile.template.IncludesContext;
import org.smile.util.StringUtils;
/***
 * dao 操作对象 相当于一个接口的所有方法的集合
 * 
 * @author 胡真山
 * 
 */
public class DaoMapper<E> implements Db,LoggerHandler{
	/**对应的xml配置信息*/
	private MapperXml mapperXml;
	/**dao 的名称可以在注解中配置 不配置时以类名做为名称*/
	private String name;
	/**dao所属的应用*/
	private OrmApplication application;
	/**方法对应的操作*/
	protected Map<String,MappedOperator> operatorsMap=new ConcurrentHashMap<String,MappedOperator>();
	/** 包含的代码片断 */
	protected IncludesContext includesContext;
	
	protected boolean single=false;
	
	public DaoMapper(OrmApplication application,MapperXml xml){
		this.application=application;
		this.mapperXml=xml;
	}
	
	public long getXmlUpdateTime() {
		return mapperXml.getXmlUpdateTimes();
	}
	
	/**dao接口类*/
	private Class<E> daoClass;
	/**
	 * 初始化DaoMapper
	 */
	public void doInit() {
		initSnippet(mapperXml.getSnippet());
		initOneList(mapperXml.getSelect());
		initOneList(mapperXml.getUpdate());
		initOneList(mapperXml.getInsert());
		initOneList(mapperXml.getDelete());
		initOneList(mapperXml.getBatch());
		initAnnOperator();
		initParameterType();
		initIsSingle();
		
	}
	/**
	 * 初始化是否单例
	 */
	private void initIsSingle() {
		Sington sington=AnnotationUtils.getAnnotation(this.daoClass, Sington.class);
		if(sington!=null) {
			this.single=sington.value();
		}else {
			this.single=this.mapperXml.isSingle()||StringUtils.isEmpty(this.mapperXml.getTarget());
		}
	}
	
	/**
	 * 处理代码片断
	 * @param snippet
	 */
	protected void initSnippet(List<Snippet> snippetList){
		if(CollectionUtils.notEmpty(snippetList)){
			includesContext=new IncludesContext();
			for(Snippet s:snippetList){
				includesContext.addInclude(s.getId(), new StringReader(s.getContent()));
			}
		}
	}

	/**
	 * 初始化注解配置的方法
	 */
	private void initAnnOperator(){
		Method[] methods=daoClass.getMethods();
		for(Method m:methods){
			Sql sqlAnn=AnnotationUtils.getAnnotation(m, Sql.class);
			if(sqlAnn!=null){
				String type=sqlAnn.type();
				if(sqlAnn.batch()) {//是批量操作
					type=Db.BATCH;
				}
				Class<Operator> op=AnnotationOperatorType.getOperatorClass(type);
				if(op!=null){
					initOneMethod(m,sqlAnn,op);
				}
			}
		}
	}
	/***
	 * 初始化一个注解方法
	 * @param method 方法
	 * @param sqlAnn 注解配置
	 */
	private void initOneMethod(Method method,Sql sqlAnn,Class<Operator> type){
		try {
			IOperator op=type.newInstance();
			op.initFormAnnotation(application, sqlAnn, method);
			MappedOperator  mappedOperator=initOneMappedOperator(op);
			mappedOperator.setTargetMethod(method);
			mappedOperator.setFromAnnotation(true);
		}catch (Exception e) {
			throw new OrmInitException("初始化 "+this.daoClass+" 的  method  "+method+" 失败 "+type,e);
		}
	}
	/**
	 * 检验是否使用数组绑定参数
	 * @param operator
	 * @param m
	 */
	private void checkArrayBandMethod(MappedOperator operator,Method m){
		ArrayBound arrayBound=m.getAnnotation(ArrayBound.class);
		if(arrayBound!=null){
			operator.setArrayBound(true);
		}
	}
	/**
	 * 初始化方法的参数类型
	 */
	private void initParameterType(){
		Method[] methods=daoClass.getMethods();
		for(Method m:methods){
			MappedOperator mappedOperator=operatorsMap.get(m.getName());
			if(mappedOperator!=null){
				if(mappedOperator.isFromAnnotation()) {
					if(!m.equals(mappedOperator.getTargetMethod())) {//绑定的方法不是当前方法
						continue;
					}
				}else {//从xml中来的操作映射还没有绑定方法
					mappedOperator.setTargetMethod(m);
				}
				checkArrayBandMethod(mappedOperator, m);
				//方法参数
				Class[] paramClass=m.getParameterTypes();
				ParameterType type;
				//空参数时
				if(ArrayUtils.isEmpty(paramClass)){
					type=ParameterType.NULL;
				}else{
					//判断是不否为分页查询的方法
					Class returnType=m.getReturnType();
					if(paramClass.length==1){//单参数的方法
						type=new ParameterType(paramClass[0],mappedOperator);
					}else if(checkIsPageQueryMethod(returnType, paramClass)){
						type=new ParameterType(paramClass[0],mappedOperator);
						type.setPageQuery(true);
					}else{//多参数方法
						type=new ParameterType(paramClass,mappedOperator);
					}
				}
				mappedOperator.setParameterType(type);
			}
		}
	}
	/**
	 * 只有当返回类型为pageModel类型时才识为分页查询方法
	 * 检验是否是分页查询参数方法
	 * @param returnType
	 * @param paramClass
	 * @return
	 */
	private boolean checkIsPageQueryMethod(Class returnType,Class[] paramClass){
		if(PageModel.class==returnType){//只有当返回类型为pageModel类型时才识为分页查询方法,最后两个参数为int型
			if(paramClass.length>1){
				Class pageParam=paramClass[paramClass.length-2];
				Class sizeParam=paramClass[paramClass.length-1];
				return (pageParam==int.class||pageParam==Integer.class)&&(sizeParam==int.class||sizeParam==Integer.class);
			}
		}
		return false;
	}
	/**返回结果*/
	public void initResultMapper(){
		for(MappedOperator operator:operatorsMap.values()){
			operator.initResultMapper();
		}
	}
	
	public Class<E> getDaoClass() {
		return daoClass;
	}
	/**
	 * 设置dao的接口类
	 * @param clazz 接口
	 */
	public void setDaoClass(Class<E> clazz){
		this.daoClass=clazz;
	}
	/**
	 * 初始化一个xml配置的方法
	 * @param xml
	 */
	private MappedOperator initOneMappedOperator(IOperator xml){
		//用于可以插件修改实现
		IOperator proxyXml=application.plugin(xml);
		MappedOperator operator=new MappedOperator(this,proxyXml);
		registOperator(operator);
		return operator;
	}
	/**
	 * 注入一个dao操作
	 * @param mappedOperator
	 */
	private void registOperator(MappedOperator mappedOperator){
		String id=mappedOperator.getId();
		if(operatorsMap.containsKey(id)){
			throw new OrmInitException("重复定义的操作ID:"+id+",可能是xml中重复配置、或xml与注解重复、也可能是由于方法重载的原因,此框架不支持方法的重载");
		}
		operatorsMap.put(mappedOperator.getId(), mappedOperator);
	}
	/**
	 * 实始化一个操作列表的xml配置方法
	 * @param operators
	 */
	private void initOneList(List<? extends Operator> operators){
		if(operators!=null){
			for(IOperator xml:operators){
				initOneMappedOperator(xml);
			}
		}
	}
	
	public MappedOperator getOperator(String id){
		return this.operatorsMap.get(id);
	}
	/**
	 * 是否是单例模式
	 * 如果配置了是单例则是单例的
	 * 如果没有配置target实现也可以认为是单例服务
	 * @return
	 */
	public boolean isSingle(){
		return this.single;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public OrmApplication getApplication() {
		return application;
	}
	
	/**
	 * 复制操作属性从另一个daomapper中
	 * @param other
	 */
	public void replaceFromOther(DaoMapper<E> other){
		Map<String,MappedOperator> otherOperators=other.operatorsMap;
		this.name=other.name;
		this.application=other.application;
		this.mapperXml=other.mapperXml;
		for(MappedOperator op:otherOperators.values()){
			this.operatorsMap.put(op.getId(), op);
			op.setDaoMapper(this);
		}
	}

	/***
	 * 配置信息最后更新时间
	 * @return
	 */
	public long getClassUpdateTime() {
		return mapperXml.getClassUpdateTimes();
	}
	/***
	 * mapper xml配置文件内容
	 * @return
	 */
	public MapperXml getMapperXml() {
		return mapperXml;
	}

	public IncludesContext getIncludesContext() {
		return includesContext;
	}
	
}
