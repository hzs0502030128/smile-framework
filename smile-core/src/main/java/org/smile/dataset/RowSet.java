package org.smile.dataset;

import org.smile.dataset.group.GroupRowSet;
import org.smile.dataset.index.CrossIndexRowSet;
import org.smile.dataset.index.IndexRowSet;
import org.smile.dataset.sort.Orderby;


public interface RowSet extends DataResultSet{
	/**
	 * 第一个的行索引
	 * @return
	 */
    public int firstIndex();
    /**
     * 最后一行索引
     * @return
     */
    public int lastIndex();
    /**
     * 原始集的索引
     * @param i
     * @return
     */
    public int rowIndex(int i);
    /**
     * 获取源数据源
     * @return
     */
	public DataSet getDataSet();
	
	/**
	 * 创建索引
	 * @param field
	 * @return
	 */
	public IndexRowSet index(String[] field);
	/**
	 * 交叉索引
	 * @param rowField
	 * @param columnField
	 * @return
	 */
	public CrossIndexRowSet crossIndex(String[] rowField, String[] columnField);
	/**
	 * 执行分组
	 * @param field
	 * @return
	 */
	public GroupRowSet  group(String field);
	
	/**
	 * 执行分组
	 * @param field
	 * @return
	 */
	public GroupRowSet  group(String field,Orderby orderby);
   
}
