package org.smile.util;

public class URLUtils {
	
	public static final String URL_PROTOCOL_FILE="file";
	
	public static final String URL_PROTOCOL_JAR="jar";
	
	public static String getURI(String url){
		int index=url.indexOf('?');
		if(index>=0){
			return url.substring(0,index);
		}
		return url;
	}
}
