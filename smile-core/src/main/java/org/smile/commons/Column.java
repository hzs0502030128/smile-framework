package org.smile.commons;

/**
 * 一个列属性封装接口 
 * @author 胡真山
 * @Date 2016年1月8日
 * @param <T>
 */
public interface Column<T> {
	/**
	 * 列的索引值
	 * @return
	 */
	public int getIndex();
	/**
	 * 属性名
	 * @return
	 */
	public String getName();
	/**
	 * 属性类型
	 * @return
	 */
	public Class getPropertyType();
	/**
	 * 对属性的封装对象  
	 * 当包含详细的属性信息
	 * @return
	 */
	public T getProperty();
}
