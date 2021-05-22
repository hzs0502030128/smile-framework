package org.smile.web.filter;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smile.commons.Strings;
import org.smile.io.IOUtils;
import org.smile.log.LoggerHandler;
import org.smile.template.SimpleStringTemplate;
import org.smile.template.Template;
import org.smile.util.StringUtils;
import org.smile.util.XmlUtils;
import org.smile.web.HttpHeader;
import org.smile.web.author.AuthorConfigInfo;
import org.smile.web.author.AuthorInfo;
import org.smile.web.author.AuthorityConfig;
import org.smile.web.author.AuthorityContext;

/**
 * http basice author check
 * @author strive
 * 默认文件文件：配置在classpath 下  
 * authorization.xml  
 * 也可以在web.xml 配置filter的时候指定 authorization
 * 例子
 *<user username="hzs" password="123456" role="admin"/>
 *	<user username="hzs1" password="123456" role="guest,admin"/>
 *	
 *	<role name="admin">/*</role>
 *	<role name="guest">
 *      /ww/*
 *		/ws/*
 *	</role>
 */
public class HttpAuthorizationFilter implements Filter,LoggerHandler {
	/**
	 * 用户验证配置文件
	 */
	private String filename="authorization.xml";
	
	private static final String configFileNameKey="authorization";
	
	private AuthorityContext context;
	
	public void destroy() {
		logger.info(getClass()+" destroy");
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req=(HttpServletRequest)request;
		String uri=req.getRequestURI();
		String contextPath=req.getContextPath();
		String checkUri=uri;
		if(!Strings.BLANK.equals(contextPath)){
			checkUri= uri.substring(uri.lastIndexOf(contextPath)+contextPath.length());
		}
		//排除
		if(context.isExcludes(checkUri)){
			chain.doFilter(request, response);
		}else{
			//进行身份验证
			HttpServletResponse resp=(HttpServletResponse)response;
			HttpHeader header=new HttpHeader(req);
			AuthorInfo info=header.getBasicAuthorInfo();
			logger.debug(info);
			if(info==null){
				header.responseUnAuthorized(resp);
			}else{
				AuthorConfigInfo configInfo=context.getAuthorConfigInfo(info.getUsername());
				if(configInfo==null||!configInfo.checkPassword(info.getPassword())){
					header.responseUnAuthorized(resp);
				}else{
					if(configInfo.testUrl(checkUri)){
						chain.doFilter(request, response);
					}else{
						header.responseUnAuthorized(resp);
					}
				}
			}
		}
	}
	/**
	 * 可以web中配置
	 */
	public void init(FilterConfig config) throws ServletException {
		String filename=config.getInitParameter(configFileNameKey);
		if(StringUtils.notEmpty(filename)){
			this.filename=filename;
		}
		InputStream is=HttpAuthorizationFilter.class.getClassLoader().getResourceAsStream(this.filename);
		if(is!=null){
			String configXml;
			try {
				configXml = IOUtils.readString(is);
			} catch (IOException e) {
				throw new ServletException("read config file "+filename,e);
			}
			Template template=new SimpleStringTemplate(configXml);
			AuthorityConfig authorConfig=XmlUtils.parserXml(AuthorityConfig.class, template.processToString(System.getProperties()));
			context=new AuthorityContext(authorConfig);
		}
	}

}
