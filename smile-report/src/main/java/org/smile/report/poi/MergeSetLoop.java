package org.smile.report.poi;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
/**
 * 用于在合并行的时候循环
 * @author 胡真山
 */
public class MergeSetLoop<T> implements SheetLoop<T>{
	/**合并设置*/
	protected MergeSet<T> config;
	/**暂存原有的值*/
	protected Object oldPropertyValue;
	/**
	 * 第一次出现不同值时候的索引
	 */
	protected int indexRow;
	/**要合并的起始行*/
	protected int startRowIndex;
	/**
	 * @param config 合并配置信息
	 * @param indexRow 起始行
	 */
	public MergeSetLoop(MergeSet<T> config,int indexRow){
		this.config=config;
		this.indexRow=indexRow;
		this.startRowIndex=indexRow;
	}
	/**
	 * 合并行
	 * @param sheet 页
	 * @param oldRow 开始合并的行
	 * @param endRow 合并结束时的行
	 * @param keyIndex 合并的列
	 */
	protected void mergeRow(Sheet sheet,int oldRow,int endRow){
		for(Integer j:config.getMergeColumnIndexs()){
			sheet.addMergedRegion(new CellRangeAddress(oldRow, endRow, j, j));
		}
	}
	/**
	 * 循环数据集合 进行合并行
	 * @param sheet
	 * @param currentObj
	 * @param i
	 */
	public void loop(Sheet sheet,T currentObj,int i){
		Object value=config.getMergeKeyValue(currentObj);
		if(oldPropertyValue!=null&&!config.isNeedMerge(value,oldPropertyValue)){
			if(i>indexRow+1){
				mergeRow(sheet, indexRow, i-1);
			}
			//更新到一次不同的索引
			indexRow=i;
		}
		oldPropertyValue=value;
	}
	
	/**
	 * 尝试合并最后一行
	 * @param sheet
	 * @param i 
	 */
	public void loopLast(Sheet sheet,int i){
		if(i>indexRow+1){
			mergeRow(sheet, indexRow, i-1);
		}
	}
	/**
	 * getter 合并的起始行
	 * @return
	 */
	public int getStartRowIndex() {
		return startRowIndex;
	}
	
}
