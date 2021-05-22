package org.smile.report.excel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.smile.beans.converter.BasicConverter;
import org.smile.beans.converter.ConvertException;
import org.smile.commons.SmileRunException;
import org.smile.reflect.Generic;

/**
 * 
 * @author 胡真山
 *
 */
public class XlsExportDynamicTemplate extends XlsExportTemplate{
	/**
	 * 动态数据配置
	 */
	protected Map<String,Object> dynamic=new HashMap<String,Object>();
	/**
	 * 标题行
	 */
	protected Row titleRow;
	/**
	 * 标题行的索引
	 */
	protected Integer titleRowIndex;
	/**
	 * 标题的内容
	 */
	protected String[] titles;
	/**
	 * 设置动态配置
	 * @param name
	 * @param param
	 */
	public void setDynamic(String name,Object param){
		dynamic.put(name, param);
	}
	
	
	@Override
	protected void doHeaderConfig(Sheet sheet) {
		if(titleRowIndex==null){
			titleRowIndex=dataNameRowIndex-1;
		}
		titleRow=sheet.getRow(titleRowIndex);
		super.doHeaderConfig(sheet);
		titles=getRowStringValues(titleRow, firstCellNum, lastCellNum);
		try {
			doDynamicTemplate(sheet);
		} catch (ConvertException e) {
			throw new SmileRunException("加载动态表头出错",e);
		}
	}
	/**
	 * 标题行索引
	 * @return
	 */
	public Integer getTitleRowIndex() {
		return titleRowIndex;
	}

	/**
	 * 设置标题行索引
	 * @param titleRowIndex
	 */
	public void setTitleRowIndex(Integer titleRowIndex) {
		this.titleRowIndex = titleRowIndex;
	}
	/**
	 * 处理理动态模板
	 * @throws ConvertException 
	 */
	protected void doDynamicTemplate(Sheet sheet) throws ConvertException{
		if(dynamic.size()>0){
			//返回列数
			short lastCellIndex=doDynamicRow(titleRow, titles);
			doDynamicRow(dataNameRow, names);
			this.lastCellNum=lastCellIndex;
			this.names=getRowStringValues(dataNameRow, this.firstCellNum, this.lastCellNum);
		}
	}
	/**
	 * 处理一行动态行
	 * @param namerow
	 * @param rownames
	 * @return
	 * @throws ConvertException
	 */
	public short doDynamicRow(Row namerow,String[] rownames) throws ConvertException{
		short index=firstCellNum;
		short lastCellIndex=lastCellNum;
		for(String name:rownames){
			if(ExpressionUtils.isDynamicExp(name)){
				String key=ExpressionUtils.getDynamicExpName(name);
				Object value=dynamic.get(key);
				List<String> dyNames=BasicConverter.getInstance().convert(List.class,new Generic(new Class[]{String.class}),value);
				Cell oldCell=namerow.getCell(index);
				if(dyNames.size()>1){
					int addColumn=dyNames.size()-1;
					shiftCells(namerow, index+1, lastCellIndex-1, addColumn);
					lastCellIndex+=addColumn;
					short first=index;
					for(String newName:dyNames){
						if(index>first){
							Cell newCell=namerow.createCell(index);
							copyCell(oldCell, newCell, false);
							newCell.setCellValue(newName);
						}else{
							oldCell.setCellValue(newName);
						}
						index++;
					}
				}else{
					oldCell.setCellValue(dyNames.get(0));
					index++;
				}
			}else{
				index++;
			}
			
		}
		return lastCellIndex;
	}
}
