package org.smile.beans;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.smile.beans.converter.BaseTypeConverter;
import org.smile.beans.converter.BasicConverter;
import org.smile.beans.converter.BeanException;
import org.smile.beans.converter.ConvertException;
import org.smile.beans.converter.Converter;
import org.smile.collection.ArrayIterable;
import org.smile.collection.ArrayUtils;
import org.smile.collection.CollectionUtils;
import org.smile.collection.IndexList;
import org.smile.collection.LinkedHashMap;
import org.smile.commons.Chars;
import org.smile.commons.SmileRunException;
import org.smile.log.LoggerHandler;
import org.smile.reflect.ClassTypeUtils;
import org.smile.reflect.Generic;
/**
 * 属性定义
 * @author 胡真山
 *
 */
public class FieldDeclare<T> implements LoggerHandler{
	/***
	 * 用*代表所有属性  适用于collection
	 * 返回list
	 */
	private static final String ALL_INDEX="*";
	/**
	 * 属性的类型
	 */
	protected final Class<T> type;
	/**
	 * 泛型支持
	 */
	protected Generic generic;
	
	protected MapBeanClass beanClass;
	
	/**
	 * 
	 * @param type 定义字段的类型
	 * @param generic 字段接收的泛型
	 */
	public FieldDeclare(Class<T> type,Generic generic) {
		this.type = type;
		this.generic = generic;
	}
	/**
	 * 定义一个字段的类型
	 * @param type
	 */
	public FieldDeclare(Class<T> type) {
		this.type = type;
	}

	/**
	 * 
	 * @param type
	 * @param generic 接收一个类型数组做为泛型
	 */
	public FieldDeclare(Class<T> type, Class<?>[] generic) {
		this.type = type;
		this.generic=new Generic(generic.length);
		int i=0;
		for(Class c:generic){
			this.generic.setIndex(i++, c);
		}
	}
	/**
	 * 此字段定义的泛型
	 * @return
	 */
	public Generic getGeneric() {
		return generic;
	}
	
	/**
	 * 以此类型实例化一个对象
	 * @return
	 * @throws BeanException
	 */
	public T newInstance() throws BeanException{
		if(this.type==List.class){
			return (T)new IndexList();
		}else if(Map.class==this.type){
			//Map默认为HashMap
			return (T)new LinkedHashMap();
		}else{
			return ClassTypeUtils.newInstance(this.type);
		}
	}
	/**
	 * 获取泛型的值
	 * @return
	 */
	protected Class[] getGenericValues(){
		if(generic==null){
			return null;
		}
		return generic.values;
	}
	
	/**
	 * 读取值
	 * @return
	 */
	public T castValue(BeanProperties pb,Object targetValue) {
		return castValue(pb.getConverter(), targetValue);
	}
	
	
	public T castValue(Converter converter,Object targetValue){
		//如果是为空并是基础类型的时候默认值
		if(targetValue==null){
			if(ClassTypeUtils.isBasicType(type)){//是基本类型时，需要设置默认值
				return ClassTypeUtils.basicNullDefault(type);
			}
			return null;
		}
		if(type.isAssignableFrom(targetValue.getClass())){
			return (T) targetValue;
		}
		try {
			return converter.convert(type,generic, targetValue);
		} catch (ConvertException e) {
			throw new SmileRunException("cast "+targetValue.getClass()+" to "+type+" error ", e);
		}
	}
	
	
	public T castValue(Object targetValue) {
		return castValue(getConverter(),targetValue);
	}
	
	
	
	/**
	 * 用于类型转换的转换器
	 * @return
	 */
	protected Converter getConverter(){
		return BasicConverter.getInstance();
	}
	/**
	 * 读取属性的类型
	 * @return
	 */
	public Class<T> type() {
		return type;
	}

	/**
	 * 直接获取属性值 
	 * @param fieldName 表达式的属性名
	 * @return
	 * @throws BeanException
	 */
	public Object get(BeanProperties bp,Object targetObj,Object fieldName) throws BeanException{
		if(targetObj==null){
			return null;
		}else if(targetObj instanceof Map){
			return ((Map) targetObj).get(fieldName);
		}else if(targetObj instanceof PropertiesGetter){
			return ((PropertiesGetter)targetObj).getValue(fieldName);
		}else if(targetObj instanceof Collection){
			//当类型是集合的时候，属性必须是索引
			Integer index=BaseTypeConverter.getInstance().convert(int.class, fieldName);
			return CollectionUtils.get((Collection)targetObj,index);
		}else if(ClassTypeUtils.isBasicArrayType(type)){
			//是数组时属性必须是索引
			Integer index=BaseTypeConverter.getInstance().convert(int.class, fieldName);
			return ArrayUtils.get(targetObj,index);
		}else{
			if(bp==null){
				return BeanUtils.getValue(targetObj,String.valueOf(fieldName));
			}
			return bp.getFieldValue(targetObj,String.valueOf(fieldName));
		}
	}
	
	/**
	 * 直接获取属性值 
	 * @param fieldName 表达式的属性名
	 * @return
	 * @throws BeanException
	 */
	public Object get(Object targetObj,Object fieldName) throws BeanException{
		return get(null, targetObj, fieldName);
	}
	/**
	 * 获取属性值
	 * @param bp
	 * @param expKey 带表达式的属性名称
	 * @return
	 * @throws BeanException
	 */
	public Object getExpValue(Object targetObj,BeanProperties bp,String expKey) throws BeanException{
		if(targetObj instanceof Collection){
			return getCollectionPropertyValue((Collection) targetObj,bp, expKey);
		}else if(targetObj instanceof Object[] || ClassTypeUtils.isBasicArrayType(targetObj.getClass())){
			return getCollectionPropertyValue(new ArrayIterable(targetObj), bp, expKey);
		}else if(targetObj instanceof Map){
			return getMapPropertyValue((Map)targetObj,bp, expKey);
		}else if(targetObj instanceof PropertiesGetter){
			return getPropertiesValue((PropertiesGetter)targetObj, bp, expKey);
		}else{
			return bp.getExpFieldValue(targetObj,expKey);
		}
	}
	/**
	 * 是否可以看成collection使用
	 * @param targetObj
	 * @return
	 */
	public static boolean canLookAsCollection(Class type){
		if(Collection.class.isAssignableFrom(type)||Object[].class.isAssignableFrom(type)  || ClassTypeUtils.isBasicArrayType(type)){
			return true;
		}
		return false;
	}
	
	/**
	 * 設置属性值
	 * @param bp
	 * @param expKey 带表达式的属性名称
	 * @return
	 * @throws BeanException
	 */
	public void setExpValue(Object targetObj,BeanProperties bp,String expKey,Object value) throws BeanException{
		if(targetObj instanceof Collection){
			setCollectionPropertyValue((Collection) targetObj,bp, expKey,value);
		}else if(targetObj instanceof Object[] || ClassTypeUtils.isBasicArrayType(targetObj.getClass())){
			setCollectionPropertyValue(new ArrayIterable(targetObj), bp,expKey,value);
		}else if(targetObj instanceof Map){
			setMapPropertyValue((Map)targetObj,bp,expKey,value);
		}else{
			bp.setExpFieldValue(targetObj, expKey, value);
		}
	}
	/**
	 * 设置属性的值
	 * @param targetObj 目录对象
	 * @param bp 
	 * @param filedName 字段属性
	 * @param value 字段的值
	 * @return
	 * @throws BeanException
	 */
	public Object set(Object targetObj,BeanProperties bp,String filedName,Object value) throws BeanException{
		if(value instanceof Collection){
			return setCollectionPropertyValue(targetObj,bp,filedName, value);
		}else if(ClassTypeUtils.isBasicArrayType(type)){
			Array.set(value, Integer.valueOf(filedName), value);
		}else{
			 if(targetObj==null){
				 try {
					targetObj=ClassTypeUtils.newInstance(type);
				} catch (Exception e) {
					throw new BeanException("创建"+type+"的实例失败",e);
				}
			 }
			 if(targetObj instanceof Map){
				 ((Map) targetObj).put(filedName, value);
			 }else{
				 if(bp==null){
					 BeanUtils.setValue(targetObj, filedName, value);
				 }
				 bp.setFieldValue(filedName, value, targetObj);
			 }
		}
		return targetObj;
	}
	
	/**
	 * 设置属性的值
	 * @param targetObj 目录对象
	 * @param filedName 字段属性
	 * @param value 字段的值
	 * @return
	 * @throws BeanException
	 */
	public Object set(Object targetObj,String filedName,Object value) throws BeanException{
		return this.set(targetObj, null, filedName, value);
	}
	/**
	 * 是否有泛型
	 * @return
	 */
	public boolean hasGeneric(){
		return generic!=null&&ArrayUtils.notEmpty(generic.values);
	}
	
	/**
	 * 获取此对象的属性值
	 * @param bp
	 * @param subExpName
	 * @return
	 * @throws BeanException
	 */
	public Object getCollectionPropertyValue(Collection target,BeanProperties bp, String subExpName) throws BeanException{
		int index=subExpName.indexOf(Chars.DOT);
		if(index>0){
			String idx=subExpName.substring(0,index);
			if(ALL_INDEX.equals(idx)){
				List<Object> result=new IndexList<Object>();
				for(Object obj:target){
					Generic sub=null;
					if(hasGeneric()){
						sub=generic.sub(0);
					}
					FieldDeclare tempValue=new FieldDeclare(result.getClass(),sub);
					Object temp=tempValue.getExpValue(obj,bp, subExpName.substring(index+1));
					result.add(temp);
				}
				return result;
			}else{
				Integer key=Integer.valueOf(idx);
				Object result=CollectionUtils.get(target, key);
				if(result!=null){
					Generic sub=null;
					if(hasGeneric()){
						sub=generic.sub(0);
					}
					FieldDeclare tempValue=new FieldDeclare(result.getClass(),sub);
					return tempValue.getExpValue(result,bp, subExpName.substring(index+1));
				}
				return null;
			}
		}else{
			try{
				if(ALL_INDEX.equals(subExpName)){
					List<Object> result=new IndexList<Object>();
					for(Object obj:target){
						result.add(obj);
					}
					return result;
				}else{
					Integer key=Integer.valueOf(subExpName);
					return CollectionUtils.get(target,key);
				}
			}catch(NumberFormatException e){
				throw new BeanException("collection property must is int of index  -->"+subExpName+" cant cast to int",e);
			}
		}
	}
	/**
	 * 获取此对象的属性值
	 * Map对象才调用此方法
	 * @param bp
	 * @param subFielName 字段的名称
	 * @return
	 * @throws BeanException
	 */
	public Object getMapPropertyValue(Map targetFiledValue,BeanProperties bp, String subFielName) throws BeanException{
		int index=subFielName.indexOf(Chars.DOT);
		Object key=null;
		if(index>0){
			key=subFielName.substring(0,index);
			if(hasGeneric()){
				key=bp.getConverter().convert(generic.values[0], key);
			}
			Object result=targetFiledValue.get(key);
			if(result!=null){
				Generic sub=null;
				if(hasGeneric()){
					sub=generic.sub(1);
				}
				FieldDeclare fieldDeclare=new FieldDeclare(result.getClass(),sub);
				return fieldDeclare.getExpValue(result,bp,subFielName.substring(index+1));
			}else if(result==null){//为空并且是map的时候按表达为key获取
				return ((Map)targetFiledValue).get(subFielName);
			}
			return result;
		}else{
			key=subFielName;
			if(hasGeneric()){
				key=bp.getConverter().convert(generic.values[0], subFielName);
			}
			return targetFiledValue.get(key);
		}
	}
	/**
	 * 获取此对象的属性值
	 * Map对象才调用此方法
	 * @param bp
	 * @param subFielName 字段的名称
	 * @return
	 * @throws BeanException
	 */
	public Object getPropertiesValue(PropertiesGetter targetFiledValue,BeanProperties bp, String subFielName) throws BeanException{
		int index=subFielName.indexOf(Chars.DOT);
		Object key=null;
		if(index>0){
			key=subFielName.substring(0,index);
			if(hasGeneric()){
				key=bp.getConverter().convert(generic.values[0], key);
			}
			Object result=targetFiledValue.getValue(key);
			if(result!=null){
				Generic sub=null;
				if(hasGeneric()){
					sub=generic.sub(1);
				}
				FieldDeclare fieldDeclare=new FieldDeclare(result.getClass(),sub);
				return fieldDeclare.getExpValue(result,bp,subFielName.substring(index+1));
			}
			return result;
		}else{
			key=subFielName;
			if(hasGeneric()){
				key=bp.getConverter().convert(generic.values[0], subFielName);
			}
			return targetFiledValue.getValue(key);
		}
	}
	/**
	 * 设置此属性的值
	 * 只有Map时才调用此方法
	 * @param bp
	 * @param subFieldName
	 * @param value
	 */
	public Object setMapPropertyValue(Map targetFieldValue,BeanProperties bp, String subFieldName, Object value) throws BeanException{
		Object key=subFieldName;
		int index = subFieldName.indexOf(Chars.DOT);
		if(targetFieldValue==null){
			targetFieldValue=(Map)newInstance();
		}
		if (index > 0) {
			String name = subFieldName.substring(0, index);
			key=name;
			if(hasGeneric()){//有泛型时对key类型转换
				key=bp.getConverter().convert(generic.values[0],name);
			}
			Object subObject = get(bp,targetFieldValue, key);
			if(!hasGeneric()||generic.values[1]==Map.class){
				if(subObject==null){
					subObject=new HashMap();
					((Map)targetFieldValue).put(key, subObject);
				}
				Generic sub=null;
				if(hasGeneric()){
					sub=generic.sub(1);
				}
				if(subObject instanceof Map){
					PropertyValue tempValue=new PropertyValue(subObject,Map.class,sub);
					tempValue.setPropertyValue(bp, subFieldName.substring(index+1), value);
				}else{
					bp.setExpFieldValue(subObject,subFieldName.substring(index+1), value);
				}
			}else if(generic.values[1]==List.class){
				if(subObject==null){
					subObject=new IndexList();
					((Map)targetFieldValue).put(key, subObject);
				}
				Generic sub=null;
				if(hasGeneric()){
					sub=generic.sub(1);
				}
				PropertyValue tempValue=new PropertyValue(subObject,List.class,sub);
				tempValue.setCollectionPropertyValue(bp, subFieldName.substring(index+1), value);
			}else{
				if(subObject==null){
					try{
						subObject=ClassTypeUtils.newInstance(generic.values[1]);
						((Map)targetFieldValue).put(key, subObject);
					}catch(Exception e){
						throw new BeanException(e);
					}
				}
				bp.setExpFieldValue(subObject,subFieldName.substring(index+1), value);
			}
		}else{
			if(hasGeneric()){
				key=bp.getConverter().convert(generic.values[0],generic.sub(0),subFieldName);
				value=bp.getConverter().convert(generic.values[1],generic.sub(1), value);
				((Map)targetFieldValue).put(key, value);
			}else{
				targetFieldValue.put(key, value);
			}
		}
		return targetFieldValue;
	}
	
	/**
	 * 设置此属性的值
	 * 只有当类型为List时才调用些方法
	 * @param bp
	 * @param subFieldName
	 * @param value
	 */
	public Object setCollectionPropertyValue(Object targetFieldValue,BeanProperties bp, String subFieldName, Object value) throws BeanException{
		int index = subFieldName.indexOf(Chars.DOT);
		if (index > 0) {
			String name = subFieldName.substring(0, index);
			int key=Integer.parseInt(name);
			Object subObject = ((List)targetFieldValue).get(key);
			if(!hasGeneric()||generic.values[0]==Map.class){
				if(subObject==null){
					subObject=new HashMap();
					((List)targetFieldValue).add(key, subObject);
				}
				Generic sub=null;
				if(hasGeneric()){
					sub=generic.sub(0);
				}
				PropertyValue tempValue=new PropertyValue(subObject,Map.class,sub);
				tempValue.setPropertyValue(bp, subFieldName.substring(index+1), value);
			}else if(generic.values[0]==List.class){
				if(subObject==null){
					subObject=new IndexList();
					((List)targetFieldValue).add(key, subObject);
				}
				Generic sub=null;
				if(hasGeneric()){
					sub=generic.sub(0);
				}
				PropertyValue tempValue=new PropertyValue(subObject,List.class,sub);
				tempValue.setCollectionPropertyValue(bp, subFieldName.substring(index+1), value);
			}else{
				if(subObject==null){
					subObject=ClassTypeUtils.newInstance(generic.values[0]);
					((List)targetFieldValue).add(key, subObject);
				}
				bp.setExpFieldValue(subObject,subFieldName.substring(index+1), value);
			}
		}else{
			int key=Integer.parseInt(subFieldName);
			if(hasGeneric()){
				value=bp.getConverter().convert(generic.values[0],generic.sub(0), value);
			}
			if(targetFieldValue==null){
				targetFieldValue=(T)new IndexList();
			}
			((List)targetFieldValue).add(key, value);
		}
		return targetFieldValue;
	}
}
