package org.smile.report.poi;

/**
 * 单元格的坐标
 * @author 胡真山
 *
 */
public class MegerCellIndex {
	/**
	 * 行索引
	 */
	private int rowIndex;
	/**
	 * 列索引
	 */
	private int colIndex;
	/**
	 * hash code的值 用与快速从hashmap中查找到坐标
	 */
	private int hasCode;
	/**
	 * 坐标
	 * @param rowIndex 行坐标 行索引
	 * @param colIndex 列坐标 列索引
	 */
	public MegerCellIndex(int rowIndex, int colIndex) {
		this.rowIndex = rowIndex;
		this.colIndex = colIndex;
		this.hasCode = (rowIndex + "-" + colIndex).hashCode();
	}
	/**
	 * 行坐标
	 * @return
	 */
	public int getRowIndex() {
		return rowIndex;
	}
	/**
	 * 列坐标
	 * @return
	 */
	public int getColIndex() {
		return colIndex;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MegerCellIndex) {
			MegerCellIndex o = (MegerCellIndex) obj;
			if (o.colIndex == colIndex && o.rowIndex == rowIndex) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return hasCode;
	}
}