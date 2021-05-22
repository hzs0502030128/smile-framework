package org.smile.report.excel;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.smile.report.poi.BaseExcel;
/**
 * excel模板功能 
 * @author 胡真山
 */
public abstract class Template extends BaseExcel{
	/***
	 * 自定义常量的集合 可以通过在xls模板上 #key 来显示
	 */
	protected Map<String, Object> params;
	/**
	 * 要填充数据的 sheet 索引号
	 */
	protected int sheetIndex = 0;
	/***
	 * 页脚开始行索引
	 */
	protected int bottomRowIndex = -1;
	/***
	 * 页脚开始行索引
	 */
	protected int bottomRowEnd = -1;
	/**
	 * 子集合的数据行号
	 */
	protected int subDataRowNumber = 1;
	/**
	 * 页脚行数
	 */
	protected int bottomRowCount = -1;
	/***
	 * 标题行的索引
	 */
	protected int titleRowIndex = 0;
	/**
	 * 标题行的索引
	 * 
	 * @param titleRowIndex
	 */
	public void setTitleRowIndex(int titleRowIndex) {
		this.titleRowIndex = titleRowIndex;
	}

	public int getBottomRowCount() {
		return bottomRowCount;
	}

	public void setBottomRowCount(int bottomRowCount) {
		this.bottomRowCount = bottomRowCount;
	}

	/**
	 * 设置页脚信息
	 * @param bottomRowIndex 页脚开始行索引
	 * @param rowCount 页脚行数
	 */
	public void setBottomRowIndex(int bottomRowIndex, int rowCount) {
		this.bottomRowIndex = bottomRowIndex;
		this.bottomRowEnd = bottomRowIndex + rowCount-1;
	}

	/**
	 * 要填充数据的 sheet 索引号
	 * 
	 * @param sheetIndex
	 */
	public void setSheetIndex(int sheetIndex) {
		this.sheetIndex = sheetIndex;
	}

	/** 增加一个参数 */
	public void addParam(String name, Object value) {
		if (params == null) {
			params = new HashMap<String, Object>();
		}
		params.put(name, value);
	}
	

	
	/**
	 * 子行号
	 * @return
	 */
	public int getSubDataRowNumber() {
		return subDataRowNumber;
	}
	/**
	 * 获取工作表
	 * @return
	 */
	public Workbook getWorkbook() {
		return workbook;
	}
	/**
	 * 设置sheet页的名称
	 * @param index 要设置的sheet的索引
	 * @param name 新名称
	 */
	public void setSheetName(int index,String name){
		workbook.setSheetName(index, name);
	}

	/**
	 * 获取sheet的名称
	 * @param index
	 * @return
	 */
	public String getSheetName(int index){
		return workbook.getSheetName(index);
	}
	/**
	 * 删除sheet页
	 * @param index 要删除的索引
	 */
	public void removeSheet(int index){
		workbook.removeSheetAt(index);
	}

	
	
}
