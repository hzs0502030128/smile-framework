package org.smile.ioc;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.smile.commons.Strings;
import org.smile.io.FileNameUtils;
import org.smile.ioc.load.IocContextLoader;
import org.smile.log.LoggerHandler;
import org.smile.util.StringUtils;

public class IocContextLoaderListener implements ServletContextListener, LoggerHandler {

	public static final String CONTEXT_FILE = "iocContextConfigFile";
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

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		WebXmlIocContext.getInstance().distory();
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		String configPath = context.getInitParameter(CONTEXT_FILE);
		String fileName=getConfigPath(context, configPath);
		String configRoot = FileNameUtils.getFullPath(fileName);
		WebXmlIocContext iocContext = WebXmlIocContext.getInstance();
		new IocContextLoader().loadFileConfig(iocContext, configRoot, fileName);
		iocContext.afterLoad();
		logger.info("load ioc config file " + fileName + " success ");
	}

}
