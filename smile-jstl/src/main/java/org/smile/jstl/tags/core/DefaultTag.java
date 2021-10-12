package org.smile.jstl.tags.core;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.smile.jstl.tags.TagUtils;
/**
 * switch 字标签  case 后的default
 * @author 胡真山
 * 2015年11月5日
 */
public class DefaultTag extends SimpleTagSupport {

	@Override
	public void doTag() throws JspException {
		JspTag parent = getParent();
		if (parent == null || !(parent instanceof SwitchTag)) {
			throw new JspException(SwitchTag.MSG_PARENT_SWITCH_REQUIRED, null);
		}

		SwitchTag switchTag = (SwitchTag) parent;
		if (switchTag.isValueFounded() == false) {
			TagUtils.invokeBody(getJspBody());
		}
	}
}