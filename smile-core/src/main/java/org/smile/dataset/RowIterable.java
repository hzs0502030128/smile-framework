package org.smile.dataset;

import java.util.Iterator;


public  interface RowIterable extends Iterator<Row>,Iterable<Row>{
	/***
	 * 从头开始
	 */
	public void toFirst();
	/***
	 * 跳过几步
	 * @param i
	 */
	public void skip(int i);
	/**
	 * 标记位置
	 */
	public void remark();
	/**
	 * 重置到标记的位置
	 */
	public void reset();
	
	/**
     * 当前行的一个列的值
     * @param col
     * @return
     */
    public Object valueAt(int col);
    
}
