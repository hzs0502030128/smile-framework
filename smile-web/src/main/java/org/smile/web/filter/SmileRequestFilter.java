package org.smile.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.smile.Smile;
import org.smile.json.JSON;
import org.smile.log.LoggerHandler;
/**
 * request 编码设置filter
 * @author strive
 *
 */
public class SmileRequestFilter implements Filter,LoggerHandler {

	private String encode=Smile.ENCODE;
	
	public void destroy() {
		
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding(encode);
		response.setCharacterEncoding(encode);
		if(logger.isDebugEnabled()){
			logger.debug(((HttpServletRequest)request).getRequestURI());
			logger.debug(JSON.toJSONString(((HttpServletRequest)request).getParameterMap()));
		}
		chain.doFilter(request, response);
	}
	/**
	 * 可以web中配置
	 */
	public void init(FilterConfig config) throws ServletException {
		String encode=config.getInitParameter("encode");
		if(encode!=null){
			this.encode=encode;
		}
	}

}
