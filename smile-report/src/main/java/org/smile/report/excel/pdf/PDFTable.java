package org.smile.report.excel.pdf;

import java.util.LinkedList;
import java.util.List;

import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPTable;


public class PDFTable {
	
	protected PdfPTable table;
	
	protected List<Image> images;
	
	public PDFTable(PdfPTable table){
		this.table=table;
	}
	
	public void add(Image image){
		if(images==null){
			images=new LinkedList<Image>();
		}
		images.add(image);
	}
}
