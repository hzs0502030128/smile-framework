package org.smile.jstl.tags.core;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.smile.jstl.tags.TagUtils;

public class CaseTag extends SimpleTagSupport {

	private String value;
	
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public void doTag() throws JspException {
		JspTag parent = getParent();
		if (parent == null || !(parent instanceof SwitchTag)) {
			throw new JspException(SwitchTag.MSG_PARENT_SWITCH_REQUIRED, null);
		}

		SwitchTag switchTag = (SwitchTag) parent;
		if ((switchTag.getValue() != null) && switchTag.getValue().equals(value)) {
			switchTag.valueFounded();
			TagUtils.invokeBody(getJspBody());
		}
	}
}
