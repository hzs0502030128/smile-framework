package org.smile.orm.parameter;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.smile.db.sql.BoundSql;
import org.smile.orm.executor.MappedOperator;
import org.smile.orm.executor.SqlBuilder;
import org.smile.orm.xml.execut.BatchOperator;
import org.smile.reflect.ClassTypeUtils;

/***
 * 参数类型封装信息
 * @author 胡真山
 */
public class ParameterType{
	/**** 空参数  即没有参数 */
	public static final ParameterType NULL = new ParameterType();
	/**默认的参数名称*/
	public static final String DETAULT_PARAM_VAR = "arg";
	/**参数类型*/
	private Class[] typeClass;
	/**参数处理者*/
	private ParameterTypeHandler typeHandler;
	/**是否是无参数*/
	private boolean isNull = true;
	/**是否是分页查询方法参数*/
	private boolean isPageQuery = false;
	/**是否是单字段参数*/
	private boolean isOneFieldType=false;
	/**是否是单字段数组类型"*/
	private boolean isOneFieldArrayType=false;
	/**是否是单字段指量操作参数*/
	private boolean isBatchType=false;
	/**参数名称*/
	private String[] paramNames;
	/**是否是多参数*/
	private boolean isMultiArgs=false;
	/**
	 * @param type 参数类型
	 * @param operator 参数所属的操作方法
	 */
	public ParameterType(Class type,MappedOperator operator) {
		this.isNull = false;
		typeClass = new Class[]{type};
		isOneFieldType=isOneFieldParameter(type);
		isOneFieldArrayType=isOneFieldArrayParameter(type);
		if(operator.getOperator() instanceof BatchOperator){
			isBatchType=true;
		}
		initParameterHandler(operator);
		initParamName(operator);
	}
	/***
	 * 多参数时统一使用map封装
	 * @param types
	 * @param operator
	 */
	public ParameterType(Class[] types,MappedOperator operator) {
		this.isMultiArgs=true;
		this.isNull = false;
		typeClass = types;
		if(operator.getOperator() instanceof BatchOperator){
			isBatchType=true;
		}
		initParameterHandler(operator);
		initParamName(operator);
	}
	/**
	 * 是不是多参数
	 * @return
	 */
	public boolean isMultiArgs() {
		return isMultiArgs;
	}
	/**
	 * 初始化参数名称
	 * @param operator
	 */
	private void initParamName(MappedOperator operator){
		this.paramNames=operator.getParamNameFromMethod();
	}
	/**
	 * 实始化参数处理者
	 * @param operator
	 */
	private void initParameterHandler(MappedOperator operator){
		if(this.isMultiArgs){//多参数方法时
			this.typeHandler=new MultiArgsTypeHandler();
		}else{
			if(isBatchType){
				if(isOneFieldType()){
					this.typeHandler=new BatchOneFieldHandler();
				}else{
					this.typeHandler=new DefaultTypeHandler();
				}
			}else if(operator.isArrayBound()){
				this.typeHandler=new BaseArrayTypeHandler();
			}else if(Map.class.isAssignableFrom(typeClass[0])){
				this.typeHandler=new BeanTypeHandler();
			}else if(Collection.class.isAssignableFrom(typeClass[0])
					||isOneFieldArrayType()||Object[].class.isAssignableFrom(typeClass[0])){
				this.typeHandler=new CollectionTypeHandler();
			}else if(isNullType()){
				this.typeHandler=new NullTypeHandler();
			}else if(isOneFieldType()){
				this.typeHandler=new OneFieldTypeHandler();
			}else{
				this.typeHandler=new DefaultTypeHandler();
			}
		}
	}
	/**
	 * 空的参数类型
	 */
	private ParameterType() {
		this.isNull = true;
		this.typeHandler=new NullTypeHandler();
	}

	public boolean isPageQuery() {
		return isPageQuery;
	}

	public void setPageQuery(boolean isPageQuery) {
		this.isPageQuery = isPageQuery;
	}
	
	public boolean isOneFieldType() {
		return isOneFieldType;
	}

	/**
	 * 是否单字段类型参数
	 * @return
	 */
	public boolean isOneFieldArrayType() {
		return isOneFieldArrayType;
	}
	/**
	 * 是不是为空参数
	 * @return
	 */
	public boolean isNullType() {
		return isNull;
	}

	public static boolean isOneFieldParameter(Class clazz) {
		if (ClassTypeUtils.isBasicType(clazz)) {
			return true;
		} else if (ClassTypeUtils.isBasicObjType(clazz)) {
			return true;
		}
		return (clazz == String.class||Date.class.isAnnotationPresent(clazz));
	}
	/**
	 * 判断是否是单属性参数
	 * @param param
	 * @return
	 */
	public static boolean isOneFieldParameter(Object param) {
		Class clazz=param.getClass();
		return isOneFieldParameter(clazz);
	}
	
	private static boolean isOneFieldArrayParameter(Class clazz){
		if (ClassTypeUtils.isBasicArrayType(clazz)) {
			return true;
		} else if (ClassTypeUtils.isBasicObjectArray(clazz)) {
			return true;
		}
		return false;
	}
	/**
	 * 处理sql的参数绑定
	 * @param builder
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public BoundSql handleBoundSql(SqlBuilder builder,Object param) throws SQLException{
		return typeHandler.handleBoundSql(builder,this,param);
	}
	/**
	 * 参数名称
	 * @return
	 */
	public String[] getParamNames(){
		return paramNames;
	}
	/**
	 * 单参数名称
	 * @return
	 */
	public String getParamName(){
		return paramNames[0];
	}
	public Class[] getTypeClass() {
		return typeClass;
	}
	
	
}
