package org.smile.strate.jslt.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.smile.util.StringUtils;

public class ExceptionTag extends TagSupport {
	private String name;
	@Override
	public int doStartTag() throws JspException {
		if(StringUtils.isEmpty(name)){
			name="exception";
		}
		Exception errors=(Exception)pageContext.getRequest().getAttribute(name);
		if(errors!=null){
			try {
				errors.printStackTrace(pageContext.getResponse().getWriter());
			} catch (IOException e) {
				throw new JspException(e);
			}
		}
		return SKIP_BODY;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
