package org.smile.orm.xml.execut;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

import org.smile.annotation.AnnotationUtils;
import org.smile.db.Db;
import org.smile.db.sql.BoundSql;
import org.smile.db.sql.SQLRunner;
import org.smile.orm.OrmApplication;
import org.smile.orm.ann.Orm;
import org.smile.orm.ann.Sql;
import org.smile.orm.mapping.OrmTableMapping;
import org.smile.orm.result.ResultType;
import org.smile.reflect.ClassTypeUtils;
import org.smile.util.StringUtils;
/***
 * 一个方法的操作类
 * @author 胡真山
 */
public abstract class Operator implements Db,IOperator{
	@XmlAttribute
	protected String id;
	@XmlAttribute
	protected String mapper;
	@XmlAttribute
	protected String template;
	@XmlValue
	protected String sql;
	@XmlAttribute
	protected String include;
	@XmlAttribute
	protected String sqlType;
	
	@Override
	public String getId() {
		return id;
	}
	@Override
	public String getMapper() {
		return mapper;
	}
	
	@Override
	public String getSql() {
		return sql;
	}
	
	@Override
	public String getTemplate() {
		return template;
	}
	
	@Override
	public String getInclude() {
		return include;
	}
	
	@Override
	public String getSqlType() {
		return sqlType;
	}
	
	@Override
	public void initFormAnnotation(OrmApplication application, Sql sqlAnn,Method method){
		this.sql=sqlAnn.sql();
		this.mapper=sqlAnn.mapper();
		sqlType=sqlAnn.sqlType();
		template=sqlAnn.template();
		this.include=sqlAnn.include();
		OrmTableMapping tableMapping=initNullSqlOrmTableMapping(application,sql, method);
		if(tableMapping!=null) {
			initAnnotationSql(application, tableMapping, method);
		}
		this.id=method.getName();
	}
	/**
	 * 初始化sql从注解中  如果注解中指定了sql 内容  直接取指定的
	 * 如果没有指定则可以根据方法的第一个参数获取是不是是配置了映射的类
	 * 如果是则可以从映射类中初始化出 一此简单的 CURD 语句
	 * @param application
	 * @param mapping 注解 
	 * @param method Dao方法
	 */
	protected abstract void initAnnotationSql(OrmApplication application, OrmTableMapping mapping,Method method);
	/**
	 * 没有配置sql语句时 初始化方法   
	 * @param sql 实现时会有非空判断 查找Table注解映射
	 * @param method Dao接口方法
	 * @return
	 */
	protected OrmTableMapping initNullSqlOrmTableMapping(OrmApplication application,String sql,Method method){
		if(StringUtils.isEmpty(sql)){
			Class<?> parameterClass=getParameterType(method);
			if(parameterClass!=null){
				Orm orm=AnnotationUtils.getAnnotation(parameterClass,Orm.class);
				if(orm!=null&&orm.isTable()){
					return (OrmTableMapping)application.getOrmObjMapper(parameterClass);
				}
			}
		}
		return null;
	}
	/**
	 * 方法的映射类  使用注解的时候从方法参数获取映射的类
	 * @param method 方法
	 * @return 用于映射的类
	 */
	protected Class getParameterType(Method method){
		Class clazz=ClassTypeUtils.getFirstParameter(method);
		if(clazz!=null){
			if(Collection.class.isAssignableFrom(clazz)){
				Class[] generic=ClassTypeUtils.getGeneric(method.getGenericParameterTypes()[0]);
				if(generic!=null){
					clazz=generic[0];
				}
			}
			return clazz;
		}
		return null;
	}
	@Override
	public void updateSqlType(String sqlType) {
		this.sqlType = sqlType;
	}
	
	@Override
	public Object execute(SQLRunner runner, ResultType pType, BoundSql boundSql)
			throws SQLException {
		if(pType.isBooleanType()){
			return runner.execute(boundSql);
		}else{
			return runner.executeUpdate(boundSql);
		}
	}
}
