
package org.smile.jstl.tags.core;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.smile.jstl.tags.TagUtils;

/**
 * Switch tag provides string comparison of its value with inner {@link org.smile.jstl.tags.core.CaseTag case} values.
 */
public class SwitchTag extends SimpleTagSupport {

	static final String MSG_PARENT_SWITCH_REQUIRED = "Parent switch tag is required.";

	private String value;
	private boolean valueFounded;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void valueFounded() {
		valueFounded = true;
	}

	public boolean isValueFounded() {
		return valueFounded;
	}

	@Override
	public void doTag() throws JspException {
		TagUtils.invokeBody(getJspBody());
	}

}
