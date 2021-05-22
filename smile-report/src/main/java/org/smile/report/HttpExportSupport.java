package org.smile.report;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.smile.report.excel.XlsExportTemplate;
/**
 * 对导出动作的支持
 * @author 胡真山
 *
 */
public interface HttpExportSupport extends ExportSourceSupport{
	/**
	 * 格式化下载文件名称
	 */
	public String formatExportFileName(String filename);
	/**
	 * 把模板内容写的response中
	 * @param template
	 * @param response
	 * @param fileName
	 * @throws IOException
	 */
	public void writeToResponse(XlsExportTemplate template,HttpServletResponse response,String fileName) throws IOException;
}
