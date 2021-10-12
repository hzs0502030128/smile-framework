
package org.smile.jstl.tags.core;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @author 胡真山
 *
 */
public class UrlTagSupport extends BodyTagSupport{

	protected UrlTag urlTag;
	
	@Override
	public int doStartTag() throws JspException {
		urlTag=(UrlTag)pageContext.getAttribute(UrlTag.URL_TAG_CURRENT);
		return EVAL_BODY_INCLUDE;
	}
}