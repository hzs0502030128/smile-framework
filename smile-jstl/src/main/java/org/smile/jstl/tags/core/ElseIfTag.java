package org.smile.jstl.tags.core;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspTag;

import org.smile.jstl.tags.TagUtils;

public class ElseIfTag extends IfTag{
	/**是否是已经出现TRUE*/
	protected boolean ifResult;
    @Override
    public int doStartTag() throws JspException {
    	JspTag parent = getParent();
        boolean result=TagUtils.getIfElseSuccess(parent,pageContext);
        ifResult=test||result;
        if(!result&&test){     // 当前条件为true,之前无条件为true
        	return EVAL_BODY_INCLUDE;
        }
        return SKIP_BODY;
    }
	@Override
	public int doEndTag() throws JspException {
		JspTag parent = getParent();
		TagUtils.setIfElseSuccess(parent, ifResult, pageContext);
		return EVAL_PAGE;
	}
}
