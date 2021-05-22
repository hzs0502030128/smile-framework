package org.smile.report.poi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.smile.io.ByteArrayInputStream;
import org.smile.io.ByteArrayOutputStream;
import org.smile.report.function.BaseFunctionHandler;
import org.smile.report.function.FunctionHandler;

public class BaseExcel extends PoiSupport implements Excel{
	/**
	 * 常量表达式标识
	 * */
	protected static final String PARAM_FLAG = "#";
	/**
	 * 变量表达式 在模板上以 如 ##dataRowNumber
	 */
	protected static final String VALIBALE = "##";
	/**当前工作表*/
	protected Workbook workbook;
	
	/** 当前数据填充到的序号 */
	protected int dataRowNumber = 1;
	/** 数据的长度 */
	protected int dataSize;
	/**
	 * 函数处理
	 */
	protected FunctionHandler functionHandler=new BaseFunctionHandler();
	
	@Override
	public Workbook getWorkbook() {
		return workbook;
	}
	
	public BaseExcel(){}

	/***
	 * 设置工作表
	 * @param workbook
	 */
	public void setWorkbook(Workbook workbook) {
		this.workbook = workbook;
	}
	
	public BaseExcel(Workbook wb) {
		this.workbook=wb;
	}
	
	public BaseExcel(File file){
		this.workbook=createWorkbook(file);
	}
	
	public BaseExcel(InputStream is){
		this.workbook=createWorkbook(is);
	}
	@Override
	public Sheet[] getSheets() {
		return getSheets(workbook);
	}

	@Override
	public String[] getSheetNames() {
		return getSheetNames(workbook);
	}
	
	/***
	 * 把模板的内容写入到流中
	 * @param os 目标输出流
	 * @throws IOException
	 */
	@Override
	public void write(OutputStream os) throws IOException{
		workbook.write(os);
	}
	
	@Override
	public int getSheetCount(){
		return workbook.getNumberOfSheets();
	}

	@Override
	public InputStream toInuptStream() throws IOException {
		ByteArrayOutputStream os=new ByteArrayOutputStream();
		workbook.write(os);
		return new ByteArrayInputStream(os.toByteArray());
	}
	
	/**
	 * 数据行号
	 * @return
	 */
	public int getDataRowNumber() {
		return dataRowNumber;
	}
	
	/**
	 * 数据的行长度
	 * @return
	 */
	public int getDataSize() {
		return this.dataSize;
	}
	
}
