package org.smile.report.excel.jacob;

import org.smile.log.LoggerHandler;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.JacobException;
import com.jacob.com.Variant;

public class JaCobSupport implements LoggerHandler{
	
	protected ActiveXComponent app = new ActiveXComponent("Excel.Application");
	
	public JaCobSupport(){
		ComThread.InitMTA();
	}
	/**
	 * 转换xls到pdf
	 * @param excelFile
	 * @param pdfFile
	 */
	public void converExcelToPdf(String excelFile,String pdfFile) throws JacobException{
		try {
			//设置应用程序不可见 不显示界面
			app.setProperty("Visible", false);
			Dispatch workbooks = app.getProperty("Workbooks").toDispatch();
			//打开文件
			Dispatch workbook = Dispatch.invoke(workbooks, "Open", Dispatch.Method, new Object[] { excelFile, new Variant(false), new Variant(false) }, new int[3]).toDispatch();
			//另存为文件
			Dispatch.invoke(workbook, "ExportAsFixedFormat", Dispatch.Method, new Object[] {0,pdfFile}, new int[1]);
			//关闭
			Dispatch.call(workbook, "Close", new Variant(false));
		} catch (Exception e) {
			throw new JacobException("excel 转 pdf出错 excel:"+excelFile+",pdf:"+pdfFile+e.getMessage());
		} 
	}
	/***
	 * 关闭应用程序
	 */
	public void closeApp(){
		app.invoke("Quit", new Variant[] {});
		//释放资源   不释放excel进行不会关闭
		ComThread.Release();
	}
}
