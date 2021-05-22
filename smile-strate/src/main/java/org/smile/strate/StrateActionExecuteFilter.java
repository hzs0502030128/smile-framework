package org.smile.strate;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smile.beans.converter.BeanException;
import org.smile.http.HttpMethod;
import org.smile.log.LoggerHandler;
import org.smile.reflect.ClassTypeUtils;
import org.smile.strate.action.ActionContext;
import org.smile.strate.action.StrateInitException;
import org.smile.strate.dispatch.ActionURIParser;
import org.smile.strate.dispatch.StrateDispatcher;
import org.smile.util.StringUtils;
/**
 * 使用filter方法实现strate 策略跳转 
 * 
 * <filter>
		<filter-name>strate</filter-name>
		<filter-class>org.smile.strate.StrateActionExecuteFilter</filter-class>
		<init-param>
			<param-name>beanHandler</param-name>
			<!--可使用spring支持的handler-->
			<param-value>org.smile.strate.handler.IocBeanHandler</param-value>
		</init-param>
	</filter>
	
	<filter-mapping>
		<filter-name>strate</filter-name>
		<url-pattern>*.do</url-pattern>
	</filter-mapping>
 * 
 * 另有servlet 方法实现  {@link ActionServlet}
 * @author 胡真山
 * @Date 2016年1月18日
 */
public class StrateActionExecuteFilter implements Filter,LoggerHandler{
	
	private StrateDispatcher dispatcher=new StrateDispatcher();
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request=(HttpServletRequest)req;
		HttpServletResponse response= (HttpServletResponse)resp;
		String uri=this.dispatcher.properedURI(request, response);
		if(dispatcher.isActionURI(request.getContextPath(),uri)){
			this.dispatcher.execute(request, response,uri, HttpMethod.valueOf(request.getMethod()));
		}else{
			chain.doFilter(request, resp);
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		StrateContext.getInstance().start(config.getServletContext());
		String urlParserId=config.getInitParameter("url-parser");
		this.dispatcher.initActionUrlParser(urlParserId);
	}

	@Override
	public void destroy() {
		
	}

}
