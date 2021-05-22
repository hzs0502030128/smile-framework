package org.smile.report.poi;

import java.io.OutputStream;

public interface ExcelConvert {
	/**
	 * 转换到一个流中
	 * @param excel
	 * @param os
	 */
	public void convert(Excel excel, OutputStream os);
	
}
