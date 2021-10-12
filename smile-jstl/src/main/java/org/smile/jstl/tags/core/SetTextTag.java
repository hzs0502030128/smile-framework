
package org.smile.jstl.tags.core;

import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.smile.resource.DefaultResource;
import org.smile.web.scope.ScopeUtils;

/**
 * 设置国际化文本
 */
public class SetTextTag extends SimpleTagSupport {

	protected String name;
	
	protected String key;
	
	protected String scope;

	public void setName(String name) {
		this.name = name;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public void doTag() throws JspException {
		PageContext pageContext = (PageContext) getJspContext();
		Locale local=pageContext.getRequest().getLocale();
		String value=DefaultResource.getInstance().getString(local, key);
		ScopeUtils.setScopeAttribute(name, value, scope, pageContext);
	}

}
