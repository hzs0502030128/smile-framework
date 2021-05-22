package org.smile.orm;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.smile.collection.CollectionUtils;
import org.smile.commons.Strings;
import org.smile.log.LoggerHandler;
import org.smile.orm.dao.BaseDaoTargetHandler;
import org.smile.orm.dao.DaoMapper;
import org.smile.orm.dao.DaoTargetHandler;
import org.smile.orm.dao.Executor;
import org.smile.orm.executor.sql.BaseSqlConverter;
import org.smile.orm.executor.sql.SqlConverter;
import org.smile.orm.mapping.BoundOrmTableMapping;
import org.smile.orm.mapping.MappingException;
import org.smile.orm.mapping.OrmObjMapping;
import org.smile.orm.mapping.OrmObjMappingInit;
import org.smile.orm.mapping.OrmTableMapping;
import org.smile.orm.mapping.adapter.OrmApplicationMapperAdapter;
import org.smile.orm.mapping.adapter.OrmMapperAdapter;
import org.smile.orm.parameter.ParamNameGetter;
import org.smile.orm.xml.XmlHelper;
import org.smile.plugin.Interceptor;
import org.smile.tag.State;
import org.smile.tag.TagEngine;
import org.smile.tag.config.TagLibContext;
import org.smile.util.ClassScaner;
import org.smile.util.RegExp;
/***
 * @author 胡真山
 *
 */
public class OrmApplication implements Runnable ,LoggerHandler{
	/**
	 * 自动刷新xml配置文件的线程名称
	 */
	protected static final String loaderThreadName="AutoReLoadOrmXml";
	/**所有的dao对象与接口名映射*/
	private Map<Class,DaoMapper> daoMapperMap=new ConcurrentHashMap<Class,DaoMapper>();
	/**对象映射*/
	private Map<String,OrmObjMapping> ormObjMapper=new ConcurrentHashMap<String, OrmObjMapping>();
	/**表映射*/
	private Map<String,OrmTableMapping> ormTableMapper=new ConcurrentHashMap<String, OrmTableMapping>();
	
	private OrmMapperAdapter ormMapperAdapter=new OrmApplicationMapperAdapter(this);
	/**
	 * sql语句转换   sql  osql
	 */
	private Map<String,SqlConverter> sqlConverters=new ConcurrentHashMap<String, SqlConverter>();
	/**
	 * 扫描目录
	 */
	private List<String> packageString;
	
	protected XmlHelper xmlHelper=new XmlHelper();
	/**
	 * 用于拆分多个包
	 */
	private RegExp splitReg=new RegExp("[,; \t\n]+");
	
	private Executor executor;
	
	protected Object baseTarget;
	
	private List<Interceptor> interceptors;
	/**
	 * 使用哪种表映射去实例化一个映射
	 */
	protected Class tableMapperClass=BoundOrmTableMapping.class;
	/**
	 * 加载xml配置文件的间隔时间
	 */
	protected long reloadXmlDelay=5;
	/**
	 * 是否自动加载xml配置文件
	 */
	protected boolean autoReLoad=false;
	/**
	 * 用来定时加载xml文件的线程池
	 */
	protected ScheduledExecutorService scheduledExecutor;
	/**用于加载配置文件*/
	protected MapperReloader reloader=new MapperReloader(this);
	
	/**参数名称处理*/
	protected ParamNameGetter paramNameGetter;
	/**targetDao获取者*/
	protected DaoTargetHandler daoTargetHandler=new BaseDaoTargetHandler();
	/**模板标签引擎*/
	protected TagEngine tagEngine;
	
	
	public void setPackageString(List<String> packageString) {
		this.packageString = packageString;
	}
	
	public void setTableMapperClass(Class tableMapperClass) {
		this.tableMapperClass = tableMapperClass;
	}
	/**
	 * 注册转换器
	 * @param name
	 * @param converter
	 */
	public void registerSqlConverter(String name,SqlConverter converter){
		//插件
		SqlConverter plugin=plugin(converter);
		sqlConverters.put(name, plugin);
	}
	
	/**
	 * 注册默认的sql转换器
	 */
	public void resisterDefaultSqlConverter(){
		//基本sql
		SqlConverter  sqlconverter=new BaseSqlConverter();
		sqlconverter.setOrmApplication(this);
		registerSqlConverter(SqlType.SQL,sqlconverter);
	}
	/**
	 * 初始化标签引擎
	 */
	private void initTagEngine() {
		//标签引擎
		tagEngine=new TagEngine();
		State state=new State();
		state.registTags(Strings.BLANK,TagLibContext.DEFAULT_CORE);
		tagEngine.setState(state);
	}
	/***
	 * 扫描所有的dao
	 */
	public void init(){
		initTagEngine();
		initDaoMapper();
		initResultMapper();
		//自动重新加载xml文件
		if(autoReLoad){
			scheduledExecutor=Executors.newScheduledThreadPool(1, new ThreadFactory(){
				@Override
				public Thread newThread(Runnable r) {
					Thread t= new Thread(r);
					t.setName(loaderThreadName);
					return t;
				}
			});
			scheduledExecutor.scheduleWithFixedDelay(this,60,reloadXmlDelay, TimeUnit.SECONDS);
		}
	}
	/**
	 * 初始化daomapper
	 */
	private void initDaoMapper() {
		ClassScaner scaner=new ClassScaner();
		List<String> strs=getPackageStrings();
		for(String s:strs){
			Set<Class<?>> classSet=scaner.getClasses(s.trim());
			for(Class<?> clazz:classSet){
				try {
					OrmObjMappingInit init=new OrmObjMappingInit(clazz,tableMapperClass);
					if(init.init()){
						OrmObjMapping mapper=init.getOrmObjMapping();
						//是table的时候把ormbase的映射也初始化一下
						if(mapper instanceof OrmTableMapping){
							OrmTableMapping.getType(clazz);
							ormTableMapper.put(clazz.getSimpleName(),(OrmTableMapping)mapper);
						}
						ormObjMapper.put(clazz.getName(),mapper);
						ormObjMapper.put(init.getMapperName(), mapper);
					}
				} catch (MappingException e) {
					throw new OrmInitException("init objmapper error class:"+clazz, e);
				}
			}
			for(Class<?> clazz:classSet){
				try {
					initDao(clazz);
				} catch (MappingException e) {
					throw new OrmInitException("init daomapper error class:"+clazz, e);
				}
			}
			logger.info("扫描目录:["+s+"]完成");
		}
		logger.info("orm 加载完成,"+ormObjMapper.size()+"个映射，其中表映射："+ormTableMapper.size()+"个");
	}
	/***
	 * 拆分扫描目录字符串
	 * @return
	 */
	private List<String> getPackageStrings(){
		List<String> list=new LinkedList<String>();
		for(String str:this.packageString){
			String[] s=splitReg.split(str,true);
			CollectionUtils.addItem(list, s);
		}
		return list;
	}
	/***
	 * 初始化结果映射
	 */
	protected void initResultMapper(){
		for(DaoMapper daoMapper:daoMapperMap.values() ){
			daoMapper.initResultMapper();
		}
	}
	/***
	 * 初始化一个dao
	 * @param clazz
	 * @throws MappingException
	 */
	private void initDao(Class<?> clazz) throws MappingException{
		DaoMapper<?> daoMapper=reloader.loadCreateMapper(clazz);
		if(daoMapper!=null) {
			this.daoMapperMap.put(clazz, daoMapper);
		}
	}
	
	
	/**
	 * 重新加载DaoMapper的配置
	 */
	public void reloadDaoMappers(){
		for(DaoMapper mapper:daoMapperMap.values()){
			Class<?> clazz=mapper.getDaoClass();
			File file=xmlHelper.getDaoXmlFile(clazz);
			//加载xml
			if(reloader.reloadXmlCreateMapper(file, clazz, mapper)){
				//如果加载了xml同时也会加载class,所以不需再加载class了
				continue;
			}
			//加载class类
			file=xmlHelper.getClassFile(clazz);
			reloader.reloadClassCreateMapper(file, clazz, mapper);
		}
	}
	
	
	public <E> DaoMapper<E> getMapper(Class<E> clazz){
		return this.daoMapperMap.get(clazz);
	}
	
	public OrmObjMapping getOrmObjMapper(String name){
		return this.ormObjMapper.get(name);
	}
	
	public OrmObjMapping getOrmObjMapper(Class<?> clazz){
		return getOrmObjMapper(clazz.getName());
	}
	/***
	 * 所有的dao接口
	 * @return
	 */
	public Collection<DaoMapper> getAllMapper(){
		return this.daoMapperMap.values();
	}
	
	public List<Interceptor> getInterceptors() {
		return interceptors;
	}
	
	public void setInterceptors(List<Interceptor> interceptors) {
		this.interceptors = interceptors;
	}
	/***
	 * 使用配置的拦截器代理一个对象 
	 * @param obj 需代理的目标
	 * @return 代理后的对象
	 */
	public <T,V extends T> T plugin(V obj){
		if(interceptors==null){
			return obj;
		}else{
			T proxy=obj;
			for(Interceptor interceptor:interceptors){
				proxy=(T)interceptor.plugin(proxy);
			}
			return proxy;
		}
	}
	/**
	 * 获取全局基本target
	 */
	public Object getBaseTarget() {
		return baseTarget;
	}
	/**
	 * 设置全局基本target
	 * @param baseTarget
	 */
	public void setBaseTarget(Object baseTarget) {
		this.baseTarget = baseTarget;
	}
	/**
	 * 表映射
	 * @param name
	 * @return
	 */
	public OrmTableMapping getTableMapper(String name){
		return ormTableMapper.get(name);
	}
	
	public OrmMapperAdapter getOrmMapperAdapter() {
		return ormMapperAdapter;
	}
	
	public SqlConverter getSqlConverter(String name){
		return sqlConverters.get(name);
	}
	
	/**
	 * 自动加载刷新时间间隔
	 * @param reloadXmlDelay
	 */
	public void setReloadXmlDelay(long reloadXmlDelay) {
		this.reloadXmlDelay = reloadXmlDelay;
	}
	/**
	 * 设置是否自动刷新xml配置文件 
	 * @param autoReLoad
	 */
	public void setAutoReLoad(boolean autoReLoad) {
		this.autoReLoad = autoReLoad;
	}
	
	@Override
	public void run() {
		try{
			reloadDaoMappers();
		}catch(Throwable e){
			logger.error("重新加载xml失败",e);
		}
	}
	
	public ParamNameGetter getParamNameGetter() {
		return paramNameGetter;
	}
	
	public void setParamNameGetter(ParamNameGetter paramNameGetter) {
		this.paramNameGetter = paramNameGetter;
	}
	
	public DaoTargetHandler getDaoTargetHandler() {
		return daoTargetHandler;
	}
	
	public void setDaoTargetHandler(DaoTargetHandler daoTargetHandler) {
		this.daoTargetHandler = daoTargetHandler;
	}
	/**
	 * 执行器
	 * @return
	 */
	public Executor getExecutor() {
		return executor;
	}

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

	public TagEngine getTagEngine() {
		return tagEngine;
	}

	public void setTagEngine(TagEngine tagEngine) {
		this.tagEngine = tagEngine;
	}
	
	
	
}