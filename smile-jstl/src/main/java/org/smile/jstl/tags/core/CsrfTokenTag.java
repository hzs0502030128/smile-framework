
package org.smile.jstl.tags.core;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.smile.web.CsrfShield;

public class CsrfTokenTag extends SimpleTagSupport {

	protected String name;
	/**
	 * Specifies token name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void doTag() throws IOException {
		JspContext jspContext = this.getJspContext();
		HttpServletRequest request = (HttpServletRequest) ((PageContext) jspContext).getRequest();
		HttpSession session = request.getSession();
		String value = CsrfShield.prepareCsrfToken(session);
		if (name == null) {
			name = CsrfShield.CSRF_TOKEN_NAME;
		}
		jspContext.getOut().write("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
	}
}
