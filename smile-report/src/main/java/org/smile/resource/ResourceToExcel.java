package org.smile.resource;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.smile.collection.ArrayUtils;
import org.smile.report.poi.HeaderExcelXls;
import org.smile.report.poi.SheetHeader;
import org.smile.util.LocaleUtils;


public class ResourceToExcel {
	/**
	 * 需要导出的方言
	 */
	private String[] locals;
	/**
	 * 显示标题
	 */
	private String[] titles;
	
	private String keyName="key";
	
	private String keyLabel="键";
	
	private MessageResource resource;
	/**
	 * 转出到excel文件中
	 * @param filePath
	 * @throws IOException
	 */
	public void exportToExcel(String filePath) throws IOException{
		Map<String, Map<String, String>> mapS = new LinkedHashMap<String, Map<String, String>>();
		for (String l : locals) {
			ResourceBundle bundle= resource.getResourceBundle(LocaleUtils.getLocale(l));
			for (String s : bundle.keySet()) {
				Map<String, String> map = mapS.get(s);
				if (map == null) {
					map = new HashMap<String, String>();
					map.put(keyName, s);
					mapS.put(s, map);
				}
				map.put(l, bundle.getString(s));
			}
		}
		export(mapS.values(),filePath);
	}

	private void export(Collection<Map<String, String>> values, String filePath) throws IOException {
		String[][] headTitle=new String[1][locals.length+1];
		if(titles!=null){
			headTitle[0]=ArrayUtils.append(new String[]{keyLabel}, titles);
		}else{
			headTitle[0]=ArrayUtils.append(new String[]{keyLabel}, locals);
		}
		String[] cols=ArrayUtils.append(new String[]{keyName}, locals);
		HeaderExcelXls excel=new HeaderExcelXls(new SheetHeader(cols,headTitle));
		excel.fillDataList(values);
		excel.write(new FileOutputStream(filePath));
	}

	public void setLocals(String[] locals) {
		this.locals = locals;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public void setKeyLabel(String keyLabel) {
		this.keyLabel = keyLabel;
	}

	public void setResource(MessageResource resource) {
		this.resource = resource;
	}

	public void setTitles(String[] titles) {
		this.titles = titles;
	}
	
	
}
