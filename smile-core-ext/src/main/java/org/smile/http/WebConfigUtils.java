package org.smile.http;

import javax.servlet.ServletContext;

import org.smile.commons.Strings;
import org.smile.util.StringUtils;


public class WebConfigUtils {
	/**配置classes目录*/
	private final static String CLASSPATH = "classpath:";
	/***/
	private final static String CLASSES = "/WEB-INF/classes/";
	/**
	 * 获取web路径配置的路径
	 * @param servletContext
	 * @param configPath
	 * @return
	 */
	public static String getConfigPath(ServletContext servletContext,String configPath){
		String root = servletContext.getRealPath("/");
		String fileName = Strings.BLANK;
		String path=StringUtils.trimToBlank(configPath);
		if (path.startsWith(CLASSPATH)) {
			path =StringUtils.remove(path,CLASSPATH);
			fileName = root + CLASSES + path;
		} else {
			fileName = root + path;
		}
		return fileName;
	}
}
