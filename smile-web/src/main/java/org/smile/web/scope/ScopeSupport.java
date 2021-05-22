package org.smile.web.scope;

import java.util.Enumeration;

public interface ScopeSupport {
	/**获取属性值*/
	public Object getAttribute(String key);
	/**设置属性值*/
	public void setAttribute(String key, Object value);
	/**移除属性*/
	public void removeAttribute(String key);
	/**一个属性名称的列举*/
	public Enumeration<String> getAttributes();
}
