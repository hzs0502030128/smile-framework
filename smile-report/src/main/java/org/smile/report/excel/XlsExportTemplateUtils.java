package org.smile.report.excel;

import java.util.Map;

public class XlsExportTemplateUtils {
	/**
	 * 一次性填充多页数据
	 * @param template
	 * @param contexts
	 */
	public static void fillDataSource(XlsExportTemplate template,Map<String,Object>[] contexts){
		int index=0;
		int sheetIndex=1;
		for(Map<String,Object> context :contexts){
			if(index>1){
				template.copySheet(0, template.getSheetName(0)+"-"+index);
				template.setSheetIndex(sheetIndex++);
				template.fillDataSource(context);
			}
			index++;
		}
		//填充第一个sheet
		template.setSheetIndex(0);
		template.fillDataSource(contexts[0]);
	}
	/**
	 * 一次填充多页数据
	 * @param template
	 * @param contexts
	 * @param nameHandler
	 */
	public static void fillDataSource(XlsExportTemplate template,Map<String,Object>[] contexts,SheetNameHandler nameHandler){
		int index=0;
		int sheetIndex=1;
		for(Map<String,Object> context :contexts){
			if(index>1){
				template.copySheet(0, nameHandler);
				template.setSheetIndex(sheetIndex++);
				template.fillDataSource(context);
			}
			index++;
		}
		//填充第一个sheet
		template.setSheetIndex(0);
		template.fillDataSource(contexts[0]);
	}
}
