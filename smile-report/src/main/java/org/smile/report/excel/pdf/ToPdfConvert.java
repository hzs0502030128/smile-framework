package org.smile.report.excel.pdf;

import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.smile.report.poi.Excel;
import org.smile.report.poi.ExcelConvert;
import org.smile.report.poi.SheetStruct;
/**
 * excel使用poi转pdf
 * @author 胡真山
 *
 */
public class ToPdfConvert implements ExcelConvert{
	
	private PdfBuilder pdf;
	
	public ToPdfConvert(PdfBuilder pdf){
		this.pdf=pdf;
	}
	@Override
	public void convert(Excel excel, OutputStream os) {
		pdf.buildDocument(os);
		for(Sheet sheet : excel.getSheets()){
			SheetStruct sheetStruct = new SheetStruct(sheet);
			pdf.buildTable(sheetStruct);
		}
		pdf.build();
	}

}
