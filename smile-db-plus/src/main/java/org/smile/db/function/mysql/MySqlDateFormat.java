package org.smile.db.function.mysql;

import java.util.LinkedHashMap;
import java.util.Map;

public class MySqlDateFormat {
	private static Map<String,String> format=new LinkedHashMap<String,String>();
	private static Map<String,String> replace=new LinkedHashMap<String, String>();
	static{
		format.put("yyyy-mm-dd", "%Y-%m-%d");
		format.put("yyyy-mm-dd HH:mi:ss", "%Y-%m-%d %H:%i:%s");
		format.put("HH:mm:ss", "%H:%i:%s");
		format.put("HH", "%H");
		format.put("ss", "%s");
		format.put("mi", "%i");
		format.put("mm", "%m");
		format.put("yyyy", "%Y");
		format.put("yy", "%y");
		format.put("dd", "%d");
		format.put("yyyy-mm", "%Y-%m");
		format.put("yyyymmdd", "%Y%m%d");
		
		replace.put("yyyy", "%Y");
		replace.put("yy", "%y");
		replace.put("mm", "%m");
		replace.put("dd", "%d");
		replace.put("HH", "%H");
		replace.put("hh", "%h");
		replace.put("mi", "%i");
		replace.put("ss", "%s");
		
	}
	
	public static String getFormat(String text){
		String f=format.get(text);
		if(f==null){
			f=text;
			for(Map.Entry<String, String> entry:replace.entrySet()){
				f=f.replaceAll(entry.getKey(), entry.getValue());
			}
		}
		return f;
	}
}
