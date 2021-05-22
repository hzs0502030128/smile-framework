package org.smile.dataset;

import java.util.Set;

public interface DataSetMetaData {
	/**
	 * @param col
	 * @return
	 */
	public String getColumnName(int col);
	/**
	 * 
	 * @param col
	 * @return
	 */
	public Class getColumType(int col);
	/**
	 * 
	 * @param name
	 * @return
	 */
	public int getColumnIndex(String name);
	/**
	 * 
	 * @param name
	 * @return
	 */
	public Class getColumType(String name);
	
	public void setColumnType(int col,Class type);
	/**
	 * 定义的列的数量
	 * @return
	 */
	public int getColumnCount();
	
	public Set<String> columnNames();
}
