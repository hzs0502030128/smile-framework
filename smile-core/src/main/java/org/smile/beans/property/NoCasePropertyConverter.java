package org.smile.beans.property;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
/**
 * 不区分大小写的 key 与 property 转换
 * @author strive
 *
 */
public class NoCasePropertyConverter extends PropertyDescriptorConverter {
	/**
	 * 构造涵数
	 * @param c
	 * @throws IntrospectionException
	 */
	public NoCasePropertyConverter(Class c){
		super(c);
	}
	/**
	 * 把map的键转成 bean的 属性
	 */
	@Override
	public PropertyDescriptor keyToProperty(String name) {
		return this.beanInfo.getPropertyDescriptorNocase(name);
	}
	/**
	 * 把bean的属性转成 map的key
	 */
	@Override
	public  String propertyToKey(PropertyDescriptor property) {
		String propertyName=property.getName();
		return propertyName.toLowerCase();
	}
	
}
