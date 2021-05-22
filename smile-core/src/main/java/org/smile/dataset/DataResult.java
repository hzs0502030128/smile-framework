package org.smile.dataset;

public interface DataResult {
	/**
	 * 以列名查找索引
	 * @param columnName
	 * @return
	 */
	public int getColumnIndex(String columnName);
	/**
	 * 获取列名
	 * @param column
	 * @return
	 */
	public String getColumnName(int column);
	/**
	 * 以列索引获取字段的值 
	 * @param col
	 * @return 以字符串形式返回
	 */
	public String getString(int col);
	/**
	 * 以列名获取字段的值 
	 * @param col
	 * @return 以字符串形式返回
	 */
	public String  getString(String column);
	
	public Integer getInteger(int col);
	
	
	/***
	 * 获取字段内容
	 * @param column
	 * @return
	 */
	public Object get(String column);
	
	public Object get(int col);
	/**
	 * 获取整型字段
	 * @param column
	 * @return
	 */
	public Integer getInteger(String column);
	/**
	 * 
	 * @param column
	 * @return
	 */
	public Long getLong(String column);
	/**
	 * 获取字段值
	 * @param column 列名称
	 * @return
	 */
	public Short getShort(String column);
	
	public Double getDouble(String column);
	
	public Float getFloat(String column);
	
	public Long getLong(int column);
	
	public Short getShort(int column);
	
	public Double getDouble(int column);
	
	/**
	 * 以float返回
	 * @param column 列索引
	 * @return
	 */
	public Float getFloat(int column);
	
	/**
	 * 列的个数
	 * @return
	 */
	public int getColumnCount();
	/**
	 * 
	 * @param column 列索引 
	 * @param type 返回值类型
	 * @return
	 */
	public <T> T getColumn(int column,Class<T> type);
	/**
	 * 
	 * @param column 列名
	 * @param type 返回值类型
	 * @return
	 */
	public <T> T getColumn(String column, Class<T> type);
	
}
