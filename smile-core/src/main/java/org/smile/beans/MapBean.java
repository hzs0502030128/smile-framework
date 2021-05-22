package org.smile.beans;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.smile.beans.converter.BeanException;
import org.smile.collection.BaseMapEntry;
import org.smile.commons.SmileRunException;
import org.smile.expression.EvaluateException;
/**
 * 以map的方式定义一个bean
 * key 做为bean的成员属性变量
 * @author 胡真山
 * @date 2018年5月28日
 *
 */
public class MapBean<T extends MapBeanClass> implements PropertiesGetter<String, Object>{
	/** 定义的类*/
	private T clazz;
	/***
	 * 所有属性的值
	 */
	private Map<String,PropertyValue> values;
	
	private BeanProperties beanProperties;
	
	protected MapBean(T clazz){
		this.clazz=clazz;
		this.values=new ConcurrentHashMap<String, PropertyValue>(clazz.fieldCount());
		this.beanProperties=clazz.getBeanProperties();
	}
	/***
	 * 设置属性的值
	 * @param name 属性名称
	 * @param property 属性的值
	 */
	protected void setProperty(String name,PropertyValue property){
		values.put(name, property);
	}
	
	/**
	 * 设置字段属性
	 * @param name
	 * @param value
	 * @throws BeanException
	 */
	public void set(String name,Object value) throws BeanException{
		PropertyValue pv=values.get(name);
		if(pv==null){
			throw new BeanException("未定义的属性字段："+name);
		}
		pv.value(beanProperties,pv.getDeclare().castValue(value));
	}
	/**
	 * 获取字段属性
	 * @param name
	 * @return
	 * @throws BeanException
	 */
	public <T> T get(String name) throws BeanException{
		PropertyValue pv=values.get(name);
		if(pv==null){
			throw new BeanException("未定义的属性字段："+name);
		}
		return (T)pv.value();
	}
	
	/**定义的类型*/
	public T getMapBeanClass(){
		return clazz;
	}

	public BeanProperties getBeanProperties() {
		return beanProperties;
	}

	public void setBeanProperties(BeanProperties beanProperties) {
		this.beanProperties = beanProperties;
	}
	
	public PropertyValue getPropertyValue(String name) {
		return values.get(name);
	}
	/**
	 * 同时获取多个属性的值
	 * @param fieldNames
	 * @return
	 */
	public Object[] getPropertyValues(String[] fieldNames){
		Object[] param=new Object[fieldNames.length];
		for(int i=0;i<fieldNames.length;i++){
			try {
				param[i]=get(fieldNames[i]);
			} catch (BeanException e) {
				throw new SmileRunException(e);
			}
		}
		return param;
	}
	
	
	@Override
	public String toString() {
		return wrapAsMap().toString();
	}
	/**
	 * 把此对象做为一个map使用
	 * @return
	 */
	public Map<String,Object> wrapAsMap(){
		return new WrapMap();
	}
	
	class WrapMap extends AbstractMap<String,Object>{
		@Override
		public Set<java.util.Map.Entry<String, Object>> entrySet() {
			return new AbstractSet<Map.Entry<String,Object>>() {
				@Override
				public Iterator<java.util.Map.Entry<String, Object>> iterator() {
					return new WrapMapIterator();
				}

				@Override
				public int size() {
					return values.size();
				}
			};
		}
		
	}
	
	class WrapMapIterator implements Iterator<Map.Entry<String,Object>>{
		
		Iterator<Map.Entry<String, PropertyValue>> iterator;
		
		WrapMapIterator(){
			this.iterator=values.entrySet().iterator();
		}
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public Entry<String, Object> next() {
			Map.Entry<String, PropertyValue> pv=iterator.next();
			return new BaseMapEntry(pv.getKey(),pv.getValue().value());
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}

	@Override
	public Object getValue(String name) {
		try {
			return get(name);
		} catch (BeanException e) {
			throw new SmileRunException(e);
		}
	}
}
