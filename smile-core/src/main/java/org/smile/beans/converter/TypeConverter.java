package org.smile.beans.converter;

import org.smile.reflect.Generic;

public interface TypeConverter<T> {
	/**
	 * 转换后的目标类型
	 * @return
	 */
	public Class<T> getType();
	/***
	 * 转换类型
	 * @param generic 泛型
	 * @param value 源类型值
	 * @return 目标类型值
	 * @throws ConvertException
	 */
	public T convert(Generic generic,Object value) throws ConvertException;
	
	/**
	 * 不需要泛型支持的转换
	 * @param value
	 * @return
	 * @throws ConvertException
	 */
	public T convert(Object value) throws ConvertException;
}
