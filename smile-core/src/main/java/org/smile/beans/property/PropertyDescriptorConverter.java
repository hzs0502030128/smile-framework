package org.smile.beans.property;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

import org.smile.beans.BeanInfo;
import org.smile.beans.BeanPropertyColumn;
import org.smile.commons.Column;

public class PropertyDescriptorConverter implements PropertyConverter<PropertyDescriptor> {
	
	/**javabean的信息*/
	protected BeanInfo beanInfo;
	/**
	 * 构造涵数
	 * @param c
	 * @throws IntrospectionException
	 */
	public PropertyDescriptorConverter(Class c){
		this.beanInfo=BeanInfo.getInstance(c);
	}
	/**
	 * 把map的键转成 bean的 属性
	 */
	@Override
	public PropertyDescriptor keyToProperty(String name) {
		return this.beanInfo.getPropertyDescriptor(name);
	}
	/**
	 * 把bean的属性转成 map的key
	 */
	@Override
	public  String propertyToKey(PropertyDescriptor property) {
		return property.getName();
	}
	
	@Override
	public Column<PropertyDescriptor> newColumn(int index,String key) {
		PropertyDescriptor pd=keyToProperty(key);
		if(pd!=null){
			return new BeanPropertyColumn(index,pd);
		}
		return null;
	}
}
