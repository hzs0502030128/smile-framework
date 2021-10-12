
package org.smile.jstl.tags.core;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.smile.beans.converter.BeanException;
import org.smile.jstl.tags.PropertySupport;
import org.smile.jstl.tags.TagUtils;
import org.smile.util.StringUtils;
import org.smile.web.scope.ScopeUtils;

/**
 * Sets some value to scope. Default scope is 'page'.
 */
public class SetTag extends SimpleTagSupport {

	protected String name;
	
	protected Object value;
	
	protected Object property;
	
	protected String scope;

	public void setName(String name) {
		this.name = name;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public void setProperty(Object property) {
		this.property = property;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public void doTag() throws JspException {
		PageContext pageContext = (PageContext) getJspContext();
		boolean isexpression=PropertySupport.isExpression(value);
		if(isexpression){
			try {
				value=PropertySupport.getExpressionValue((String)value, pageContext);
			} catch (BeanException e) {
				throw new JspException(e);
			}
		}else if(!StringUtils.isNull(property)){
			value=TagUtils.getValueProperty(value, property);
		}
		ScopeUtils.setScopeAttribute(name, value, scope, pageContext);
		reset();
	}
	
	protected void reset(){
		this.value=null;
		this.property=null;
	}
	
}
