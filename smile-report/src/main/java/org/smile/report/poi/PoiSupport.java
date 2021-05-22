package org.smile.report.poi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.smile.beans.BeanUtils;
import org.smile.beans.converter.BaseTypeConverter;
import org.smile.beans.converter.BeanException;
import org.smile.beans.converter.ConvertException;
import org.smile.commons.ExceptionUtils;
import org.smile.commons.SmileRunException;
import org.smile.commons.Strings;
import org.smile.io.IOUtils;
import org.smile.log.LoggerHandler;
import org.smile.reflect.ClassTypeUtils;
import org.smile.report.excel.CellImage;
import org.smile.util.DateUtils;
import org.smile.util.StringUtils;

/**
 * poi支持的一些通用方法
 * @author 胡真山
 *
 */
public class PoiSupport implements LoggerHandler {
	
	protected static final DataFormatter DEFAULT_DATA_FORMTTER=new DataFormatter(); 
	
	protected static final String CUSTOM_FORMT_START="reserved";

	public static final NumberFormat nf = new DecimalFormat("#####.##");

	public static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	/**几种常用的自定义日期格式映射*/
	public static Map<Short,DateFormat> INDEXS_CUSTOM_DATE_FORMAT=new HashMap<Short, DateFormat>();
	
	static {
		INDEXS_CUSTOM_DATE_FORMAT.put((short)31, new SimpleDateFormat("yyyy年M月d日"));
		INDEXS_CUSTOM_DATE_FORMAT.put((short)32, new SimpleDateFormat("h时mm分"));
		INDEXS_CUSTOM_DATE_FORMAT.put((short)58, new SimpleDateFormat("M月d日"));
		INDEXS_CUSTOM_DATE_FORMAT.put((short)180, new SimpleDateFormat("yyyy-MM-dd"));
	}
	
	/**
	 * 默认字符宽度
	 */
	public static final int DEFAULT_FONT_WIDTH=256;
	// DPI = 15 Twips
	// 1 Pixel = 1440 TPI / 96
	private static final short TWIPS_PER_PIEXL = 15;

	/**
	 * 创建Excel文本
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static Workbook createWorkbook(InputStream in) {
		try {
			return WorkbookFactory.create(in);
		} catch (Exception e) {
			throw new SmileRunException("创建excel工作表失败", e);
		} finally {
			IOUtils.close(in);
		}
	}
	/**
	 * 创建Excel表
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static Workbook createWorkbook(File file) {
		InputStream in=null;
		try {
			in=new FileInputStream(file);
			return WorkbookFactory.create(in);
		} catch (Exception e) {
			throw new SmileRunException("创建excel工作表失败", e);
		} finally {
			IOUtils.close(in);
		}
	}

	/** 
     * Sheet复制 
     * @param fromSheet  原sheet
     * @param toSheet  目标sheet
     * @param copyValueFlag 
     */  
    public static void copySheet(Sheet fromSheet,Sheet toSheet,boolean copyValueFlag) {  
        //合并区域处理  
    	copyMergerRegion(fromSheet, toSheet);  
    	Row tmpRow ;
        for (Iterator<Row> rowIt = fromSheet.rowIterator(); rowIt.hasNext();) {  
        	tmpRow=rowIt.next();  
            Row newRow = toSheet.createRow(tmpRow.getRowNum());  
            //行复制  
            copyRow(tmpRow,newRow,copyValueFlag);  
        }  
    }
    /**
     * 是复制打印页面大小设置
     * @param fromSheet
     * @param toSheet
     */
    public static void copySheetPagePrintSet(Sheet fromSheet,Sheet toSheet){
    	PrintSetup ps=fromSheet.getPrintSetup();
    	PrintSetup toPs=toSheet.getPrintSetup();
    	toPs.setFitHeight(ps.getFitHeight());
    	toPs.setFitWidth(ps.getFitWidth());
    	toPs.setScale(ps.getScale());
    	toSheet.setAutobreaks(fromSheet.getAutobreaks());
    }
    /**
     * 复制所有的打印设置
     * @param fromSheet
     * @param toSheet
     */
    public static void copySheetAllPrintSet(Sheet fromSheet,Sheet toSheet){
    	PrintSetup ps=fromSheet.getPrintSetup();
    	PrintSetup toPs=toSheet.getPrintSetup();
    	try {
			BeanUtils.fillProperties(toPs, ps);
			toSheet.setAutobreaks(fromSheet.getAutobreaks());
		} catch (BeanException e) {
			throw new ExcelException("复制打印属性出错", e);
		}
    }
    /**
     * 复制打印页边距
     * @param fromSheet
     * @param toSheet
     */
    public static void copySheetPrintMerge(Sheet fromSheet,Sheet toSheet){
    	 toSheet.setMargin(Sheet.TopMargin,fromSheet.getMargin(Sheet.TopMargin));// 页边距（上）    
    	 toSheet.setMargin(Sheet.BottomMargin,fromSheet.getMargin(Sheet.BottomMargin));// 页边距（下）    
    	 toSheet.setMargin(Sheet.LeftMargin,fromSheet.getMargin(Sheet.LeftMargin) );// 页边距（左）    
    	 toSheet.setMargin(Sheet.RightMargin,fromSheet.getMargin(Sheet.RightMargin));// 页边距（右    
    }
    /**
     * 复制分页参数
     * @param fromSheet
     * @param toSheet
     */
    public static void copyPrintBreaks(Sheet fromSheet,Sheet toSheet){
    	//分页符的拷贝
		int[] rowBreaks = fromSheet.getRowBreaks();
		for (int rowBreaksIndex = 0; rowBreaksIndex < rowBreaks.length; rowBreaksIndex++) {
			toSheet.setRowBreak(rowBreaks[rowBreaksIndex]);
		}
		//分页符的拷贝
		int[] collBreaks = fromSheet.getColumnBreaks();
		for (int i = 0; i < collBreaks.length; i++) {
			toSheet.setColumnBreak(collBreaks[i]);
		}
    }
    /**
     * 复制打印区域
     * @param fromSheet
     * @param toSheet
     */
    public static void copyPrintArea(Sheet fromSheet,Sheet toSheet){
    	//打印区域复制
		Workbook wk=fromSheet.getWorkbook();
		int formSheetIndex=wk.getSheetIndex(fromSheet);
		Workbook toWk=toSheet.getWorkbook();
		int toSheetIndex=toWk.getSheetIndex(toSheet);
		String printArea=wk.getPrintArea(formSheetIndex);
		if(StringUtils.notEmpty(printArea)){
			int index=printArea.indexOf("!");
			printArea=printArea.substring(index+1);
			toWk.setPrintArea(toSheetIndex, printArea);
		}
    }
    
    /**
     * 获取sheet的索引
     * @param sheet
     * @return
     */
    public static int getSheetIndex(Sheet sheet){
    	return sheet.getWorkbook().getSheetIndex(sheet);
    }
    /**
     * sheet的列数  取最大行的列数  
     * 会循环所有的列
     * @param sheet
     * @return
     */
    public static int getSheetColumnCount(Sheet sheet){
    	Iterator<Row> rows = sheet.iterator();
		int colNum = 0;
		while (rows.hasNext()) {
			int rowColNum = 0;
			Row row = rows.next();
			rowColNum=row.getLastCellNum();
			if (rowColNum > colNum) {
				colNum = rowColNum;
			}
		}
		return colNum;
    }
    
    /**
     * 复制sheet中列的宽度
     * @param fromSheet
     * @param toSheet
     */
    public static void copySheetWidth(Sheet fromSheet,Sheet toSheet,int columnCount){
    	for(int i=0;i<columnCount;i++){
    		int width=fromSheet.getColumnWidth(i);
    		toSheet.setColumnWidth(i, width);
    	}
    }
    
	/** 
	* 复制原有sheet的合并单元格到新创建的sheet 
	* @param fromSheet 新创建sheet 
	* @param toSheet   原有的sheet 
	*/
	public static void copyMergerRegion(Sheet fromSheet, Sheet toSheet) {
		int sheetMergerCount = fromSheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergerCount; i++) {
			CellRangeAddress mergedRegionAt = fromSheet.getMergedRegion(i);
			toSheet.addMergedRegion(mergedRegionAt);
		}
	}
	/**
	 * 复制一个批注
	 * @param sheet
	 * @param toSheet
	 * @param source
	 * @param rowIndex
	 * @param colIndex
	 */
	public static void copyComment(Sheet sheet,Sheet toSheet,Comment source,int rowIndex,int colIndex){
		Drawing  draw=toSheet.createDrawingPatriarch();
		ClientAnchor ca=source.getClientAnchor();
		CreationHelper helper=toSheet.getWorkbook().getCreationHelper();
		ClientAnchor ca2=copyClientAnchor(helper, ca, rowIndex, colIndex);
		Comment newComment=draw.createCellComment(ca2);
		newComment.setString(source.getString());
	}
	/**
	 * 
	 * @param source
	 * @param to
	 * @param toRowIndex
	 * @param toColIndex
	 */
	public static ClientAnchor copyClientAnchor(CreationHelper helper,ClientAnchor source,int toRowIndex,int toColIndex){
		ClientAnchor to=helper.createClientAnchor();
		to.setAnchorType(source.getAnchorType());
		to.setCol1(source.getCol1());
		to.setCol2(source.getCol2());
		to.setDx1(source.getDx1());
		to.setDx2(source.getDx2());
		to.setDy1(source.getDy1());
		to.setDy2(source.getDy2());
		to.setRow1(toRowIndex);
		to.setCol1(toColIndex);
		to.setRow2(toRowIndex+source.getRow2()-source.getRow1());
		to.setCol2(toColIndex+source.getCol2()-source.getCol1());
		return to;
	}

	/**
	 * 加载excel
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static Workbook createWorkbook(String fileName) throws IOException {
		BufferedInputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(fileName));
			Workbook workbook = WorkbookFactory.create(is);
			return workbook;
		} catch (Exception e) {
			throw new IOException("创建excel工作表失败" + fileName, e);
		} finally {
			IOUtils.close(is);
		}
	}

	/**
	 * 复制行
	 * @param scrRow 源
	 * @param desRow 目标行
	 * @param copyValue 是否复制值 如为false则不复制内容
	 */
	public static void copyRow(Row scrRow, Row desRow, boolean copyValue) {
		desRow.setHeight(scrRow.getHeight());
		for (Iterator<Cell> cellIt = scrRow.cellIterator(); cellIt.hasNext();) {
			Cell srcCell = cellIt.next();
			Cell newCell = desRow.createCell(srcCell.getColumnIndex());
			copyCell(srcCell, newCell, copyValue);
		}
	}

	/**
	 * getter 方法
	 * @param oneData
	 * @param field
	 * @return
	 * @throws Exception
	 */
	protected static Method getGetMethod(Object oneData, String field) throws Exception {
		return ClassTypeUtils.getGetter(oneData.getClass(), field);
	}

	/***
	 * 反射get方法取属性值
	 * 
	 * @param oneData
	 * @param field
	 * @return
	 * @throws Exception
	 */
	protected static Object invokeGetValue(Object oneData, String field) throws Exception {
		Method method = getGetMethod(oneData, field);
		return method.invoke(oneData);
	}
	
	/***
	 * 获取对象的属性值
	 * @param oneData
	 * @param field
	 * @return 
	 */
	protected static Object getValue(Object oneData,String field){
		if(oneData instanceof Map){
			Map map=(Map)oneData;
			return map.get(field);
		}else{
			try {
				return invokeGetValue(oneData, field);
			} catch (Exception e) {
				logger.error("获取"+oneData+"属性："+field,e);
				return null;
			}
		}
	}
	
	/***
	 * 获取表达式的值
	 * @param oneData 用于取值的对象
	 * @param exp 需取值的属性表达式
	 * @return 取得的值
	 */
	public static Object getPropertyExpValue(Object oneData,String exp){
		if(oneData==null){
			return null;
		}
		int index=exp.indexOf(".");
		if(index>0){
			String name=exp.substring(0,index);
			Object value=getValue(oneData, name);
			if(value instanceof Collection){
				return value;
			}else{
				return getPropertyExpValue(value, exp.substring(index+1));
			}
		}
		return getValue(oneData, exp);
	}

	/***
	 * 获取EXCEL中合并单元格的值
	 * 
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	public static Object getMergedValue(Cell cell) {
		Cell mergedCell = getCellMerged(cell);
		return getCellValue(mergedCell);
	}

	/**
	 * 合并单元格
	 * @param sheet 要合并的单元格
	 * @param startRow 起始行
	 * @param endRow 结束行
	 * @param startColumn 起始列
	 * @param endColumn 结束列
	 */
	public static void merge(Sheet sheet, int startRow, int endRow, int startColumn, int endColumn) {
		sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, startColumn, endColumn));
	}

	/***
	 * 获取单元格的合并单元格 ，如不是合并返回当前单元格
	 * 
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	public static Cell getCellMerged(Cell cell) {
		if(cell==null){
			return null;
		}
		int row = cell.getRowIndex();
		int column = cell.getColumnIndex();
		Sheet sheet = cell.getSheet();
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			int firstColumn = range.getFirstColumn();
			int lastColumn = range.getLastColumn();
			int firstRow = range.getFirstRow();
			int lastRow = range.getLastRow();
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					return sheet.getRow(firstRow).getCell(firstColumn);
				}
			}
		}
		return cell;
	}

	/**
	 * @param sheet
	 * @return
	 */
	public static Map<MegerCellIndex, CellRangeAddress> initMegerCells(Sheet sheet) {
		Map<MegerCellIndex, CellRangeAddress> mergeCell = new HashMap<MegerCellIndex, CellRangeAddress>();
		int num = sheet.getNumMergedRegions();
		int j = 0;
		int k = 0;
		for (int i = 0; i < num; i++) {
			CellRangeAddress cra = sheet.getMergedRegion(i);
			int firstRow = cra.getFirstRow();
			int firstCol = cra.getFirstColumn();
			int lastRow = cra.getLastRow();
			int lastCol = cra.getLastColumn();
			for (j = firstRow; j <= lastRow; j++) {
				for (k = firstCol; k <= lastCol; k++) {
					MegerCellIndex index = new MegerCellIndex(j, j);
					mergeCell.put(index, cra);
				}
			}
		}
		return mergeCell;
	}

	/***
	 * 获取一个单元格的值
	 * 
	 * @param cell
	 * @return
	 */
	public static Object getCellValue(Cell cell) {
		if (cell != null) {
			try {
				switch (cell.getCellType()) {
					case Cell.CELL_TYPE_NUMERIC: {
						if (DateUtil.isCellDateFormatted(cell)) {
							//日期格式
							return cell.getDateCellValue();
						}else{
							short dataformt=cell.getCellStyle().getDataFormat();
							if(INDEXS_CUSTOM_DATE_FORMAT.containsKey(dataformt)){
								// 处理自定义日期格式、时间格式
								return cell.getDateCellValue();
							}else{
								return cell.getNumericCellValue();
							}
						}
					}
					case Cell.CELL_TYPE_STRING: {
						return cell.getStringCellValue();
					}
					case Cell.CELL_TYPE_FORMULA: {
						String formula = cell.getCellFormula();
						if (formula.indexOf("DATE(") >= 0) {
							return DateUtil.getJavaDate(cell.getNumericCellValue());
						} else if (formula.indexOf("SUM(") >= 0) {
							return cell.getNumericCellValue();
						} else {
							try {
								return cell.getNumericCellValue();
							} catch (Exception e) {
								logger.error("获取单元格内容出错"+cell.getRowIndex()+","+cell.getColumnIndex(),e);
							}
						}
					}
					case Cell.CELL_TYPE_BOOLEAN: {
						return cell.getBooleanCellValue();
					}
					default: {
						return Strings.BLANK;
					}
				}
			} catch (Exception ex) {
				logger.info(ExceptionUtils.getExceptionMsg(ex));
			}
		}
		return Strings.BLANK;
	}

	/***
	 * 结果 转为string 类型
	 * @param value
	 * @return
	 */
	public static String convertToString(Object value) {
		if (value == null) {
			return null;
		} else if (value instanceof String) {
			return (String) value;
		} else if (value instanceof Date) {
			return df.format((Date) value);
		}
		if (value instanceof byte[]) {
			return new String((byte[]) value);
		} else if (value instanceof Byte[]) {
			Byte[] bt = (Byte[]) value;
			byte[] b = new byte[bt.length];
			for (int i = 0; i < b.length; i++) {
				b[i] = bt[i].byteValue();
			}
			return new String(b);
		} else {
			return String.valueOf(value);
		}
	}

	/**
	 * 获取单元格的值返回字符串类型
	 * @param cell
	 * @return
	 */
	public static String getStringValue(Cell cell) {
		Object value = getCellValue(cell);
		if (value instanceof String) {
			return (String) value;
		} else if (value instanceof Number) {
			return nf.format(value);
		} else if (value instanceof Date) {
			return df.format(value);
		} else {
			return String.valueOf(value);
		}
	}

	/***
	 * 设置一个单元格的值
	 * @param c
	 * @param value
	 */
	public static void setCellValue(Cell c, Object value) {
		if (value == null) {
			return;
		}
		int srcCellType = c.getCellType();

		if (value instanceof CellImage) {// 如果是图片
			// 图片
			setImageCell(c, (CellImage) value);
		} else if (srcCellType == Cell.CELL_TYPE_NUMERIC) {
			if (DateUtil.isCellDateFormatted(c)) {
				c.setCellValue(DateUtils.parseDate(String.valueOf(value)));
			} else {
				if (value instanceof Number) {
					c.setCellValue(((Number) value).doubleValue());
				} else if (value instanceof String) {
					c.setCellValue(Double.valueOf((String) value));
				}
			}
		} else if (srcCellType == Cell.CELL_TYPE_BOOLEAN) {
			try {
				c.setCellValue(BaseTypeConverter.getInstance().convert(Boolean.class, value));
			} catch (ConvertException e) {
				logger.error("设置boolean格式单元格出错"+c.getRowIndex()+"-"+c.getColumnIndex(),e);
			}
		} else {
			if (value instanceof Number) {
				c.setCellValue(((Number) value).doubleValue());
			} else {
				c.setCellValue(convertToString(value));
			}
		}
	}

	/**
	 * 设置单元格内容为一个图片
	 * @param cell
	 * @param image
	 */
	public static void setImageCell(Cell cell, CellImage image) {
		Sheet sheet = cell.getSheet();
		Workbook workbook = sheet.getWorkbook();
		int pictureIdx = workbook.addPicture(image.getImages(), image.getType());
		CreationHelper helper = workbook.getCreationHelper();
		Drawing drawing = cell.getSheet().createDrawingPatriarch();
		ClientAnchor anchor = helper.createClientAnchor();
		// 图片插入坐标
		anchor.setCol1(cell.getColumnIndex());
		anchor.setRow1(cell.getRowIndex());
		anchor.setDx1(image.getLeft());
		anchor.setDy1(image.getTop());
		// 插入图片
		Picture pict = drawing.createPicture(anchor, pictureIdx);
		if (image.isResize()) {
			pict.resize(image.getWidth(), image.getHeight());
		} else {
			pict.resize();
		}
		cell.setCellValue(Strings.BLANK);
	}

	/***
	 * 获取一行数据字符串类型值
	 * @param row 数据行
	 * @param first
	 * @param last
	 * @return
	 */
	public static String[] getRowStringValues(Row row, short first, short last) {
		int length = last - first;
		String[] values = new String[length];
		for (int i = first; i < last; i++) {
			Cell c = row.getCell(i);
			try {
				values[i] = getStringValue(c);
			} catch (Exception e) {
				throw new ExcelException("get cell value for string error,sheet "+row.getSheet().getSheetName()+" column index " + i + " row " + row.getRowNum(), e);
			}
		}
		return values;
	}

	/**
	 * 移动一个行的单元格
	 * @param row 要移动的行
	 * @param startColIndex 起始单元格列索引
	 * @param endColIndex 结束单元格列索引
	 * @param count 要右移的列数
	 */
	public static void shiftCells(Row row, int startColIndex, int endColIndex, int count) {
		int columnCount = endColIndex - startColIndex + 1;
		if (columnCount > 0) {
			Object[] cellsValues = new Object[columnCount];
			CellStyle[] cellStyles = new CellStyle[columnCount];
			int[] cellsTypes = new int[columnCount];
			//保存原有单元格信息 修改2007格式单元格创建后会共享内容导致问题
			int index=0;
			for (int i = startColIndex; i<= endColIndex; i++) {
				Cell cell=row.getCell(i);
				cellsValues[index]=getCellValue(cell);
				cellStyles[index]=cell.getCellStyle();
				cellsTypes[index]=cell.getCellType();
				index++;
			}
			int newlastIndex = endColIndex + count;
			//开始赋值
			index=0;
			for (int i = startColIndex + count; i <= newlastIndex; i++) {
				Cell newcell = row.createCell(i);
				setCellValue(newcell, cellsValues[index]);
				newcell.setCellStyle(cellStyles[index]);
				newcell.setCellType(cellsTypes[index]);
				index++;
			}
		}
	}

	/***
	 * 获取EXCEL中合并单元格的值 以String 类型返回
	 * @param cell 要获取值的单元格
	 * @return 字符串类型
	 */
	public static String getMergedStringValue(Cell cell) {
		Cell mergedCell = getCellMerged(cell);
		return getStringValue(mergedCell);
	}

	/**
	 * 复制单元格
	 * @param srcCell 源单元格
	 * @param desCell 目标单元格
	 * @param copyValue 是否复制内容值
	 */
	public static void copyCell(Cell srcCell, Cell desCell, boolean copyValue) {
		desCell.setCellStyle(srcCell.getCellStyle());
		desCell.setCellType(srcCell.getCellType());
		if (copyValue) {
			setCellValue(desCell, getCellValue(srcCell));
		}
	}
	/**
	 * 克隆单元格样式
	 * @param sourceCellStyle
	 * @param sourceWorkBook
	 * @param targetWorkBook
	 * @return
	 */
	public static CellStyle cloneCellStyle(CellStyle sourceCellStyle,Workbook sourceWorkBook,Workbook targetWorkBook){
			CellStyle cStyle=targetWorkBook.createCellStyle();
			//是否换行
			cStyle.setWrapText(sourceCellStyle.getWrapText());
			Font font=targetWorkBook.createFont();
			Font sourceFont=sourceWorkBook.getFontAt(sourceCellStyle.getFontIndex());
			try {
				BeanUtils.fillProperties(sourceFont, font);
			} catch (BeanException e) {
				throw new SmileRunException("复制字体属性出错",e);
			}
			//字体拷贝
			cStyle.setFont(font);
			//拷贝对齐方式
			cStyle.setAlignment(sourceCellStyle.getAlignment());
			cStyle.setVerticalAlignment(sourceCellStyle.getVerticalAlignment());
			//边框拷贝
			cStyle.setBorderBottom(sourceCellStyle.getBorderBottom());
			cStyle.setBorderLeft(sourceCellStyle.getBorderLeft());
			cStyle.setBorderRight(sourceCellStyle.getBorderRight());
			cStyle.setBorderTop(sourceCellStyle.getBorderTop());
			cStyle.setBottomBorderColor(sourceCellStyle.getBottomBorderColor());
			cStyle.setLeftBorderColor(sourceCellStyle.getLeftBorderColor());
			cStyle.setRightBorderColor(sourceCellStyle.getRightBorderColor());
			cStyle.setTopBorderColor(sourceCellStyle.getTopBorderColor());
			//别的样式的拷贝
			return cStyle;
	}
	/**
	 * 相素转poi长度
	 * @param pixel
	 * @return
	 */
	public static short pixel2PoiHeight(int pixel) {
		return (short) (pixel * TWIPS_PER_PIEXL);
	}

	/**
	 * poi长度转相素
	 * @param height
	 * @return
	 */
	public static float poi2PixelHeight(float height) {
		return height / TWIPS_PER_PIEXL;
	}

	public static float poiWidthPixels2Width(float width) {
		return width * 96 / 72;
	}
	/**
	 * 工作表 的Sheet页
	 * @param workbook
	 * @return
	 */
	public static Sheet[] getSheets(Workbook workbook) {
		int sheetNum = workbook.getNumberOfSheets();
		Sheet[] sheets=new Sheet[sheetNum];
		for (int i = 0; i < sheetNum; i++) {
			sheets[i] = workbook.getSheetAt(i);
		}
		return sheets;
	}
	
	/**
	 * 工作表 的Sheet页名称
	 * @param workbook
	 * @return
	 */
	public static String[] getSheetNames(Workbook workbook) {
		int sheetNum = workbook.getNumberOfSheets();
		String[] sheets=new String[sheetNum];
		for (int i = 0; i < sheetNum; i++) {
			sheets[i] = workbook.getSheetAt(i).getSheetName();
		}
		return sheets;
	}

	/**
	 * 合并行
	 * 相同的行合并
	 * @param sheet
	 * @param startColumn
	 * @param endColumn
	 */
	public static void mergeColumns(Sheet sheet,int startColumn,int endColumn){
		int lastRowIndex=sheet.getLastRowNum();
		for(int colIndex=startColumn;colIndex<endColumn;colIndex++){
			String temp=null;
			int startRowIndex=0;
			int currentRowIndex;
			for(currentRowIndex=0;currentRowIndex<=lastRowIndex;currentRowIndex++){
				Row row=sheet.getRow(currentRowIndex);
				if(row!=null){
					Cell cell=row.getCell(colIndex);
					String value=cell.getStringCellValue();
					if(temp!=null&&!temp.equals(value)){
						if(currentRowIndex>startRowIndex+1){
							sheet.addMergedRegion(new CellRangeAddress(startRowIndex, currentRowIndex-1,colIndex,colIndex));
						}
						//更新到一次不同的索引
						startRowIndex=currentRowIndex;
					}
					temp=value;
				}
			}
			if(currentRowIndex>startRowIndex+1){
				sheet.addMergedRegion(new CellRangeAddress(startRowIndex, currentRowIndex-1,colIndex,colIndex));
			}
		}
	}
	/**
	 * 设置边框
	 * @param style
	 * @param border
	 */
	public static void setCellStyleBorder(CellStyle style,short border){
		style.setBorderBottom(border);
		style.setBorderTop(border);
		style.setBorderLeft(border);
		style.setBorderRight(border);
	}
	
	/**
	 * 获取单元格格式化以后的内容
	 * @return
	 */
	public static String getCellFormatedValue(Cell cell,DataFormatter formatter){
		CellStyle style=cell.getCellStyle();
		if(isCustomDataFormat(style)){
			Object data=PoiSupport.getCellValue(cell);
			if(data instanceof Date){
				DateFormat format=INDEXS_CUSTOM_DATE_FORMAT.get(style.getDataFormat());
				if(format==null){
					throw new SmileRunException("不支持的单元格格式"+style.getDataFormat());
				}
				return format.format((Date)data);
			}else{
				return String.valueOf(data);
			}
		}else{
			return formatter.formatCellValue(cell);
		}
	}
	/**
	 * 获取格式化以后的内容
	 * @param cell
	 * @return
	 */
	public static String getCellFormatedValue(Cell cell){
		return getCellFormatedValue(cell, DEFAULT_DATA_FORMTTER);
	}
	/**
	 * 是否是自定义格式化格式
	 * @param cellStyle
	 * @return
	 */
	public static boolean isCustomDataFormat(CellStyle cellStyle){
		String formatTxt=cellStyle.getDataFormatString();
		if(StringUtils.isEmpty(formatTxt)){
			return true;
		}
		return formatTxt.startsWith(CUSTOM_FORMT_START);
	}
	/**
	 * 获取单元格
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	public static Cell getCell(Sheet sheet,int row,int column){
		Row r=sheet.getRow(row);
		if(r!=null){
			return r.getCell(column);
		}
		return null;
	}
	/**
	 * 先从单元格中获取 如单元格为空 再从sheet中获取
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	public static Comment getCellComment(Sheet sheet,int row,int column){
		Cell cell=getCell(sheet, row, column);
		if(cell!=null){
			return cell.getCellComment();
		}
		return sheet.getCellComment(row, column);
	}
	/**
	 * 合并一个sheet的行
	 * @param sheet
	 * @param dataSource 以填充的数据合并
	 * @param loops 循环
	 * @param startIndex 开始
	 */
	public static void merge(Sheet sheet,Collection<?> dataSource,List<MergeSetLoop<Object>> loops, int startIndex){
		//当前循环的索引
		int i=startIndex;
		for(Object oneData:dataSource){
			for(MergeSetLoop<Object> loop:loops){
				loop.loop(sheet, oneData, i);
			}
			i++;
		}
		//最后一行
		for(MergeSetLoop<Object> loop:loops){
			loop.loopLast(sheet,i);
		}
	}
	/**
	 * 合并一个sheet的行
	 * @param sheet
	 * @param loops
	 * @param startRowIndex
	 * @param endRowIndex
	 */
	public static void merge(Sheet sheet,List<MergeSetLoop<Row>> loops, int startRowIndex,int endRowIndex){
		//当前循环的索引
		int i=startRowIndex;
		for(;i<endRowIndex;i++){
			Row row=sheet.getRow(i);
			for(MergeSetLoop<Row> loop:loops){
				loop.loop(sheet, row, i);
			}
			i++;
		}
		//最后一行
		for(MergeSetLoop<Row> loop:loops){
			loop.loopLast(sheet,i);
		}
	}
}
