package org.smile.console.context;

import java.util.Map;

public interface ConsoleContext {
	/***
	 * 获取参数map
	 * @return
	 */
	public Map<String,Object> getParameterMap();
	/**
	 * 获取请求参数的值
	 * @param name
	 * @return
	 */
	public String getParameter(String name);
	/**
	 * 获取使用编号
	 * @return
	 */
	public String getCommandCode();
	/**
	 * 写入响应html
	 * @param html
	 */
	public void writeReponse(String html);
	/***
	 * 设置属性值
	 * @param name
	 * @param value
	 */
	public void setAttribute(String name,Object value);
	
	/**
	 * 获取一个属性的值
	 * @param name
	 * @return
	 */
	public Object getAttribute(String name);
	
}
