
package org.smile.jstl.tags.core;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.smile.jstl.tags.TagUtils;
import org.smile.util.StringUtils;
import org.smile.web.scope.ScopeUtils;

/**
 * get some value to scope. 
 * Default scope is all scope.
 */
public class GetTag extends SimpleTagSupport {

	protected String name;
	
	protected Object property;

	protected String scope;

	public void setScope(String scope) {
		this.scope = scope;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	

	public void setProperty(Object property) {
		this.property = property;
	}

	@Override
	public void doTag() throws JspException {
		PageContext pageContext = (PageContext) getJspContext();
		Object value;
		if(StringUtils.isEmpty(scope)){
			value=ScopeUtils.getAttribute(name, pageContext);
		}else{
			value=ScopeUtils.getScopeAttribute(name, scope, pageContext);
		}
		if(value!=null){
			if(StringUtils.isNull(property)){
				try {
					pageContext.getOut().print(value);
				} catch (IOException e) {
					throw new JspException(e);
				}
			}else{
				try{
					Object result=TagUtils.getValueProperty(value, property);
					if(result!=null){
						pageContext.getOut().print(result);
					}
				}catch(Exception e){
					throw new JspException("get "+value+" property "+property+" error",e);
				}
			}
		}
	}
	
	

}
