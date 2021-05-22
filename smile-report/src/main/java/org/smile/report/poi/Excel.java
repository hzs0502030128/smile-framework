package org.smile.report.poi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * excel表格
 * @author 胡真山
 *
 */
public interface Excel{
	/**
	 * 获取所有的sheet
	 * @return
	 */
	public Sheet[] getSheets();
	
	/**
	 * 所有sheet的名称
	 * @return
	 */
	public String[] getSheetNames();
	
	/**
	 * sheet个数
	 * @return
	 */
	public int getSheetCount();
	/**
	 * 工作表
	 * @return
	 */
	public Workbook getWorkbook();
	/**
	 * 以输入流的等式读取
	 * @return
	 * @throws IOException 
	 */
	public InputStream toInuptStream() throws IOException;
	
	/***
	 * 把模板的内容写入到流中
	 * @param os 目标输出流
	 * @throws IOException
	 */
	public void write(OutputStream os) throws IOException;
}
