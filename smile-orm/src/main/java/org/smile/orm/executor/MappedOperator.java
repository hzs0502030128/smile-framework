package org.smile.orm.executor;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.smile.collection.ArrayUtils;
import org.smile.commons.SmileRunException;
import org.smile.commons.Strings;
import org.smile.dataset.DataSet;
import org.smile.db.PageModel;
import org.smile.db.Transaction;
import org.smile.db.sql.BoundSql;
import org.smile.db.sql.SQLRunner;
import org.smile.io.FileNameUtils;
import org.smile.io.IOUtils;
import org.smile.orm.OrmApplication;
import org.smile.orm.OrmInitException;
import org.smile.orm.dao.DaoMapper;
import org.smile.orm.mapping.BoundOrmTableMapping;
import org.smile.orm.mapping.OrmObjMapping;
import org.smile.orm.parameter.ParameterType;
import org.smile.orm.result.DataSetResultType;
import org.smile.orm.result.ListResultType;
import org.smile.orm.result.MapperResultType;
import org.smile.orm.result.ObjectMapperResultType;
import org.smile.orm.result.ObjectResultType;
import org.smile.orm.result.PageResultType;
import org.smile.orm.result.ResultType;
import org.smile.orm.result.ResultTypeUtils;
import org.smile.orm.result.VoidResultType;
import org.smile.orm.xml.execut.IOperator;
import org.smile.orm.xml.execut.MapperXml;
import org.smile.reflect.ClassTypeUtils;
import org.smile.template.IncludesContext;
import org.smile.template.StringTemplate;
import org.smile.template.Template;
import org.smile.template.handler.SmileTemplateHandler;
import org.smile.util.ClassPathUtils;
import org.smile.util.ObjectLenUtils;
import org.smile.util.StringUtils;
/**
 * DAO的一个操作的映射类
 * @author 胡真山
 *
 * @param <E>
 */
public class MappedOperator{
	/**
	 * void 方法返回类型
	 */
	protected static final String VOID="void";
	/**默认路径标记*/
	protected static final String DEFAULT_INCLUDE="method.";
	
	protected OrmApplication application;
	
	protected DaoMapper daoMapper;
	
	protected IOperator xmlOperator;
	/**sql 解析*/
	protected StringTemplate sqlTemplate;
	/**返回值mapper*/
	protected ResultType resultType;
	/**参数类型*/
	protected ParameterType parameterType;
	/**模板类型*/
	protected String templateType;
	
	protected Method targetMethod;
	
	protected SqlBuilder builder;
	/**是否是使用数组来绑定查询参数*/
	protected boolean arrayBound=false;
	/**是否来自注解*/
	protected boolean fromAnnotation=false;
	
	public String getId(){
		return xmlOperator.getId();
	}
	
	public String getSql(){
		return xmlOperator.getSql();
	}
	
	private String getInclude(DaoMapper daoMapper,IOperator xmlVo) {
		String include=xmlVo.getInclude();
		if(StringUtils.isEmpty(include)) {
			include=daoMapper.getMapperXml().getInclude();
		}
		return include;
	}
	/**
	 * 构建映射的操作
	 * @param daoMapper 
	 * @param mapperXml  DAO映射xml
	 * @param xmlVo 方法映射的配置
	 */
	public MappedOperator(DaoMapper daoMapper,IOperator xmlVo){
		this.xmlOperator=xmlVo;
		this.daoMapper=daoMapper;
		MapperXml mapperXml=daoMapper.getMapperXml();
		this.application=daoMapper.getApplication();
		//如果方法的模板类型为空取文件的模板类型
		this.templateType=xmlVo.getTemplate();
		if(StringUtils.isEmpty(templateType)){
			templateType=mapperXml.getTemplate();
		}
		//如果方法没有配置sqlType取dao的配置
		if(StringUtils.isEmpty(xmlVo.getSqlType())){
			xmlVo.updateSqlType(mapperXml.getSqlType());
		}
		builder=this.application.plugin(new BoundSqlBuilder(this));
		String sql=StringUtils.trim(xmlVo.getSql());
		if(StringUtils.isEmpty(sql)) {//sql为空时尝试从include文件中读取
			//读取include文件内容
			String include=getInclude(daoMapper, xmlVo);
			if(StringUtils.notEmpty(include)){ //使用包含文件方式的sql
				String path=mapperXml.getNamespace();
				if(StringUtils.isEmpty(path)) {
					path=ClassPathUtils.getPackageDir(daoMapper.getDaoClass())+daoMapper.getDaoClass().getSimpleName();
				}else {
					path=ClassPathUtils.getPackageDir(path);
				}
				String includeFile;
				if(include.startsWith(DEFAULT_INCLUDE)){
					//已法名为默认路径标记
					includeFile=path+xmlVo.getId()+Strings.DOT+FileNameUtils.getExtension(include);
				}else{
					includeFile=path+xmlVo.getInclude();
				}
				InputStream is=daoMapper.getDaoClass().getClassLoader().getResourceAsStream(includeFile);
				try {
					if(is!=null){//文件不存在
						//自动识别模板类型
						if(xmlVo.getInclude().endsWith(Template.GROOVY)){
							templateType=Template.GROOVY;
						}
						sql=StringUtils.trim(IOUtils.readString(is));
					}
				} catch (Exception e) {
					throw new OrmInitException("读取include属性文件"+xmlVo.getInclude()+"失败",e);
				}
			}
		}
		if(StringUtils.notEmpty(sql)){
			this.sqlTemplate=new StringTemplate(templateType,sql);
			if(this.sqlTemplate.getHandler() instanceof SmileTemplateHandler) {//设置为orm应用的引擎方便定制orm模板的标签
				((SmileTemplateHandler)sqlTemplate.getHandler()).setEngine(application.getTagEngine());
			}
			IncludesContext context=daoMapper.getIncludesContext();
			if(context!=null){
				this.sqlTemplate.getHandler().setIncludeContext(context);
			}
		}
	}
	/**
	 * 获取参数类型
	 * @return
	 */
	public ParameterType getParameterType() {
		return parameterType;
	}

	/**
	 * 	设置映射操作的参数类型
	 * @param parameterType
	 */
	public void setParameterType(ParameterType parameterType) {
		this.parameterType = parameterType;
	}
	/**
	 * 设置操作的方法目标
	 * @param targetMethod
	 */
	public void setTargetMethod(Method targetMethod) {
		this.targetMethod = targetMethod;
	}
	/**
	 * 	初始化集合返回类型
	 * @param returnClass 方法返回的类
	 * @param mapperStr 配置的返回映射
	 */
	private void initCollectionResultMapper(Class returnClass,String mapperStr){
		if(ResultTypeUtils.isMapType(xmlOperator.getMapper())){
			this.resultType=new ListResultType(Map.class);
		}else{
			OrmObjMapping mapper=getOrmObjMapper(application, returnClass, mapperStr);
			if(mapper!=null){
				if(PageModel.class.isAssignableFrom(returnClass)){//是分页查询
					this.resultType=new MapperResultType(PageModel.class, mapper);
				}else{
					this.resultType=new MapperResultType(List.class, mapper);
				}
			}else{
				try {
					Class clazz=convertClassCollection(xmlOperator.getMapper());
					this.resultType=new ListResultType(clazz);
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(xmlOperator.getId()+"不正确的类型"+xmlOperator.getMapper(),e);
				}
			}
		}
	}
	/**
	 * 初始单对象返回
	 * @param returnClass
	 * @param mapperStr
	 */
	private void initSingleResultMapper(Class returnClass,String mapperStr){
		OrmObjMapping mapper=getOrmObjMapper(application, returnClass, mapperStr);
		if(mapper!=null){
			if(PageModel.class.isAssignableFrom(returnClass)){
				this.resultType=new MapperResultType(PageModel.class, mapper);
			}else{
				this.resultType=new ObjectMapperResultType(mapper);
			}
		}else{
			try {
				if(PageModel.class.isAssignableFrom(returnClass)){
					Class clazz=convertClassCollection(xmlOperator.getMapper());
					this.resultType=new PageResultType(clazz);
				}else{
					Class clazz=convertClassSingle(xmlOperator.getMapper(),returnClass);
					this.resultType=new ObjectResultType(clazz);
				}
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(xmlOperator.getId()+"不正确的类型"+xmlOperator.getMapper(),e);
			}
		}
	}
	/**
	 * 初始化该操作的返回在类型
	 * @param application
	 */
	public void initResultMapper(){
		if(targetMethod==null){
			throw new OrmInitException("not exists method "+xmlOperator.getId());
		}
		Class returnClass=targetMethod.getReturnType();
		if(returnClass==null||VOID.equals(returnClass.getName())){
			this.resultType=new VoidResultType();
		}else if(DataSet.class.isAssignableFrom(returnClass)){
			this.resultType=new DataSetResultType();
		}else{
			String mapperStr=xmlOperator.getMapper();
			if(Map.class.isAssignableFrom(returnClass)){
				this.resultType=new ObjectResultType(returnClass);
			}else if(Collection.class.isAssignableFrom(returnClass)){
				initCollectionResultMapper(returnClass, mapperStr);
			}else{
				initSingleResultMapper(returnClass, mapperStr);
			}
		}
		resultType.onInit();
	}
	/**
	 * 当是集合类型的时候 
	 * @param type 
	 * @return 当没有配置mapper时以方法返回类型的泛型为结果
	 * @throws ClassNotFoundException
	 */
	private Class convertClassCollection(String type) throws ClassNotFoundException {
		if(StringUtils.isEmpty(type)){
			return ClassTypeUtils.getGeneric(targetMethod.getGenericReturnType())[0];
		}else{
			return convertClass(type);
		}
	}
	
	private OrmObjMapping getOrmObjMapper(OrmApplication application,Class retrunType,String mapper){
		if(StringUtils.isEmpty(mapper)){
			Class mapperClass;
			if(Collection.class.isAssignableFrom(retrunType)||PageModel.class.isAssignableFrom(retrunType)){
				Class[] generic=ClassTypeUtils.getGeneric(targetMethod.getGenericReturnType());
				if(generic!=null){
					mapperClass=generic[0];
				}else{
					throw new OrmInitException("没有正确的配置 mapper返回类型,也没有通过方法的返回类型的泛型来指定");
				}
			}else{
				mapperClass=retrunType;
			}
			mapper=mapperClass.getName();
		}
		return application.getOrmObjMapper(mapper);
	}
	
	
	/**
	 * 如果是单个对象类型 
	 * @param type
	 * @param returnClass
	 * @return 没有配置mapper的时候 以方法返回对象为结果
	 * @throws ClassNotFoundException
	 */
	private Class convertClassSingle(String type,Class returnClass) throws ClassNotFoundException {
		if(StringUtils.isEmpty(type)){
			return returnClass;
		}else{
			return convertClass(type);
		}
	}
	
	/**
	 * 转换类型 为 class
	 * @param type
	 * @return
	 * @throws ClassNotFoundException
	 */
	private Class convertClass(String type) throws ClassNotFoundException{
		Class clazz=null;
		if(ResultTypeUtils.isMapType(type)){
			clazz=Map.class;
		}else if(ResultTypeUtils.isStringType(type)){
			clazz = String.class;
		}else{
			clazz=ClassTypeUtils.getBasicTypeClass(type);
			if(clazz==null){
				clazz=Class.forName(type);
			}
		}
		return clazz;
	}
	/**
	 * 解析sql语句
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public BoundSql parseBoundSql(Object param) throws SQLException{
		initNullSqlTemplate(param);
		return parameterType.handleBoundSql(builder, param);
	}
	
	 
	
	/**
	 * 判断当sqlTemplate是空时从参数初始化sql信息
	 */
	private void initNullSqlTemplate(Object param){
		if(sqlTemplate==null&&param!=null){
			Class clazz=param.getClass();
			if(ObjectLenUtils.hasLength(param)){
				Object obj=ObjectLenUtils.get(param,0);
				clazz=obj.getClass();
			}
			OrmObjMapping objMapper=application.getOrmObjMapper(clazz);
			if(objMapper!=null){
				if(objMapper instanceof BoundOrmTableMapping){
					BoundOrmTableMapping tableMapper=(BoundOrmTableMapping)objMapper;
					sqlTemplate=xmlOperator.initSimpleSqlTemplateFromExeParam(templateType, tableMapper);
					if(resultType.getClass()!=VoidResultType.class){
						resultType=new ObjectMapperResultType(tableMapper);
					}
					return;
				}
			}
			throw new SmileRunException(targetMethod+" sql is null ");
		}
	}
	
	public  SQLRunner createSQLRunner(Transaction transaction,MappedOperator operator,Object param){
		initNullSqlTemplate(param);
		return this.xmlOperator.createSQLRunner(transaction,operator,param);
	}

	public IOperator getOperator(){
		return this.xmlOperator;
	}

	public ResultType getResultType() {
		return resultType;
	}
	
	public DaoMapper getDaoMapper() {
		return daoMapper;
	}

	public void setDaoMapper(DaoMapper daoMapper) {
		this.daoMapper = daoMapper;
	}

	/**
	 * 是否是数组绑定sql参数
	 * @return
	 */
	public boolean isArrayBound() {
		return arrayBound;
	}

	/**
	 * 设置是否是数组绑定sql参数
	 * @param arrayBound
	 */
	public void setArrayBound(boolean arrayBound) {
		this.arrayBound = arrayBound;
	}
	/**
	 * 获取方法的参数名称
	 * @return
	 */
	public String[] getParamNameFromMethod(){
		String[] argsNames= application.getParamNameGetter().getParamName(this.targetMethod);
		if(argsNames!=null){
			return argsNames;
		}else{
			Class[] types=targetMethod.getParameterTypes();
			if(ArrayUtils.isEmpty(types)){
				return new String[0];
			}
			argsNames=new String[types.length];
			for(int i=0;i<types.length;i++){
				argsNames[i]=ParameterType.DETAULT_PARAM_VAR+i;
			}
		}
		return argsNames;
	}

	public boolean isFromAnnotation() {
		return fromAnnotation;
	}

	public void setFromAnnotation(boolean fromAnnotation) {
		this.fromAnnotation = fromAnnotation;
	}

	public Method getTargetMethod() {
		return targetMethod;
	}
	
	

}
