
package org.smile.jstl.tags.core;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.smile.util.StringUtils;
import org.smile.web.scope.ScopeUtils;

/**
 * Map 标签  
 * @author 胡真山
 *
 */
public class DeclareTag extends TagSupport{
	
	private Object value;
	
	private String var;
	
	private String scope;
	
	private String type;
	
	@Override
	public int doStartTag() throws JspException {
		try{
			Object realValue=value;
			if(StringUtils.notEmpty(type)){
				realValue=DeclareSupport.convert(type, value);
			}
			ScopeUtils.setScopeAttribute(var,realValue , scope, pageContext);
		}catch(Exception e){
			throw new JspException(e);
		}
		
		return EVAL_BODY_INCLUDE;
	}
	public String getVar() {
		return var;
	}
	public void setVar(String var) {
		this.var = var;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}