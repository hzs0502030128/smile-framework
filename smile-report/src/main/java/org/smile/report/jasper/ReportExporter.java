package org.smile.report.jasper;

import java.io.OutputStream;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import org.smile.io.ByteArrayOutputStream;

/**
 * 报表导出
 * @author 胡真山
 */
public class ReportExporter {
	
	protected ExporterInput input;
	
	public ReportExporter(JasperPrint print){
		this(new SimpleExporterInput(print));
	}
	
	public ReportExporter(ExporterInput input){
		this.input=input;
	}
	
	public ReportExporter(List<JasperPrint> prints){
		this(SimpleExporterInput.getInstance(prints));
	}
	
	public void exportPdf(OutputStream os) throws JRException{
		JRPdfExporter jrpdfExporter = new JRPdfExporter(); 
		jrpdfExporter.setExporterInput(input);
		jrpdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));
		jrpdfExporter.exportReport();
	}
	
	public void exportHtml(OutputStream os) throws JRException{
		JRHtmlExporter jrpdfExporter = new JRHtmlExporter(); 
		jrpdfExporter.setExporterInput(input);
		jrpdfExporter.setExporterOutput(new SimpleHtmlExporterOutput(os));
		jrpdfExporter.exportReport();
	}
	
	public void exportXls(OutputStream os) throws JRException{
		JRXlsExporter jrpdfExporter = new JRXlsExporter(); 
		jrpdfExporter.setExporterInput(input);
		jrpdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));
		jrpdfExporter.exportReport();
	}
	
	public void exportXlsx(OutputStream os) throws JRException{
		JRXlsxExporter jrpdfExporter = new JRXlsxExporter(); 
		jrpdfExporter.setExporterInput(input);
		jrpdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));
		jrpdfExporter.exportReport();
	}
	
	public byte[] exportPdf() throws JRException{
		JRPdfExporter jrpdfExporter = new JRPdfExporter(); 
		jrpdfExporter.setExporterInput(input);
		ByteArrayOutputStream os=new ByteArrayOutputStream();
		jrpdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));
		jrpdfExporter.exportReport();
		return os.toByteArray();
	}
	
}
