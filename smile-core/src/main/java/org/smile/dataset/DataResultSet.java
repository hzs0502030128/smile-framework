package org.smile.dataset;

public interface DataResultSet extends DataResult,RowIterable {
	/***
     * 转换的数组
     * @return
     */
    public Row[] toArray();
    
    /**
     * 所有行的长度
     * @return
     */
    public int length();
    /***
     * 获取行
     * @param i
     * @return
     */
    public Row rowAt(int i);
    /**
     * 数据集所有行的一个列的数据结果
     * @param col
     * @return
     */
    public Object[] valuesAt(int col);
    /**
     * 游标滚动动下一行
     * @return
     */
    public boolean rollNext();
    /**
     * 集合定义元数据
     * @return
     */
    public DataSetMetaData getMetaData();
    /**
     * 当前游标指定的行
     * @return
     */
    public Row currentRow();
}
