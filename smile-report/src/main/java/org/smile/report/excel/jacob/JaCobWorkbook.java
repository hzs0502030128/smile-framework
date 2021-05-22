package org.smile.report.excel.jacob;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.smile.commons.SmileRunException;
import org.smile.io.IOUtils;
import org.smile.util.UUIDGenerator;

import com.jacob.com.JacobException;

public class JaCobWorkbook extends JaCobSupport{
	
	protected static final  String  tempext=".temp";
	
	/***
	 * 把excel转成pdf并写入到一个流中
	 * @param workBook
	 * @param os
	 */
	public synchronized void  writeToPdf(Workbook workBook,OutputStream os){
		try {
			File tempFile=File.createTempFile(UUIDGenerator.shortId(),tempext);
			logger.debug(tempFile);
			
			OutputStream tempOs=new FileOutputStream(tempFile);
			try{
				workBook.write(tempOs);
			}finally{
				IOUtils.close(tempOs);
			}
			String tempDir=tempFile.getParent();
			String pdfFile=tempDir+File.separator+UUIDGenerator.uuid()+".pdf";
			logger.debug(pdfFile);
			//转换
			converExcelToPdf(tempFile.getAbsolutePath(), pdfFile);
			//生成的pdf文件
			File pdf=new File(pdfFile);
			//把pdf文件复制到输出流中
			IOUtils.copy(new FileInputStream(pdf), os);
			tempFile.delete();
			pdf.delete();
		}catch(JacobException e){
			throw e;
		} catch (Exception e) {
			throw new SmileRunException("转换至pdf出错",e);
		}
	}
	/***
	 * 把excel转成pdf并写入到一个流中
	 * @param workBook
	 * @param os
	 */
	public synchronized void  writeToPdf(Workbook workBook,String pdfFile){
		try {
			File tempFile=File.createTempFile(UUIDGenerator.shortId(),tempext);
			logger.debug(tempFile);
			OutputStream tempOs=new FileOutputStream(tempFile);
			try{
				workBook.write(tempOs);
			}finally{
				IOUtils.close(tempOs);
			}
			logger.debug(pdfFile);
			//转换
			converExcelToPdf(tempFile.getAbsolutePath(), pdfFile);
		}catch(JacobException e){
			throw e;
		} catch (Exception e) {
			throw new SmileRunException("转换至pdf出错",e);
		}
	}
}	
