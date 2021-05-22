package org.smile.orm.load;

import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.smile.collection.CollectionUtils;
import org.smile.db.DbConstans;
import org.smile.db.DbManager;
import org.smile.db.Dialect;
import org.smile.db.TransactionHandler;
import org.smile.orm.OrmApplication;
import org.smile.orm.OrmInitException;
import org.smile.orm.dao.BaseExecutor;
import org.smile.orm.dao.Executor;
import org.smile.orm.executor.sql.SqlConverter;
import org.smile.orm.mapping.BoundOrmTableMapping;
import org.smile.orm.mapping.OrmObjMapping;
import org.smile.orm.mapping.flag.MapperFlagHandler;
import org.smile.orm.parameter.ParamNameGetter;
import org.smile.orm.parameter.SimpleParamNameGetter;
import org.smile.plugin.Interceptor;


public class ClassPathXmlApplication implements Application{
	/**
	 * 配置的数据源
	 */
	protected DataSource dataSource;
	/**
	 * 扫描的路径
	 */
	protected List<String> packageString;
	/***
	 * orm中核心应用
	 */
	protected OrmApplication application;
	/**
	 * 拦截器插件
	 */
	protected List<Interceptor> interceptors;
	/**
	 * sql语句转换器
	 */
	protected Map<String,String> sqlConverters;
	/**
	 * 数据库方言
	 */
	protected Dialect dialect=DbConstans.DIALECT;
	
	protected Class tableMapperClass=BoundOrmTableMapping.class;
	/**重加载时间闹间隔  单位 秒*/
	protected long reloadXmlDelay=5;
	/**是否自动重加载mapper*/
	protected boolean autoReLoad=false;
	/**
	 * 一个全局的基本的部分方法实现target
	 */
	protected Object baseTarget;
	/**
	 * 映射配置标志实现类
	 */
	protected MapperFlagHandler mapperFlagHandler;
	/**参数名称处理*/
	protected ParamNameGetter paramNameGetter=new SimpleParamNameGetter();
	/**事务控制者*/
	protected TransactionHandler transactionHandler;
	/**是否是默认的orm应用*/
	protected Boolean defApplication=true;
	
	@Override
	public void setDataSource(Object dataSource) {
		if(dataSource instanceof DataSource){
			this.dataSource=(DataSource)dataSource;
		}else{
			try {
				this.dataSource = DbManager.getDataSource(String.valueOf(dataSource));
			} catch (NamingException e) {
				logger.error("orm application datasource init error",e);
			}
		}
	}
	
	public void setTableMapperClass(Class tableMapperClass) {
		this.tableMapperClass = tableMapperClass;
	}


	@Override
	public void setPackageString(List<String> packageString) {
		this.packageString = packageString;
	}
	
	@Override
	public void initOrmApplication(){
		//设置mapper标记处理类
		if(mapperFlagHandler!=null){
			OrmObjMapping.flagHandler=mapperFlagHandler;
		}
		application=new OrmApplication();
		application.setPackageString(packageString);
		application.setInterceptors(interceptors);
		application.setBaseTarget(baseTarget);
		application.setTableMapperClass(tableMapperClass);
		application.setAutoReLoad(autoReLoad);
		application.setReloadXmlDelay(reloadXmlDelay);
		application.setParamNameGetter(paramNameGetter);
		initSqlConverts();
		Executor executor=(Executor)application.plugin(new BaseExecutor(application));
		executor.setDataSource(dataSource);
		executor.setDialect(dialect);
		if(transactionHandler!=null){
			executor.setTransactionHandler(transactionHandler);
		}
		application.setExecutor(executor);
		application.init();
		if(defApplication){//是默认的应用是注册到默认配置中
			DefApplicationConfig.getInstance().setDefault(this);
		}
	}
	/**
	 * 初始化SQL转换器
	 */
	protected void initSqlConverts(){
		//默认内置
		application.resisterDefaultSqlConverter();
		//配置文件加载
		if(CollectionUtils.notEmpty(sqlConverters)){
			for(Map.Entry<String, String> entry:sqlConverters.entrySet()){
				try {
					SqlConverter converter=(SqlConverter)Class.forName(entry.getValue()).newInstance();
					converter.setOrmApplication(application);
					application.registerSqlConverter(entry.getKey(), converter);
				} catch (Exception e) {
					throw new OrmInitException("初始化sql转换器出错", e);
				}
			}
		}
	}
	@Override
	public OrmApplication getOrmApplication() {
		return application;
	}
	@Override
	public void setDialect(String dialect) {
		this.dialect=Dialect.of(dialect);
	}
	/**
	 * 设置拦截器列表
	 * @param interceptors
	 */
	public void setInterceptors(List<Interceptor> interceptors) {
		this.interceptors = interceptors;
	}
	/**
	 * 设置全局基本target
	 * @param baseTarget
	 */
	public void setBaseTarget(Object baseTarget) {
		this.baseTarget = baseTarget;
	}
	
	public void setSqlConverters(Map<String, String> sqlConverters) {
		this.sqlConverters = sqlConverters;
	}
	
	public void setMapperFlagHandler(MapperFlagHandler mapperFlagHandler) {
		this.mapperFlagHandler = mapperFlagHandler;
	}

	public void setReloadXmlDelay(long reloadXmlDelay) {
		this.reloadXmlDelay = reloadXmlDelay;
	}

	public void setAutoReLoad(boolean autoReLoad) {
		this.autoReLoad = autoReLoad;
	}

	public void setParamNameHandler(ParamNameGetter paramNameHandler) {
		this.paramNameGetter = paramNameHandler;
	}

	public void setTransactionHandler(TransactionHandler transactionHandler) {
		this.transactionHandler = transactionHandler;
	}
}
