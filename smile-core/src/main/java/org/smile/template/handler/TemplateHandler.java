package org.smile.template.handler;

import java.io.Writer;

import org.smile.template.IncludesContext;

public interface TemplateHandler {
	/**
	 * 解析模板
	 * @param params
	 * @param out
	 */
	public void parse(Object params, Writer out) ;
	/**
	 * 处理到一个字符串中
	 * @param param
	 * @return
	 */
	public String processToString(Object param);
	/**
	 * 设置包含上下容器
	 * @param context
	 */
	public void setIncludeContext(IncludesContext context);
}
