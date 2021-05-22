package org.smile.report.pdf;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.smile.commons.SmileRunException;
import org.smile.io.ByteArrayOutputStream;
import org.smile.reflect.FieldUtils;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
/**
 * pdf合并
 * @author 胡真山
 *
 */
public class PdfMerge extends PdfReaderSupport{
	
	private Collection<byte[]> byteColl = new ArrayList<byte[]>();
	
	protected Rectangle pageSize;
	
	public PdfMerge(Rectangle pageSize){
		this.pageSize=pageSize;
	}
	
	public PdfMerge(){}
	
	public PdfMerge(float width,float height){
		this.pageSize=new Rectangle(width, height);
	}
	
	/**
	 * 合并pdf文件 到一个输出流中
	 * @param os
	 * @throws IOException
	 */
	public void conbine(OutputStream os)throws IOException{
		Document document = new Document();
		PdfCopy pdfCopy=null;
		try{
			
			pdfCopy = new PdfCopy(document, os);
			
			document.open();
			
			pdfCopy.open();
			
			PdfReader reader = null;
			
			PdfImportedPage page = null;
			
			Iterator<byte[]> ite = byteColl.iterator();
			
			while (ite.hasNext()) {
				//读取一个pdf文件中的页
				reader = new PdfReader(ite.next());
				int totalPages=reader.getNumberOfPages();
				if (totalPages == 0) {
					continue;
				}
				setAccessAbled(reader);
				//循环页
				for (int i=1;i<=totalPages;i++) {
					document.setPageSize(reader.getPageSize(i));
					document.newPage();
					page = pdfCopy.getImportedPage(reader, i);
					//添加到集合中
					pdfCopy.addPage(page);
					onCopyPage(pdfCopy,page);
					pdfCopy.flush();
				}
			}
		}catch(DocumentException e){
			throw new IOException("合并pdf出错",e);
		}finally{
			document.close();
			if(pdfCopy!=null){
				pdfCopy.close();
			}
		}
	}
	
	protected void onCopyPage(PdfCopy pdfCopy,PdfImportedPage page){
		
	}
	
	/**
	 * 合并成一个byte[] 
	 * @return pdf的内容
	 * @throws IOException
	 * @throws DocumentException
	 */
	public byte[] conbine() throws IOException{
		ByteArrayOutputStream os=new ByteArrayOutputStream();
		conbine(os);
		return os.toByteArray();
	}
	/**
	 * 添加一个页面
	 * @param byteArray
	 */
	public void add(byte[] byteArray) {
		byteColl.add(byteArray);
	}
	/**
	 * 设置页面大小
	 * @param pageSize
	 */
	public void setPageSize(Rectangle pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 清空
	 */
	public void clear() {
		byteColl.clear();
	}
	
}
