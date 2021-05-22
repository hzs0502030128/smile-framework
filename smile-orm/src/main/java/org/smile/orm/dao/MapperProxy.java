package org.smile.orm.dao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import org.smile.collection.ArrayUtils;
import org.smile.db.jdbc.EnableRecordDao;
import org.smile.db.jdbc.Record;
import org.smile.db.jdbc.RecordDao;
import org.smile.log.LoggerHandler;
import org.smile.orm.OrmApplication;
import org.smile.orm.executor.MappedOperator;
import org.smile.orm.parameter.ParameterType;
import org.smile.orm.record.RecordDaoImpl;
import org.smile.reflect.ClassTypeUtils;
import org.smile.util.StringUtils;
/***
 * 生成dao的代理类
 * @author 胡真山
 * 使用动态代理执行接口方法	
 */
public class MapperProxy<E> implements InvocationHandler,LoggerHandler {
	
	private OrmApplication application;
	/**mapper对象*/
	private DaoMapper<E> mapperDao;
	/**Dao代理*/
	private E mapperInterfaceProxy;
	/**用于实例其它自定义方法的实例*/
	private Object daoTarget;
	/**用于实现record操作的实例*/
	private EnableRecordDao<?> recordDao;
	
	public MapperProxy(OrmApplication application,Class<E> interfaceClazz){
		this.application=application;
		this.mapperDao=application.getMapper(interfaceClazz);
		if(mapperDao==null){
			throw new NullPointerException("不能找到接口"+interfaceClazz+"请查检扫描路径packageString配置是否正解或是否正确增加注解");
		}
		init();
	}
	
	/**
	 * 	复制一个对象
	 * @param <E>
	 * @param proxy
	 * @return
	 */
	public static <E> MapperProxy<E> copy(MapperProxy<E> proxy){
		return new MapperProxy<E>(proxy.application, proxy.mapperDao);
	}
	/**
	 * 初始化代理的信息
	 */
	private void init(){
		Class<E> clazz=(Class<E>)mapperDao.getDaoClass();
		this.mapperInterfaceProxy=(E)Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},this);
		String targetClass=mapperDao.getMapperXml().getTarget();
		if(StringUtils.notEmpty(targetClass)){//xml配置了target的class类的时候
			daoTarget=application.getDaoTargetHandler().getTarget(targetClass, application.getExecutor());
		}else{//没有配置时初始化全局的 
			this.daoTarget=application.getBaseTarget();
		}
		initRecordDao();
	}
	/**
	 * 初始化recordDao对象
	 */
	private void initRecordDao() {
		if(RecordDao.class.isAssignableFrom(mapperDao.getDaoClass())) {//接口继承了RecordDao
			Type[] types=mapperDao.getDaoClass().getGenericInterfaces();
			if(ArrayUtils.notEmpty(types)) {
				for(Type type:types) {
					Class[] wrapClass=ClassTypeUtils.getGeneric(type);
					if(ArrayUtils.notEmpty(wrapClass)) {
						for(Class clazz:wrapClass) {
							if(Record.class.isAssignableFrom(clazz)) {
								try {
									recordDao=new RecordDaoImpl(clazz);
								} catch (Exception e) {
									logger.error(e);
								}
							}
						}
					}
				}
			}
		}
	}
	
	public MapperProxy(OrmApplication application,DaoMapper<E> mapper){
		this.application=application;
		this.mapperDao=mapper;
		init();
	}
	
	public E getInterfaceDao(){
		return mapperInterfaceProxy;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String methodName=method.getName();
		MappedOperator operator=mapperDao.getOperator(methodName);
		if(operator==null){
			return doNoMappedMethod(method, methodName, args);
		}else{
			MethodArgs methodArg=new MethodArgs(args);
			if(operator.getParameterType().isPageQuery()){
				return application.getExecutor().queryPage(operator, methodArg.getSingleParam(), methodArg.getPage(), methodArg.getSize());
			}else{
				ParameterType paramterType=operator.getParameterType();
				if(paramterType.isMultiArgs()){
					//多参数执行
					return application.getExecutor().execute(operator,methodArg.getArgs());
				}else{//单参数执行
					return application.getExecutor().execute(operator,methodArg.getSingleParam());
				}
			}
		}
	}
	/**
	 * 	不是mapper的方法
	 * 	Recorddao 或其它接口继承的方法
	 * @param method
	 * @param methodName
	 * @param args
	 * @return
	 * @throws Throwable
	 */
	private Object doNoMappedMethod(Method method,String methodName,Object[] args) throws Throwable{
		switch(methodName) {
			case "toString":
					return this.toString()+" proxy:"+mapperDao.getDaoClass();
			case "getClass":
					return mapperDao.getDaoClass();
			default:
				if(this.recordDao!=null) {//是recordDao的方法
					Class declarClass= method.getDeclaringClass();
					if(declarClass.isAssignableFrom(EnableRecordDao.class)) {
						return method.invoke(this.recordDao, args);
					}
				}
				if(daoTarget!=null){//使用target对象执行方法
					try{
						return method.invoke(daoTarget,args);
					}catch(Throwable e){
						throw new TargetImplementMethodException("target implements method error "+daoTarget+" method:"+method+" ags:"+args,e);
					}
				}else{
					throw new NoSuchMethodException("not implement method name "+methodName+" parameter:"+args +" and not find interface method mapper in xml or annotation ");
				}
		}
	}
	
	public boolean isSingle(){
		return this.mapperDao.isSingle();
	}

	public Class<E> getMapperInterface() {
		return (Class<E>)mapperDao.getDaoClass();
	}
	
}
