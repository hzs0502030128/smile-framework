package org.smile.jstl.tags.form;

import javax.servlet.jsp.JspException;

import org.smile.jstl.functions.FormatFunction;
import org.smile.util.StringUtils;

public class DateFormatTag extends FormatTag {
	@Override
	public int doStartTag() throws JspException {
		try{
			if(StringUtils.isEmpty(format)){
				format=DEFAULT_DATE_FORMAT;
			}
			String result=FormatFunction.formatDate(value, format);
			pageContext.getOut().print(result);
		}catch(Exception e){
			throw new JspException(e);
		}
		return SKIP_BODY;
	}
}
