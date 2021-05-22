package org.smile.beans.property;

import org.smile.commons.Column;
/**
 * 列属性转换器的接口
 * @author 胡真山
 * @Date 2016年1月8日
 * @param <T>
 */
public interface PropertyConverter<T> {
	/**
	 * 键转换成属性字段名
	 * @param key
	 * @return
	 */
	public  T keyToProperty(String key);
	/**
	 * 属性名到 键的转换
	 * @param property
	 * @return
	 */
	public  String propertyToKey(T property);
	/**
	 * 创建新列
	 * @param index 索引
	 * @param key 字段名
	 * @return
	 */
	public Column<T> newColumn(int index,String key);
}
