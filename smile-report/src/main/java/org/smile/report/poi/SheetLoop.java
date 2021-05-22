package org.smile.report.poi;

import org.apache.poi.ss.usermodel.Sheet;

public interface SheetLoop<T> {
	/**
	 * 循环数据集合 进行合并行
	 * @param sheet
	 * @param currentObj
	 * @param i
	 */
	public void loop(Sheet sheet,T currentObj,int rowIndex);
	
	/**
	 * 尝试合并最后一行
	 * @param sheet
	 * @param i 
	 */
	public void loopLast(Sheet sheet,int rowIndex);
}
