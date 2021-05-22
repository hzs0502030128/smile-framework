package org.smile.report;

import java.util.HashMap;
import java.util.Map;
/**
 * @author 表头标题信息
 *
 */
public class Header {
	/**默认列宽*/
	protected static final int DEFAULT_COLUMN_WIDTH=20;
	/**
	 * 是否显示行号
	 */
	private boolean isRowCount = false;
	/**
	 * 冻结列数
	 */
	private int freeze = 0;
	/**
	 * 列名
	 */
	private String[] headerCols;
	/**
	 * 表头文本信息
	 */
	private String[][] headerTexts;
	/**
	 * 合并的列
	 */
	private String[] mergeCols;
	/**
	 * 列宽
	 */
	private Integer[] columnWidths;
	/**
	 * 列名与索引对应
	 */
	protected Map<String, Integer> colsMap = new HashMap<String, Integer>();
	/**
	 * 构造方法
	 * @param headerCols
	 * @param headerTexts
	 */
	public Header(String[] headerCols, String[][] headerTexts) {
		this.headerCols = headerCols;
		this.headerTexts = headerTexts;
		for (int i = 0; i < headerCols.length; i++) {
			colsMap.put(headerCols[i], i);
		}
	}
	
	/**
	 * 构造方法
	 * @param headerTexts 表头文字信息
	 */
	public Header(String[][] headerTexts) {
		this(headerTexts[headerTexts.length - 1], headerTexts);
	}

	public void setColumnWidths(Integer[] columnWidths) {
		this.columnWidths = columnWidths;
	}
	/**
	 * 获取列宽
	 * @param columnName
	 * @return
	 */
	public int getColumnWidth(String columnName){
		if(columnWidths!=null){
			return columnWidths[colsMap.get(columnName)];
		}
		return DEFAULT_COLUMN_WIDTH;
	}
	
	/**
	 * 获取列宽
	 * @return
	 */
	public Integer[] getColumnWidth(){
		return columnWidths;
	}

	/**
	 * 列显示名称
	 * @param colName
	 * @return
	 */
	public String getHeaderText(Object colName) {
		return headerTexts[headerTexts.length - 1][colsMap.get(colName)];
	}

	/**
	 * 得到列的索引
	 * @param colName
	 * @return
	 */
	public Integer getColIndex(Object colName) {
		return colsMap.get(colName);
	}
	
	public String[] getHeaderCols() {
		return headerCols;
	}

	public String[][] getHeaderTexts() {
		return headerTexts;
	}

	public String[] getMergeCols() {
		return mergeCols;
	}

	public void setMergeCols(String[] mergeCols) {
		this.mergeCols = mergeCols;
	}

	public int getFreeze() {
		return freeze;
	}

	public void setFreeze(int freeze) {
		this.freeze = freeze;
	}

	public boolean isRowCount() {
		return isRowCount;
	}

	public void setRowCount(boolean isRowCount) {
		this.isRowCount = isRowCount;
	}
	
	public int getTitleRowCount(){
		return this.headerTexts.length;
	}
}
