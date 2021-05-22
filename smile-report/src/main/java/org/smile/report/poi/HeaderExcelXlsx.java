package org.smile.report.poi;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class HeaderExcelXlsx extends HeaderExcel{

	public  HeaderExcelXlsx(SheetHeader ...header) {
		super(header);
	}
	@Override
	protected Class<? extends Workbook> getNewInstanceClass() {
		return XSSFWorkbook.class;
	}

}
