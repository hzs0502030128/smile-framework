package org.smile.report.poi;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.smile.beans.BeanUtils;
import org.smile.collection.CollectionUtils;
import org.smile.commons.SmileRunException;
import org.smile.commons.Strings;
import org.smile.function.BaseFunctionInfo;
import org.smile.json.JSON;
import org.smile.report.function.IFunction;
/**
 * excel表格操作
 * @author 胡真山
 */
public abstract class HeaderExcel extends BaseExcel{
	
	/**
	 * 默认的表头行高
	 */
	protected static final int DEFAULT_HEADER_ROW_H=18;
	
	/**默认的数据行高*/
	protected static final int DEFAULT_DATA_ROW_H=15;
	/**
	 * 表头信息
	 */
	protected SheetHeader[] headers;
	/**
	 * 标题单元格样式
	 */
	protected CellStyle headerCellStyle;
	/**
	 * 数据单元格样式
	 */
	protected CellStyle dataCellStyle;
	
	
	public HeaderExcel(SheetHeader... headers){
		try {
			this.workbook=getNewInstanceClass().newInstance();
			this.headers=headers;
			initCellStyle();
			for(SheetHeader header:headers){
				Sheet sheet=workbook.createSheet(header.getSheetName());
				createTitleRows(sheet, header);
				setColumnWidth(sheet, header);
			}
		} catch (Exception e) {
			throw new SmileRunException(e);
		}
	}
	
	/**
	 * 初始化数据单元格样式和表头样式
	 */
	protected void initCellStyle(){
		this.dataCellStyle=createDefaultDataStyle(workbook);
		this.headerCellStyle=createDefaultHeadStyle(workbook);
	}
	/**
	 * 初始化工作表  可能是2007 2003格式
	 * @return
	 */
	protected abstract Class<? extends Workbook> getNewInstanceClass();
	
	/**
	 * 设置表格列的宽度
	 * @param sheet
	 * @param header
	 */
	protected void setColumnWidth(Sheet sheet,SheetHeader header){
		Integer[] columnWidths=header.getColumnWidth();
		if(columnWidths!=null){
			for(int i=0;i<columnWidths.length;i++){
				sheet.setColumnWidth(i, DEFAULT_FONT_WIDTH*columnWidths[i]);
			}
		}
	}
	
	/**
	 * 创建一个sheet的表头
	 * @param sheet
	 * @param header
	 */
	protected void createTitleRows(Sheet sheet,SheetHeader header){
		int startRow=header.getStartRow();
		int startColumn=header.getStartColumn();
		for(String[] title:header.getHeaderTexts() ){
			createTitleRows(sheet, title, startRow++, startColumn);
		}
		int cols=header.getHeaderCols().length;
		mergeColumns(sheet, startColumn, startColumn+cols);
	}
	/**
	 * 以一个数组方式创建一个标题行
	 * 
	 * @param row
	 * @param title
	 */
	protected void createTitleRows(Sheet sheet,String[] title,int rowIndex,int startColumn) {
		int length = title.length;
		Row row=sheet.createRow(rowIndex);
		int mergeStartColumn=startColumn;
		String mergeStartColumnName=title[0];
		for (int i = startColumn; i < length; i++) {
			Cell cell = row.createCell(i);
			cell.setCellValue(title[i]);
			cell.setCellStyle(headerCellStyle);
			if(!mergeStartColumnName.equals(title[i])){
				if(i-mergeStartColumn>1){
					merge(sheet, rowIndex, rowIndex, mergeStartColumn, i-1);
				}
				mergeStartColumn=i;
			}else if(i==length-1){
				if(i-mergeStartColumn>0){
					merge(sheet, rowIndex, rowIndex, mergeStartColumn, i);
				}
			}
			mergeStartColumnName=title[i];
		}
	}
	/**
	 * 默认表头单元格样式 
	 * @param workbook
	 * @return
	 */
	protected  CellStyle createDefaultHeadStyle(Workbook workbook){
		CellStyle style = workbook.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		setCellStyleBorder(style, CellStyle.BORDER_THIN);
		style.setFont(font);
		style.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		return style;
	}
	
	/**
	 * 默认表头单元格样式 
	 * @param workbook
	 * @return
	 */
	protected  CellStyle createDefaultDataStyle(Workbook workbook){
		CellStyle style = workbook.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		setCellStyleBorder(style, CellStyle.BORDER_THIN);
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 10);
		style.setFont(font);
		return style;
	}
	
	/**
	 * 从一个列表中创建出一个工作空间
	 * 
	 * @param list
	 * @param sheetName
	 * @return
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws Exception
	 */
	public void fillDataList(int sheetIndex,Collection<?> list){
		try{
			/**创建表中的数据信息*/
			SheetHeader header=headers[sheetIndex];
			int rowNum = header.getDataStartRow();
			Sheet sheet=this.workbook.getSheetAt(sheetIndex);
			dataRowNumber=0;
			for (Object rowData:list) {
				dataRowNumber++;
				Row row = createDataRow(sheet,rowNum++);
				if(header.isPropertyFill()){
					fillPropertyRowData(header, row, rowData);
				}else{
					fillRowData(header, row, rowData);
				}
			}
			merge(sheet, header, list);
		}catch(Exception e){
			throw new SmileRunException("创建excel出错", e);
		}
	}
	/**
	 * 
	 * @param list
	 */
	public void fillDataList(Collection<?> list){
		fillDataList(0,list);
	}
	/**
	 * 根据要合并的单元格信息对所有单元格进行合并
	 * @param sheet
	 */
	protected void merge(Sheet sheet,SheetHeader header,Collection<?> dataSource){
		if(CollectionUtils.notEmpty(header.getMergeSets())){
			List<MergeSetLoop<Object>> loops=new LinkedList<MergeSetLoop<Object>>();
			//初始化合并循环
			List<MergeSet> mergeSets=header.getMergeSets();
			int startRowIndex=header.getDataStartRow();
			for(MergeSet config:mergeSets){
				loops.add(new MergeSetLoop<Object>(config, startRowIndex));
			}
			merge(sheet,dataSource,loops, startRowIndex);
		}
	}
	/**
	 * 创建数据行
	 * @param sheet
	 * @param rowNum
	 * @return
	 */
	protected Row createDataRow(Sheet sheet,int rowNum){
		Row row = sheet.createRow(rowNum++);
		row.setHeightInPoints(DEFAULT_DATA_ROW_H);
		return row;
	}
	/**
	 * 创建表头行
	 * @param sheet
	 * @param rowNum
	 * @return
	 */
	protected Row createHeaderRow(Sheet sheet,int rowNum){
		Row row = sheet.createRow(rowNum++);
		row.setHeightInPoints(DEFAULT_HEADER_ROW_H);
		return row;
	}
	
	/**
	 * 创建一个数据单元格
	 * @param row 
	 * @param cellIndex
	 * @return
	 */
	protected Cell createDataCell(Row row,int cellIndex){
		Cell cell=row.createCell(cellIndex);
		cell.setCellStyle(dataCellStyle);
		return cell;
	}
	/**
	 * 不使用属性填充数据行 
	 * 即使是 map的时候也按顺序进行填充
	 * @param header
	 * @param row
	 * @param rowData
	 */
	protected void fillRowData(SheetHeader header,Row row,Object rowData){
		int colNum = header.getStartColumn();
		if(rowData instanceof Object[]){
			Object[] temp=(Object[])rowData;
			for(Object obj:temp){
				createDataCell(row,colNum++).setCellValue(convertToString(obj));
			}
		}else if(rowData instanceof Map){
			Map map=(Map)rowData;
			for(Object obj:map.values()){
				createDataCell(row,colNum++).setCellValue(convertToString(obj));
			}
		}else{
			try {
				Map map= BeanUtils.mapFromBean(rowData);
				for(Object obj:map.values()){
					createDataCell(row,colNum++).setCellValue(convertToString(obj));
				}
			} catch (Exception e) {
				logger.error("填充excel行数据出错 row:"+row.getRowNum()+" 数据："+JSON.toJSONString(rowData),e);
			}
		}
	}
	/**
	 * 使用属性填充数据行
	 * @param header
	 * @param row
	 * @param rowData
	 */
	protected void fillPropertyRowData(SheetHeader header,Row row,Object rowData){
		int colNum = header.getStartColumn();
		String[] cols=header.getHeaderCols();
		if(rowData instanceof Object[]){
			Object[] temp=(Object[])rowData;
			for(String col:cols){
				int index=Integer.valueOf(col);
				setCellValueData(createDataCell(row,colNum++), temp[index]);
			}
		}else{
			for(String col:cols){
				Object value=getCellExpValue(rowData, col);
				setCellValueData(createDataCell(row,colNum++),value);
			}
		}
	}
	
	protected void setCellValueData(Cell cell,Object value){
		setCellValue(cell, value);
	}
	
	/**
	 * 从单元格的表达式计算出单元格的值
	 * 一个单元格的值
	 * @param oneData
	 * @param exp 单元格中的表达式 可以是属性  变量  定义的常量
	 * @return
	 */
	private Object getCellExpValue(Object oneData,String exp){
		if(exp.startsWith(VALIBALE)){
			//template属性变量表达式
			try {
				return invokeGetValue(this, exp.substring(2));
			} catch (Exception e) {
				logger.error("获取变量"+exp+"出错",e);
				return Strings.BLANK;
			}
		}else{
			BaseFunctionInfo fun=functionHandler.getFunctionInfo(exp);
			if(functionHandler.isFunction(fun)){
				//函数表达式
				IFunction function= functionHandler.getFunction(fun);
				Object value=null;
				String argExp=fun.getArgExpression()[0];
				//函数是不是需要当前的表达式取值用于转换
				if(function.needFieldValue()){
					value=getPropertyExpValue(oneData,argExp);
				}
				return function.convert(oneData,argExp,value);
			}
			return getPropertyExpValue(oneData, exp);
		}
			
	}
	
	public void write(OutputStream os) throws IOException{
		this.workbook.write(os);
	}
	
	public void registFunction(String name,IFunction function){
		this.functionHandler.registerFunction(name, function);
	}
}
