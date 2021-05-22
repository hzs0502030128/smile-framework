package org.smile.orm;

import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.smile.io.IOUtils;
import org.smile.orm.load.ApplicationLoader;
import org.smile.orm.load.DefApplicationConfig;
import org.smile.util.StringUtils;

/**
 * 可以通过此listener来配置对ormapplication xml文件进行加载 需要配置一个 文件路径 ， 例： <context-param>
 * <param-name>applicationConfigFile</param-name>
 * <param-value>classpath:orm.xml</param-value> </context-param>
 * 
 * @author 胡真山 2015年11月4日
 */
public class OrmContextLoaderListener implements ServletContextListener {
	/**
	 * 配置Orm配置文件的名称 
	 */
	public static final String CONTEXT_FILE = "applicationConfigFile";
	
	private final String classpath = "classpath:";

	private final String classes = "/WEB-INF/classes/";
	/**默认配置文件*/
	private static final String defaultConfigFile="classpath:orm-application.xml";

	@Override
	public void contextDestroyed(ServletContextEvent event) {

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		String path = context.getInitParameter(CONTEXT_FILE);
		if(StringUtils.isEmpty(path)){
			path=defaultConfigFile;
		}
		String root = context.getRealPath("/");
		InputStream is = null;
		try {
			if (path.startsWith(classpath)) {
				path = path.replace(classpath, "");
				is = new FileInputStream(root + classes + path);
			} else {
				is = new FileInputStream(root + path);
			}
			ApplicationLoader.loadXml(is,DefApplicationConfig.getInstance());
		} catch (Exception e) {
			throw new RuntimeException("加载文件" + path + "出错", e);
		} finally {
			IOUtils.close(is);
		}
	}

}
