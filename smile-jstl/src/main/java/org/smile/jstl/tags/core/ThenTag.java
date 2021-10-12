
package org.smile.jstl.tags.core;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Then nested tag for {@link org.smile.jstl.tags.core.WhenTag}.
 */
public class ThenTag extends TagSupport {

	@Override
	public int doStartTag() throws JspException {
		JspTag parent = getParent();
		if (parent == null || !(parent instanceof WhenTag)) {
			throw new JspException("Parent WhenTag tag is required", null);
		}
		WhenTag ifTag = (WhenTag) parent;
		if (ifTag.getTestValue()) {
			ifTag.setUsedBody(true);
			return EVAL_BODY_INCLUDE;
		}
		return SKIP_BODY;
	}
}