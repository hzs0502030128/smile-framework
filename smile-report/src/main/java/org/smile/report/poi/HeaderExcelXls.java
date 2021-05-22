package org.smile.report.poi;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

public class HeaderExcelXls extends HeaderExcel{

	public HeaderExcelXls(SheetHeader... header){
		super(header);
	}
	@Override
	protected Class<? extends Workbook> getNewInstanceClass() {
		return HSSFWorkbook.class;
	}

}
