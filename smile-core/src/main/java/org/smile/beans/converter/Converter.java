package org.smile.beans.converter;

import org.smile.reflect.Generic;
import org.smile.util.RegExp;




/**
 * 转换器接口
 * @author strive
 *
 */
public interface Converter {
	
	public static final RegExp arraySplit=new RegExp("[,;]+");
	/**
	 * 需要实现的转换方法
	 * @param type 目标类型
	 * @param value 要转换的值
	 * @param generic 目标泛型
	 * @return 转换后的值
	 */
	public <T> T convert(Class<T> type,Generic generic,Object value) throws ConvertException;
	/**
	 * 无泛型的转换方法
	 * @param type
	 * @param value
	 * @return
	 * @throws ConvertException
	 */
	public  <T> T convert(Class<T> type,Object value) throws ConvertException;
}
