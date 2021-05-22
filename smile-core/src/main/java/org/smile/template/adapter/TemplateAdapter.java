package org.smile.template.adapter;

import org.smile.template.handler.TemplateHandler;

public interface TemplateAdapter {
	/**
	 * 生成一个模板处理者实例
	 * @param templateText 要处理的模板内容
	 * @return
	 */
	public TemplateHandler newInstance(String templateText);
}
