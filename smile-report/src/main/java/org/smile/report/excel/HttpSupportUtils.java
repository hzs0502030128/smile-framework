package org.smile.report.excel;

import java.io.BufferedOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.smile.io.IOUtils;

public class HttpSupportUtils {
	/***
	 * 写入到response中 
	 * @param response
	 * @param template
	 * @throws IOException
	 */
	public static void write(HttpServletResponse response,XlsExportTemplate template) throws IOException{
		BufferedOutputStream os =null;
		try{
			os= new BufferedOutputStream(response.getOutputStream());
			template.write(os);
			os.flush();
		}finally{
			IOUtils.close(os);
		}
	}
}
