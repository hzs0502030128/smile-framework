package org.smile.file;

import java.io.IOException;

import org.smile.commons.MimeTypes;
import org.smile.log.LoggerHandler;
import org.smile.util.Properties;
/**
 * 	常用的contentType合集
 * @author 胡真山
 *
 */
public class ContentType implements LoggerHandler{
	
	private static Properties p=new Properties();
	
	static{
		try {
			p.loadClassPathFile("ContentType.properties");
		} catch (IOException e) {
			logger.error(e);
		}
	}
	/**
	 * 以扩展名获取contextType
	 * @param ext 不带.的扩展名
	 * @return
	 */
	public static String getContextType(String ext){
		String contentType=p.getProperty(ext);
		if(p==null){
			return MimeTypes.getMimeType(ext);
		}
		return contentType;
	}
}
