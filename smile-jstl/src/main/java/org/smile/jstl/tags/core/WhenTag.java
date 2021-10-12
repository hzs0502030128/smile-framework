
package org.smile.jstl.tags.core;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.smile.beans.converter.BaseTypeConverter;
import org.smile.beans.converter.ConvertException;
import org.smile.util.StringUtils;

/**
 * 
 */
public class WhenTag extends BodyTagSupport{

	/**条件判断*/
	private boolean testValue;
	/**
	 * 是否子标签已经处理了body
	 */
	private boolean usedBody=false;

	public void setTest(String test) {
		try {
			if(StringUtils.isEmpty(test)){
				this.testValue=false;
			}else{
				this.testValue = BaseTypeConverter.getInstance().convert(boolean.class,test);
			}
		} catch (ConvertException ignore) {
			this.testValue = false;
		}
	}

	/**
	 * Returns test value
	 */
	public boolean getTestValue() {
		return testValue;
	}

	public void setUsedBody(boolean usedBody) {
		this.usedBody = usedBody;
	}
	
	@Override
	public int doStartTag() throws JspException {
		usedBody=false;
		return EVAL_BODY_BUFFERED;
	}


	@Override
	public int doEndTag() throws JspException {
		if(usedBody||testValue){
			try {
				if(bodyContent!=null){
					pageContext.getOut().print(bodyContent.getString());
				}
			} catch (IOException e) {
				throw new JspException(e);
			}
		}
		clearBody();
		return EVAL_PAGE;
		
	}
	
	public void clearBody(){
		if(bodyContent!=null){
			bodyContent.clearBody();
		}
	}
	

	public void setTest(boolean test){
		this.testValue=test;
	}
}

