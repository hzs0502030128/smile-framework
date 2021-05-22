package org.smile.beans;

import java.util.Map;

import org.smile.beans.converter.BeanException;

/**
 * bean转到map中对属性的填充
 * @author 胡真山
 *
 */
public class BeanPropertyFiller {
	/**
	 * bean的属性转入map中
	 * @param propertyType 属性类型
	 * @param value 属性值
	 * @param propertyName 属性名称
	 * @param targetMap 目标map
	 * @throws BeanException
	 */
	public void fillPropertyToMap(Class propertyType,Object value,String propertyName,Map targetMap) throws BeanException{
		if(value!=null){
			targetMap.put(propertyName, value);
		}
	}
}
