package org.smile.dataset;

import java.util.List;
import java.util.Map;

public interface Row extends DataResult{
	/**
	 * 从指定的列获取数据返回一个数组
	 * @param columns
	 * @return
	 */
	public Object[] values(int[] columns);
	/**
	 * 行数据转成数组形式
	 * @return
	 */
	public Object[] toArray();
	/**
	 * 取行数据的一个列的值
	 * @param colmun
	 * @return
	 */
	public Object value(int column);
	/**
	 * 以list方式展示
	 * @return
	 */
	public List<Object> toList();
	/**
	 * 设置一个列的值
	 * @param column 列的索引
	 * @param value
	 */
	public void set(int column,Object value);
	/**
	 * 设置一个列的值
	 * @param column 列名
	 * @param value
	 * @return
	 */
	public Object set(String column,Object value);
	/**
	 * 是否存在列名
	 * @param columnName
	 * @return
	 */
	public boolean hasColumn(String columnName);
	
	/**
	 * 所装成一个map
	 * @return
	 */
	public Map<String,Object> readOnlyMap();
	/**
	 * 源dataset中的行索引
	 * @return
	 */
	public int index();
	/**
	 * 设置在源dataset中的行索
	 * @param i
	 */
	public void setIndex(int i);
	/**复制行数据*/
	public Row copy();
	/**
	 * 与另一个行数据比较批定的列是否是一样的数据
	 * @param row
	 * @param columns
	 * @return
	 */
	boolean equals(Row row, int[] columns);
	/**
	 * 与另一个行数据比较指定的列是否是一样的数据
	 * @param row
	 * @param columns
	 * @param size 比较前多少列
	 * @return
	 */
	boolean equals(Row row, int[] columns, int size);
}
