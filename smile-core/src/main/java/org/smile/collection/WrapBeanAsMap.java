package org.smile.collection;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.smile.beans.BeanInfo;
import org.smile.beans.BeanProperties;
import org.smile.beans.PropertiesGetter;
import org.smile.beans.converter.BeanException;
import org.smile.commons.SmileRunException;
/**
 * 把一个bean当成map使用
 * 用于对使用map参数的方法统一处理对象与map
 * @author 胡真山
 */
public class WrapBeanAsMap extends AbstractMap<String, Object>{
	/**bean的属性操作*/
	protected BeanProperties properties;
	/**真实的bean数据*/
	protected Object bean;
	
	protected BeanInfo beanInfo;
	
	public WrapBeanAsMap(Object bean){
		this(bean, false);
	}
	
	public WrapBeanAsMap(Object bean, boolean notExistsPropertError){
		this.bean=bean;
		this.properties=new BeanProperties(notExistsPropertError);
		this.beanInfo=BeanInfo.getInstance(bean.getClass());
	}
	
	public WrapBeanAsMap(Object bean, BeanProperties beanProperties){
		this.bean=bean;
		this.properties=beanProperties;
		this.beanInfo=BeanInfo.getInstance(bean.getClass());
	}
	
	public void updateBean(Object newBean){
		this.bean=newBean;
		this.beanInfo=BeanInfo.getInstance(bean.getClass());
	}
	
	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return new AbstractSet<Map.Entry<String,Object>>() {
			@Override
			public Iterator<java.util.Map.Entry<String, Object>> iterator() {
				return new WrapBeanMapIterator();
			}

			@Override
			public int size() {
				return beanInfo.beanUtilPdSize();
			}
		};
	}
	
	class WrapBeanMapIterator implements Iterator<Map.Entry<String, Object>>{
		Iterator<PropertyDescriptor> iterator;
		PropertyDescriptor next;
		EntryObj entry;
		WrapBeanMapIterator(){
			iterator=beanInfo.beanUtilPdList().iterator();
		}
		@Override
		public boolean hasNext() {
			while(iterator.hasNext()){
				next=iterator.next();
				Method method=next.getReadMethod();
				if(method!=null){
					return true;
				}else{
					next=null;
				}
			}
			return false;
		}
		@Override
		public EntryObj next() {
			if(next==null){
				if(!hasNext()){
					entry=null;
					return entry;
				}
			}
			try {
				entry= new EntryObj(next);
				next=null;
				return entry;
			} catch (Exception e) {
				throw new SmileRunException("next "+next.getName()+" error ",e);
			}
		}
		@Override
		public void remove() {
			entry.setValue(null);
		}
	}
	
	
	@Override
	public boolean containsKey(Object key) {
		return properties.hasField(beanInfo,key.toString());
	}
	
	@Override
	public Object get(Object key) {
		try {
			return properties.getExpFieldValue(bean, String.valueOf(key));
		} catch (BeanException e) {
			throw new SmileRunException(e);
		}
	}
	@Override
	public Object put(String key, Object value) {
		try {
			properties.setExpFieldValue(bean, key, value);
		} catch (BeanException e) {
			throw new SmileRunException(e);
		}
		return value;
	}
	@Override
	public Object remove(Object key) {
		try {
			properties.setExpFieldValue(bean, String.valueOf(key), null);
		} catch (BeanException e) {
			throw new SmileRunException(e);
		}
		return key;
	}

	class  EntryObj implements Entry<String, Object>{
		private Object value;
		PropertyDescriptor pd;
	    EntryObj(PropertyDescriptor pd){
	    	try {
	    		this.pd=pd;
				this.value=pd.getReadMethod().invoke(bean);
			} catch (Exception e) {
				throw new SmileRunException("get "+pd.getName()+" error ",e);
			}
	    }
		@Override
		public Object setValue(Object value) {
			Method setter=pd.getWriteMethod();
			if(setter!=null){
				try {
					setter.invoke(bean, value);
				} catch (Exception e) {
					throw new SmileRunException("set "+pd.getName()+" error ",e);
				}
			}
			this.value=value;
			return value;
		}
		
		@Override
		public Object getValue() {
			return value;
		}
		
		@Override
		public String getKey() {
			return pd.getName();
		}
		
		@Override
		public String toString() {
			return pd.getName()+":"+value;
		}
	}

	@Override
	public String toString() {
		return entrySet().toString();
	}
}
