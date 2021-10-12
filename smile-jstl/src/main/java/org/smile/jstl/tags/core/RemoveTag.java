
package org.smile.jstl.tags.core;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.smile.web.scope.ScopeUtils;

/**
 * remove some value to scope. Default scope is 'page'.
 */
public class RemoveTag extends SimpleTagSupport {

	protected String name;

	public void setName(String name) {
		this.name = name;
	}

	protected String scope;

	public void setScope(String scope) {
		this.scope = scope;
	}

	@Override
	public void doTag() throws JspException {
		PageContext pageContext = (PageContext) getJspContext();
		ScopeUtils.removeScopeAttribute(name,scope, pageContext);
	}

}
