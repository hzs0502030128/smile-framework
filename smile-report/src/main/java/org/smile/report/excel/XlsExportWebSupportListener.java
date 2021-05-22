package org.smile.report.excel;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.smile.http.WebConfigUtils;
import org.smile.util.StringUtils;

public class XlsExportWebSupportListener implements ServletContextListener{
	/**
	 * 可以在web.xml中配置初始化参数,来指定模板的目录
	 */
	protected String FILE_PARAM="xlsTemplateFileDir";
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		String dir=event.getServletContext().getInitParameter(FILE_PARAM);
		if(StringUtils.isEmpty(dir)){
			dir=XlsExportTemplate.templateDir;
		}
		XlsExportTemplate.templateDir=WebConfigUtils.getConfigPath(event.getServletContext(), dir);
	}

	@Override
	public void contextDestroyed(ServletContextEvent envent) {
		
	}
	
}
