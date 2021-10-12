package org.smile.jstl.tags.core;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.smile.web.ServletUtils;

/**
 * Outputs debug data.
 */
public class DebugTag extends SimpleTagSupport {

	@Override
	public void doTag() throws JspException, IOException {
		PageContext pageContext = (PageContext) getJspContext();
		JspWriter out = getJspContext().getOut( );
		out.println(ServletUtils.debug(pageContext));
	}
}
