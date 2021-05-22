package org.smile.expression;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.smile.beans.converter.BeanException;
import org.smile.collection.CollectionUtils;
import org.smile.collection.ExtendsMap;
import org.smile.collection.WrapBeanAsMap;
import org.smile.reflect.ClassTypeUtils;

public class DefaultContext extends AbstractContext{
	/**基本类型 基本封装类型的默认参数名*/
	protected static final String BASE_TYPE_VAR="value";
	/**根内容*/
	protected Map<String,Object> rootValue;
	/**是不是基本的类型*/
	protected boolean basicType=false;
	
	
	public DefaultContext(Object rootValue){
		setRoot(rootValue);
	}
	
	@Override
	public Object get(String exp) {
		if(rootValue==null){
			return null;
		}
		return getFieldValue(rootValue, exp);
	}
	
	public DefaultContext(){
		this.rootValue=new HashMap<String,Object>();
	}

	@Override
	public void set(String exp, Object value) {
		try {
			propertyHandler.setExpFieldValue(rootValue, exp,value);
		} catch (BeanException e) {
			throw new EvaluateException(this.toString(), e);
		}
	}
	
	public void remove(String name){
		this.rootValue.remove(name);
	}
	/***
	 * 从map中获取一个表达式的值
	 * @param targetMap
	 * @param exp
	 * @return
	 */
	protected Object getFieldValue(Map targetMap,String exp){
		try {
			return propertyHandler.getExpFieldValue(targetMap, exp);
		} catch (BeanException e) {
			throw new EvaluateException(targetMap+"-->"+exp,e);
		}
	}

	@Override
	public Object getParameter(String param) {
		return getFieldValue(parameters, param);
	}

	@Override
	public void setParameters(Object params) {
		if(params!=null){
			this.parameters=wrapToMap(params);
		}
	}
	
	protected Map<String,Object> wrapToMap(Object value){
		if(basicType){
			return CollectionUtils.hashMap(BASE_TYPE_VAR, value);
		}else if(value instanceof Map){
			return (Map<String,Object>) value;
		}else{
			return wrapBeanRoot(value);
		}
	}
	/**
	 * 对javabean类型进行包装
	 * @param value
	 * @return
	 */
	protected Map<String,Object> wrapBeanRoot(Object value){
		return new ExtendsMap<String,Object>(new WrapBeanAsMap(value));
	}

	@Override
	public void setRoot(Object rootObj) {
		if(rootObj!=null){
			Class type=rootObj.getClass();
			basicType=ClassTypeUtils.isBasicType(type)||ClassTypeUtils.isBasicObjType(type)||type==String.class;
			this.rootValue=wrapToMap(rootObj);
		}
	}

	@Override
	public Set<String> keys() {
		return this.rootValue.keySet();
	}

}
