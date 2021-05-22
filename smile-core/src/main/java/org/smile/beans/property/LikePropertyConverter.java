package org.smile.beans.property;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

/**
 * 不区分大小字与下划线
 * @author strive
 *
 */
public class LikePropertyConverter extends  NoCasePropertyConverter{
	/**
	 * 构造涵数
	 * @param c
	 * @throws IntrospectionException
	 */
	public LikePropertyConverter(Class c){
		super(c);
	}
	/**
	 * 把bean的属性转成 map的key
	 */
	@Override
	public String propertyToKey(PropertyDescriptor property) {
		return LikeKeyUtil.nameToKey(property.getName());
	}
	
	@Override
	public PropertyDescriptor keyToProperty(String name) {
		return beanInfo.getPropertyDescriptorLike(name);
	}
}
