package org.smile.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.smile.report.excel.MergeConfig;
import org.smile.report.excel.XlsExportTemplate;
import org.smile.report.excel.pdf.PdfBuilder;
import org.smile.report.function.AbstractFunction;
import org.smile.report.function.TemplateFunction;
import org.smile.report.poi.HeaderExcel;
import org.smile.report.poi.HeaderExcelXlsx;
import org.smile.report.poi.SheetHeader;
import org.smile.util.DateUtils;

import com.lowagie.text.PageSize;

public class TestReport {
	@Test
	public void test(){
		XlsExportTemplate template=new XlsExportTemplate();
		try {
			List list=new LinkedList();
			for(int i=0;i<2;i++){
				Map row=new HashMap();
				row.put("name", "班级"+i);
				row.put("age", "年龄"+i);
				List l=new LinkedList();
				row.put("data", l);
				for(int k=0;k<2;k++){
					Map map=new HashMap();
					map.put("a", "A"+k);
					map.put("b", "B"+k);
					map.put("d", "D"+k);
					l.add(map);
				}
				list.add(row);
			}
			
//			template.setMergeConfig(new MergeConfig(new String[]{"a"}, new String[]{"a","b"}));
//			template.addMergeConfig(new MergeConfig(new String[]{"a","c"}, new String[]{"c"}));
			Map context=new HashMap();
			
			context.put("dataSource", list);
			template.setDataNameRowIndex(5);
			template.setBottomRowIndex(6, 6);
			context.put("name", "广东迈瑞");
			context.put("date", DateUtils.parseDate("2017-09-08"));
//			CellImage image= new CellImage(new FileInputStream("D:/temp/test.jpg"),Workbook.PICTURE_TYPE_JPEG);
//			image.setWidth(3.5F);
//			image.setHeight(1f);
//			image.setLeft(10);
//			image.setTop(20);
//			context.put("image",image);
			
			template.loadXlsTemplate("d:/temp/test.xls");
			template.addParam("ttt", "胡真山");
			template.registerFunction("ff", new AbstractFunction(){
				@Override
				public Object convert(Object oneData, String exp, Object expValue) {
					return "测试";
				}
				
			});
			
			template.registerFunction("template", new TemplateFunction());
//			template.setDynamic("title1", "五月,六月,七月");
//			template.setDynamic("title2", "十月,十一月,十二月");
//			template.setDynamic("name1", "m1,m2,m3");
//			template.setDynamic("name2", "g1,g2,g3");
			long s=System.currentTimeMillis();
			template.fillDataSource(context);
			System.out.println(System.currentTimeMillis()-s);
//			JaCobWorkbook wb=new JaCobWorkbook(template.getWorkbook());
//			wb.writeToPdf(new FileOutputStream(new File("d:/temp/test44.pdf")));
			template.write(new FileOutputStream(new File("d:/temp/testrr.xls")));
//			
			PdfBuilder pdf=new PdfBuilder();
//			ExcelConvert convert=new ToPdfConvert(pdf);
//			convert.convert(new BaseExcel(template.getWorkbook()), new FileOutputStream(new File("d:/temp/test442.pdf")));
			PdfBuilder pdf2=new PdfBuilder();
			pdf2.getPageSetting().setRectangle(PageSize.A4);
//			ExcelConvert convert2=new ToPdfConvert(pdf2);
//			try {
//				convert2.convert(new BaseExcel(WorkbookFactory.create(new File("d:/temp/testrr.xls"))), new FileOutputStream(new File("d:/temp/test443.pdf")));
//			} catch (EncryptedDocumentException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (InvalidFormatException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
//	@Test
//	public void testExport() throws  Exception{
//		Workbook wb=WorkbookFactory.create(new File("d:/temp/test.xls"));
//		Row row=wb.getSheetAt(0).getRow(0);
//		Template.shiftCells(row, 1, 3, 1);
//		Cell c=row.createCell(1);
//		c.setCellValue("B");
//		wb.write(new FileOutputStream(new File("d:/temp/test2.xls")));
//	}
	
//	@Test
//	public void testPdf() throws FileNotFoundException{
//		PdfBuilder pdf=new PdfBuilder();
//		ExcelConvert convert=new ToPdfConvert(pdf);
//		convert.convert(new BaseExcel(new File("d:/temp/test5.xlsx")),new FileOutputStream(new File("d:/temp/test4.pdf")));
//	}
	
	@Test
	public void testXlsTitle2() throws Exception{
		String[][] names= new String[][]{{"ID","信息","信息","信息"},{"id","姓名","年龄","备注"}};
		SheetHeader header=new SheetHeader(new String[]{"id","fn{name}","ognl{age}","${'我是'+name+'今年'+age+'岁'+(100*age)}"},names);
		header.setColumnWidths(new Integer[]{10,20,10,50});
		List<Map> list=new ArrayList(100);
		for(int i=0;i<100;i++){
			Map m=new HashMap();
			m.put("id", i);
			m.put("name", "胡"+i);
			m.put("age", 100+i);
			list.add(m);
		}
		HeaderExcel excel=new HeaderExcelXlsx(header);
		header.setMergeSet(new String[]{"name"}, new String[]{"id"});
		excel.fillDataList(0, list);
		excel.write(new FileOutputStream(new  File("d:/temp/teste.xlsx")));
	}
	
//	@Test
//	public void test3() throws Exception{
//		Workbook b=WorkbookFactory.create(new File("d:/hkStock.xls"));
//		Sheet sheet=b.getSheetAt(0);
//		int s=sheet.getLastRowNum();
//		System.out.println(s);
//	}
//	@Test
//	public void testF() throws EncryptedDocumentException, InvalidFormatException, IOException{
//		Workbook wk=WorkbookFactory.create(new File("d:/temp/FORMAT.xlsx"));
//		Row row=wk.getSheetAt(0).getRow(0);
//		for(int i=0;i<10;i++){
//			System.out.println(PoiSupport.getCellValue(row.getCell(i)));
//			System.out.println(row.getCell(i).getCellStyle().getDataFormatString());
//			System.out.println(row.getCell(i).getCellStyle().getDataFormat());
//			System.out.println("*"+PoiSupport.getCellFormatedValue(row.getCell(i)));
//		}
//	}
//	@Test
//	public void testDynamicReport(){
//		XlsExportDynamicTemplate template=new XlsExportDynamicTemplate();
//		try {
//			template.loadXlsTemplate("d:/temp/dynamic.xls");
//			template.setDynamic("names", new String[]{"二月","三月","四月"});
//			template.setDynamic("ms", new String[]{"m2","m3","m4"});
//			template.setDynamic("names2", new String[]{"五月","六月","七月"});
//			template.setDynamic("ms2", new String[]{"m5","m6","m7"});
//			List list=new LinkedList();
//			for(int i=0;i<10;i++){
//				Map map=new HashMap();
//				map.put("m1", 10);
//				map.put("m2", 20);
//				map.put("m3", 30);
//				map.put("m4", 40);
//				map.put("m5", 50);
//				map.put("m6", 60);
//				map.put("m7", 70);
//				map.put("total", 8000);
//				list.add(map);
//			}
//			template.fillDataSource(list);
//			template.write(new FileOutputStream(new File("d:/temp/dynameic_out.xls")));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}
