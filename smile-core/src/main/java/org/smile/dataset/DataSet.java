package org.smile.dataset;

/**
 * 数据集接口
 * @author 胡真山
 *
 */
public interface DataSet extends RowSet{
	/**
	 * 取出一个区间的数据构建一个行结果集
	 * @param start 起始行索引 包含
	 * @param end 结果行索引 包含
	 * @return
	 */
	public RowSet rangeRowSet(int start,int end);
	/**
	 * 取行挑选的行数据构建一个行结果集
	 * @param rows 要取出的行索引
	 * @return
	 */
	public RowSet randomRowSet(int[] rows);
	/**
	 * 向数据集中添加一个数数据
	 * @param row
	 */
	public void addRow(Row row);
	
	/**
	 * 数据集的行数
	 * @return
	 */
	public int rowCount();
	
}
