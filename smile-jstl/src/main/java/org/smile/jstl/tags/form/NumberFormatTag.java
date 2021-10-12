package org.smile.jstl.tags.form;

import javax.servlet.jsp.JspException;

import org.smile.math.NumberUtils;
import org.smile.util.StringUtils;

public class NumberFormatTag extends FormatTag {
	@Override
	public int doStartTag() throws JspException {
		try{
			String result;
			if(value==null){
				result="";
			}else if(StringUtils.isEmpty(format)){
				result= NumberUtils.format((Number)value,DEFAULT_NUMBER_FORMAT);
			}else{
				result= NumberUtils.format((Number)value,format);
			}
			pageContext.getOut().print(result);
		}catch(Exception e){
			throw new JspException(e);
		}
		return SKIP_BODY;
	}
}
