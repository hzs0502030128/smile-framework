package org.smile.jstl.tags.core;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.TagSupport;

import org.smile.beans.converter.ConvertException;
import org.smile.beans.converter.type.BooleanConverter;
import org.smile.jstl.tags.TagUtils;
import org.smile.log.LoggerHandler;

public class IfTag extends TagSupport implements LoggerHandler {

	protected Boolean test;

	public void setTest(String test) {
		try {
			Boolean value = BooleanConverter.getInstance().convert(test);
			if (value != null) {
				this.test = value;
			} else {
				this.test = false;
			}
		} catch (ConvertException e) {
			throw new RuntimeException("转换test出错:" + e.getMessage());
		}
	}

	
	
	@Override
	public int doStartTag() throws JspException {
		if (test) {
			return EVAL_BODY_INCLUDE;
		}
		return SKIP_BODY;
	}

	@Override
	public int doEndTag() throws JspException {
		JspTag parent = getParent();
		TagUtils.setIfElseSuccess(parent, test, pageContext);
		return EVAL_PAGE;
	}

	public void setTest(boolean test) {
		this.test = test;
	}

}
