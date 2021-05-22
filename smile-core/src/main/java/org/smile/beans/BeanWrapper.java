package org.smile.beans;

import org.smile.beans.converter.Converter;
/**
 * 把一个对象包装成一个bean类型对象
 * @author 胡真山
 *
 * @param <T>
 */
public interface BeanWrapper<T> {
	/**
	 * 处理属性字段映射
	 * @param propertyName 目标类型的属性名
	 * @param mappingName 源类型的属性名
	 * @return
	 */
	BeanWrapper<T> fields(String propertyName, String mappingName);

	/**创建封装对象*/
	T build(Object source);
	/**
	 * 创建一个空的对象
	 * @return
	 */
	T build();

	/**
	 * 获取封装的目标类型
	 * @return
	 */
	Class<T> getTargetClass();

	/***
	 * 设置值的类型转换器
	 * @param converter
	 */
	void setConverter(Converter converter);

}