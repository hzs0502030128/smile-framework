package org.smile.report.html;

import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.smile.commons.Strings;
import org.smile.io.IOUtils;
import org.smile.report.poi.Excel;
import org.smile.report.poi.ExcelConvert;
import org.smile.report.poi.ExcelConvertException;
import org.w3c.dom.Document;

public class ToHtmlConvert implements ExcelConvert{

	/**转换使用的编码*/
	protected String encode=IOUtils.DEFAULT_ENCODE;
	/**是否关闭流*/
	protected boolean close=true;
	
	@Override
	public void convert(Excel excel, OutputStream os) {
		try{
			ExcelToHtmlConverter ethc = new ExcelToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
			ethc.setOutputColumnHeaders(false);
			ethc.setOutputRowNumbers(false);
			ethc.processWorkbook((HSSFWorkbook) excel.getWorkbook());
			Document htmlDocument = ethc.getDocument();
			DOMSource domSource = new DOMSource(htmlDocument);
			StreamResult streamResult = new StreamResult(os);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer serializer = tf.newTransformer();
			serializer.setOutputProperty(OutputKeys.ENCODING,encode);
			serializer.setOutputProperty(OutputKeys.INDENT, Strings.YES);
			serializer.setOutputProperty(OutputKeys.METHOD, Strings.HTML);
			serializer.transform(domSource, streamResult);
		}catch(Exception e){
			throw new ExcelConvertException("convert to html error ",e);
		}finally{
			if(close){
				IOUtils.close(os);
			}
		}
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public void setClose(boolean close) {
		this.close = close;
	}
	
	
}
