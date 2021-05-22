package org.smile.template;

import java.io.IOException;

public interface TemplateLoader<T> {
	/**
	 * 获取模板来源
	 * @param name
	 * @return
	 */
	public T getTemplateSource(String name);
	/**
	 * 模板来源最后修改时间
	 * @param templateSource
	 * @return
	 */
	public long getLastModify(T templateSource);
	/**
	 * 获取模板
	 * @param templateSource
	 * @param encode 读取模板源的编码
	 * @return
	 * @throws IOException
	 */
	public String getTemplateContent(T templateSource,String encode) throws IOException;
	
}
