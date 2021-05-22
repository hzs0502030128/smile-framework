package org.smile.report.excel.pdf;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.smile.commons.SmileRunException;
import org.smile.commons.Strings;
import org.smile.log.LoggerHandler;
import org.smile.report.poi.ExcelConvertException;
import org.smile.report.poi.MegerCellIndex;
import org.smile.report.poi.PoiSupport;
import org.smile.report.poi.SheetStruct;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPRow;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class PdfBuilder implements LoggerHandler{
	
	private Document doc;
	
	private PdfWriter writer;
	
	private List<PDFTable> tables = new ArrayList<PDFTable>();
	
	private PdfPageSetting pageSetting = new PdfPageSetting();
	
	/**
	 * 创建文档
	 * @param os
	 * @return
	 */
	public void buildDocument(OutputStream os){
		doc = new Document();
		try {
			writer = PdfWriter.getInstance(doc, os);
		} catch (DocumentException e) {
			throw new ExcelConvertException("构建pdf文档出错", e);
		}
	}
	
	public void buildTable(SheetStruct sheet){
		int colNum = sheet.columnCount();
		if(colNum<=0){
			return;
		}
		Sheet st=sheet.getSheet();
		float[] ratio = new float[colNum];
		//总宽度
		float totalW=0;
		for(int i=0;i<colNum;i++){
			ratio[i]=st.getColumnWidthInPixels(i);
			totalW+=ratio[i];
		}
		//宽度比例
		for(int i = 0; i < ratio.length; i++){
			ratio[i]=ratio[i]/totalW;
		}
		PdfPTable table = new PdfPTable(ratio);
		table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		int cellNum = colNum*sheet.rowCount();
		for(int i =0 ;i<cellNum;i++){
			table.addCell(new PdfParagraph(Strings.BLANK));
		}
		//合计高度
		float heigth=0;
		for(int i=0;i<sheet.rowCount();i++){
			Row row=st.getRow(i);
			if(row!=null){
				heigth+=st.getRow(i).getHeight();
			}else{
				heigth+=st.getDefaultRowHeight();
			}
		}
		pageSetting.setAutoPagesize(PoiSupport.poiWidthPixels2Width(totalW), PoiSupport.poi2PixelHeight(heigth));
		
		Iterator<Row> rows = st.rowIterator();
		sheet.initMegerCell();
		while(rows.hasNext()){
			Row row = rows.next();
			Iterator<Cell> cells = row.cellIterator();
			while(cells.hasNext()){
				Cell cell = cells.next();
				if(sheet.isBeMeger(cell)){
					table.getRow(cell.getRowIndex()).getCells()[cell.getColumnIndex()]=null;
					continue;
				}
				MegerCellIndex index = new MegerCellIndex(cell.getRowIndex(),cell.getColumnIndex());
				cell.setCellType(Cell.CELL_TYPE_STRING);
				if(sheet.getMergeCell().containsKey(index)){
					CellRangeAddress cra = sheet.getMergeCell().get(index);
					int colSpan = cra.getLastColumn()-cra.getFirstColumn()+1;
					int rowSpan = cra.getLastRow()-cra.getFirstRow()+1;
					PdfPCell pdfcell = table.getRow(cra.getFirstRow()).getCells()[cra.getFirstColumn()];
					pdfcell.setColspan(colSpan);
					pdfcell.setRowspan(rowSpan);
				}
				String val = cell.getStringCellValue();
				PdfPRow pdfrow=table.getRow(cell.getRowIndex());
				PdfPCell pdfcell = pdfrow.getCells()[cell.getColumnIndex()];
				pdfcell.setPhrase(new PdfParagraph(val));
				StyleConvertUtils.syncStyle(pageSetting,cell, pdfcell);
			}
		}
		table.setWidthPercentage(100);
		PDFTable pdftbl=new PDFTable(table);
		//处理图片
		List<Picture> pictures=sheet.getPictures();
		for(Picture pic:pictures){
			try {
				Image image=Image.getInstance(pic.getPictureData().getData());
				PdfPCell cell=table.getRow(pic.getClientAnchor().getRow1()).getCells()[pic.getClientAnchor().getCol1()];
				cell.addElement(new Chunk(image,0,0));
			} catch (Exception e) {
				throw new SmileRunException("图片转换失败", e);
			}
		}
		tables.add(pdftbl);
	}
	
	public PdfBuilder build(){
		doc.setPageSize(pageSetting.getRectangle());
		//将多出的一行多出的0.3毫米转化为像素
		float left =(float) (0.3*72/2.54);
		doc.setMargins(pageSetting.getMarginLeft(), 
				pageSetting.getMarginRight()-left, 
				pageSetting.getMarginTop(), 
				pageSetting.getMarginBottom());
		doc.open();
		try {
			for(PDFTable table : tables){
				doc.add(table.table);
				doc.newPage();
				table.table.flushContent();
			}
		} catch (DocumentException e) {
			throw new ExcelConvertException("构建pdf失败", e);
		}
		doc.close();
		return this;
	}

	public PdfWriter getWriter() {
		return writer;
	}

	public PdfPageSetting getPageSetting() {
		return pageSetting;
	}
	
}
