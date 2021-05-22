package org.smile.http;

import java.io.File;
import java.io.FileInputStream;

import org.smile.commons.SmileRunException;
import org.smile.util.Properties;
import org.smile.util.SysUtils;

/**
 * tomcat 适用的工具类
 * @author 胡真山
 */
public class TomcatUtils{
	
	private static String default_tomcat_config_dir="/config/";
	
	private static String default_tomcat_config_file="server.properties";
	
	public static Properties loadProperties(){
		String tomcatDir=SysUtils.getTomcatHome();
		Properties p=new Properties();
		try {
			p.load(new FileInputStream(new File(tomcatDir+default_tomcat_config_dir+default_tomcat_config_file)));
		} catch (Exception e) {
			throw new SmileRunException(e);
		}
		return p;
	}
}
