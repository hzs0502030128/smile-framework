package org.smile.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smile.commons.StringBand;
import org.smile.file.ContentType;
import org.smile.io.FileNameUtils;
import org.smile.io.IOUtils;
import org.smile.log.LoggerHandler;
import org.smile.util.StringUtils;

public class FileDownloadServlet extends HttpServlet implements LoggerHandler{

	private static final int BUFFER_SIZE=10*1024;
	/**配置的文件路径URL标识*/
	protected String urlFlag="/fileList/";
	/**下载文件的根目录*/
	protected String fileRoot;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String uri=req.getRequestURI();
		int index=uri.indexOf(urlFlag);
		if(index<0){
			response404(resp);
		}else{
			StringBand fileName=new StringBand();
			String ext=FileNameUtils.getExtension(uri);
			resp.setContentType(ContentType.getContextType(ext));
			fileName.append(fileRoot);
			fileName.append(uri.substring(index+urlFlag.length()));
			File file=new File(fileName.toString());
			if(file.exists()){
				IOUtils.copy(new FileInputStream(fileName.toString()), resp.getOutputStream(),BUFFER_SIZE);
			}else{
				response404(resp);
			}
		}
	}
	/**返回404页面*/
	private void response404(HttpServletResponse resp) throws IOException{
		resp.setContentType(ContentType.getContextType("html"));
		resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
		resp.getWriter().print("<h1>404 Resource not found</h1>");
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		String urlFlagConfig=config.getInitParameter("urlFlag");
		if(StringUtils.notEmpty(urlFlagConfig)){
			urlFlag=urlFlagConfig;
		}
		String fileRootConfig=config.getInitParameter("fileRootConfig");
		if(StringUtils.isEmpty(fileRootConfig)){
			fileRoot=config.getInitParameter("fileRoot");
			if(StringUtils.isEmpty(fileRoot)){
				throw new ServletException("must config fileRootConfig or fileRoot");
			}
		}else{
			fileRoot=System.getProperty(fileRootConfig);
			if(StringUtils.isEmpty(fileRoot)){
				throw new ServletException("can't find a property in system properties named "+fileRootConfig);
			}
		}
	}
	
}
