package org.smile.report.excel.pdf;

import org.smile.report.poi.ExcelConvertException;

import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;

public class PdfParagraph extends Paragraph{
	
	private static Font defFont() {
		String yaHeiFontName = "/fonts/simhei.ttf";
		try {
			Font yaHeiFont = new Font(BaseFont.createFont(yaHeiFontName, BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
			return yaHeiFont;
		} catch (Exception e) {
			throw new ExcelConvertException("加载默认字体失败", e);
		}
	}
	
	public PdfParagraph(String context,int fontSize){
		super(context, defFont());
		getFont().setSize(fontSize);
	}
	
	public PdfParagraph(String context){
		super(context, defFont());
	}
	
}
