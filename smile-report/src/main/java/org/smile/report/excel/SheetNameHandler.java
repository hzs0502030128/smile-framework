package org.smile.report.excel;

public interface SheetNameHandler {
	/**
	 * 新sheet名称
	 * @param oldName
	 * @param index
	 * @return
	 */
	public String createSheetName(String oldName);
}
