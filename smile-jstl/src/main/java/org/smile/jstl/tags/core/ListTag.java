
package org.smile.jstl.tags.core;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.smile.json.JSON;
import org.smile.util.StringUtils;
import org.smile.web.scope.ScopeUtils;

/**
 * list 标签  
 * @author 胡真山
 *
 */
public class ListTag extends TagSupport{
	
	public static final String LIST_TAG_FLAG="smile_list_tag_flag_parent";
	
	private Object value;
	
	private String var="list";
	
	private String scope;
	
	private List listValues;
	
	
	@Override
	public int doStartTag() throws JspException {
		if(StringUtils.isNull(value)){
			listValues=new LinkedList();
		}else if(value instanceof List){
			listValues=(List)value;
		}else if(value instanceof String){
			Object jsonValue= JSON.parse((String)value);
			if(jsonValue instanceof List){
				listValues=(List)jsonValue;
			}else{
				throw new JspException("value: 错误 "+value);
			}
		}
		pageContext.setAttribute(LIST_TAG_FLAG, this);
		ScopeUtils.setScopeAttribute(var, listValues, scope, pageContext);
		return EVAL_BODY_INCLUDE;
	}
	@Override
	public int doEndTag() throws JspException {
		pageContext.removeAttribute(LIST_TAG_FLAG);
		return EVAL_PAGE;
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
	public List getListValues() {
		return listValues;
	}
	
	
}