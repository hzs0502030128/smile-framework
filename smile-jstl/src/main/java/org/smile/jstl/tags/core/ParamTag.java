
package org.smile.jstl.tags.core;

import javax.servlet.jsp.JspException;

import org.smile.beans.converter.BeanException;
import org.smile.commons.Strings;
import org.smile.jstl.tags.PropertySupport;
import org.smile.util.StringUtils;
/**
 * url tag 内部标签的父类
 * @author 胡真山
 *
 */
public class ParamTag extends UrlTagSupport{
	
	private String name;
	
	private String value;
	
	@Override
	public int doStartTag() throws JspException {
		super.doStartTag();
		if(value==null){
			return EVAL_BODY_BUFFERED;
		}
		return SKIP_BODY;
	}
	@Override
	public int doEndTag() throws JspException {
		if(value==null){
			if(bodyContent!=null){
				value=bodyContent.getString();
			}
			if(StringUtils.isEmpty(value)){
				try {
					Object objValue=PropertySupport.getPropertyValue(name, pageContext);
					if(objValue==null){
						value=Strings.BLANK;
					}else{
						value=String.valueOf(objValue);
					}
				} catch (BeanException e) {
					throw new JspException(e);
				}
			}
		}
		urlTag.addParam(name, value);
		reset();
		return EVAL_PAGE;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * 重置参数
	 */
	protected void reset(){
		this.value=null;
	}
	
	@Override
	public void release() {
		super.release();
		reset();
	}
}