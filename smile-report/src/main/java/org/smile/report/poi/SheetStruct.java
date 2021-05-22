package org.smile.report.poi;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
/**
 * 一个sheet 内容
 * @author 胡真山
 *
 */
public class SheetStruct {
	
	private Sheet sheet = null;
	/**
	 * sheet的合同单元格信息
	 */
	private Map<MegerCellIndex, CellRangeAddress> mergeCell;
	
	private int columnCount;

	public Sheet getSheet() {
		return sheet;
	}

	public void setSheet(Sheet sheet) {
		this.sheet = sheet;
		initColumnCount();
	}
	
	public List<Picture> getPictures(){
		List<Picture> sheetPictrue=new LinkedList<Picture>();
		if(sheet instanceof HSSFSheet){
			HSSFSheet st=(HSSFSheet)sheet;
			HSSFPatriarch pat=st.getDrawingPatriarch();
			if(pat!=null){
				List<HSSFShape> shapes=pat.getChildren();
				for(HSSFShape shape :shapes){
		            if (shape instanceof Picture) {  
		                sheetPictrue.add((Picture)shape);
		            }  
				}
			}
			
		}else if(sheet instanceof XSSFShape){
			XSSFSheet st=(XSSFSheet)sheet;
			XSSFDrawing draw=st.getDrawingPatriarch();
			if(draw!=null){
				List<XSSFShape> shapes=draw.getShapes();
				for(XSSFShape shape :shapes){
		            if (shape instanceof Picture) {  
		                sheetPictrue.add((Picture)shape);
		            }  
				}
			}
		}
		return sheetPictrue;
	}

	public Map<MegerCellIndex, CellRangeAddress> getMergeCell() {
		return mergeCell;
	}

	public void setMergeCell(Map<MegerCellIndex, CellRangeAddress> mergeCell) {
		this.mergeCell = mergeCell;
	}

	public SheetStruct(Sheet sheet) {
		this.sheet = sheet;
		initColumnCount();
	}
	/**
	 * 初始化合并单元格的信息
	 * @param sheet 
	 * @return
	 */
	public void initMegerCell(){
		mergeCell = PoiSupport.initMegerCells(sheet);
	}

	/**
	 * 是被合并的 第一个单元格不算
	 * @param cell
	 * @return
	 */
	public boolean isBeMeger(Cell cell) {
		MegerCellIndex index=new MegerCellIndex(cell.getRowIndex(), cell.getColumnIndex());
		CellRangeAddress cra=mergeCell.get(index);
		if(cra!=null){
			return index.getRowIndex()!=cra.getFirstRow()||index.getColIndex()!=cra.getFirstColumn();
		}
		return false;
	}
	
	public void initColumnCount(){
		this.columnCount=PoiSupport.getSheetColumnCount(sheet);
	}

	/**
	 * excel的列数  以最长的一行的列数为值
	 * @return
	 */
	public int columnCount() {
		return this.columnCount;
	}
	/**
	 * 行数
	 * @return
	 */
	public int rowCount() {
		return sheet.getLastRowNum() - sheet.getFirstRowNum() + 1;
	}
}
