package org.smile.jstl.tags.form;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.smile.jstl.functions.StringFunction;

public class OmitTag extends TagSupport {
	/**字符串的值*/
	private String value;
	/**最多显示字符长度*/
	private int len=5;
	/**省略部分代替*/
	private String end="...";
	@Override
	public int doStartTag() throws JspException {
		try{
			if(value.length()<=len){
				pageContext.getOut().print(value);
			}else{
				StringBuilder result=new StringBuilder(len*2+20);
				result.append("<label title='").append(value).append("'>");
				result.append(StringFunction.substr(value, 0, len));
				result.append(end);
				result.append("</label>");
				pageContext.getOut().print(result);
			}
		}catch(Exception e){
			throw new JspException(e);
		}
		return SKIP_BODY;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public void setEnd(String end) {
		this.end = end;
	}
	
	
}
