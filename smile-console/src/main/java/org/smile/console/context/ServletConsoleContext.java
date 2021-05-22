package org.smile.console.context;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smile.beans.BeanInfo;
import org.smile.commons.SmileRunException;
import org.smile.console.command.ConsoleCommand;
import org.smile.http.HttpConstans;
import org.smile.template.HashTemplateModel;

/**
 * 一个servlet中使用的控制台上下文信息
 * 
 * @author 胡真山
 *
 */
public class ServletConsoleContext extends HashTemplateModel implements ConsoleContext {
	/** 当前请求 */
	private HttpServletRequest req;
	/** 响应 */
	private HttpServletResponse resp;
	/** 用来封装设置的属性信息 */
	private Map<String, Object> attribute = new HashMap<String, Object>();
	/** 所有的命令集合 */
	private Map<String, ConsoleCommand> commandMap;
	/** servlet 配置的路径 */
	private String uri;
	/** 用来获取对象的bean属性 */
	private BeanInfo beanInfo =BeanInfo.getInstance(getClass());

	public ServletConsoleContext(HttpServletRequest req, HttpServletResponse resp) {
		this.req = req;
		this.resp = resp;
		String uri = req.getRequestURI();
		this.uri = uri.substring(uri.lastIndexOf("/") + 1);
	}

	@Override
	public Map<String, Object> getParameterMap() {
		return (Map) req.getParameterMap();
	}

	@Override
	public String getCommandCode() {
		return req.getParameter("commandCode");
	}

	@Override
	public void writeReponse(String html) {
		resp.setContentType(HttpConstans.HTML_UTF_CONTENT_TYPE);
		try {
			resp.getWriter().print(html);
		} catch (IOException e) {
			throw new SmileRunException("response write error", e);
		}
	}

	public HttpServletRequest getReq() {
		return req;
	}

	public HttpServletResponse getResp() {
		return resp;
	}

	public String getUri() {
		return uri;
	}

	public Map<String, Object> getAttribute() {
		return attribute;
	}

	@Override
	public void setAttribute(String name, Object value) {
		attribute.put(name, value);
	}

	public Map<String, ConsoleCommand> getCommandMap() {
		return commandMap;
	}

	/**
	 * 设置命令的集合
	 * 
	 * @param commandMap
	 */
	public void setCommandMap(Map<String, ConsoleCommand> commandMap) {
		this.commandMap = commandMap;
	}

	@Override
	public String getParameter(String name) {
		return req.getParameter(name);
	}

	/**
	 * 当前命令的名称
	 * 
	 * @return
	 */
	public String getCurrentCommandName() {
		return commandMap.get(getCommandCode()).getName();
	}

	@Override
	public Object getAttribute(String name) {
		return attribute.get(name);
	}

	@Override
	protected Object getModelValue(String key) {
		Object value=attribute.get(key);//先中map设置 的map中获取
		if(value==null){
			PropertyDescriptor pd=beanInfo.getPropertyDescriptor(key);
			if(pd!=null&&pd.getReadMethod()!=null){
				try {
					value= pd.getReadMethod().invoke(this);
				} catch (Exception e) {
					throw new SmileRunException(e);
				}
			}else{
				value=req.getParameter(key);
			}
		}
		return value;
	}
}
