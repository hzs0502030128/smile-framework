
package org.smile.jstl.tags.core;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.smile.web.scope.ScopeUtils;

/**
 * Removes attribute from the scope.
 */
public class UnsetTag extends SimpleTagSupport {

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
