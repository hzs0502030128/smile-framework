package org.smile.report.poi;

import java.util.List;

import org.smile.collection.CollectionUtils;
import org.smile.commons.SmileRunException;
import org.smile.json.JSON;
import org.smile.report.Header;

public class SheetHeader extends Header{
	/**
	 * 生成excel 页的sheet名称
	 */
	private String sheetName="sheet";
	/**
	 * 是否按属性填充数据
	 */
	protected boolean isPropertyFill=false;
	/**
	 * 开始填充数据的行
	 */
	protected int startRow=0;
	/**
	 * 开始填充数据的列
	 */
	protected int startColumn=0;
	/**
	 * 合并设置
	 */
	protected List<MergeSet> mergeSets;
	
	
	public void setMergeSet(MergeSet... mergeSet){
		mergeSets=CollectionUtils.linkedList(mergeSet);
	}
	
	/***
	 * 设置合并单元格属性
	 * @param keyProperty 以哪些列作为合并的关键列
	 * @param mergeColumns 需要合并的列
	 */
	public void setMergeSet(String[] keyProperty,String[] mergeColumns){
		Integer[] indexs=new Integer[mergeColumns.length];
		int i=0;
		for(String n:mergeColumns){
			Integer index= colsMap.get(n);
			if(index==null){
				throw new SmileRunException("无法为[ "+n+" ]匹配到列,请在"+JSON.toJSONString(colsMap.keySet())+"中选择要合并的列");
			}
			indexs[i++]=index;
		}
		MergeSet mergeSet=new ObjectMergeSet(keyProperty,indexs);
		mergeSets=CollectionUtils.linkedList(mergeSet);
	}

	public SheetHeader(String[] headerCols, String[][] headerTexts) {
		super(headerCols, headerTexts);
		isPropertyFill=true;
	}

	public SheetHeader(String[][] headerTexts) {
		super(headerTexts[headerTexts.length - 1], headerTexts);
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	/**
	 * 是否按属性填充数据
	 * @return
	 */
	public boolean isPropertyFill() {
		return isPropertyFill;
	}

	public void setPropertyFill(boolean isPropertyFill) {
		this.isPropertyFill = isPropertyFill;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getStartColumn() {
		return startColumn;
	}

	public void setStartColumn(int startColumn) {
		this.startColumn = startColumn;
	}

	public List<MergeSet> getMergeSets() {
		return mergeSets;
	}
	
	
	public int getDataStartRow(){
		return getTitleRowCount()+startRow;
	}
}
