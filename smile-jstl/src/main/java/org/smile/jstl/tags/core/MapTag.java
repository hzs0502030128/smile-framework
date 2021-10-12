
package org.smile.jstl.tags.core;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.smile.jstl.tags.JSONParseSupport;
import org.smile.util.StringUtils;
import org.smile.web.scope.ScopeUtils;

/**
 * Map 标签  
 * @author 胡真山
 *
 */
public class MapTag extends TagSupport{
	
	public static final String MAP_TAG_FLAG="smile_map_tag_flag_parent";
	
	private Object value;
	
	private String var="map";
	
	private String scope;
	
	private Map mapValues;
	
	
	@Override
	public int doStartTag() throws JspException {
		if(StringUtils.isNull(value)){
			mapValues=new LinkedHashMap();
		}else if(value instanceof Map){
			mapValues=(Map)value;
		}else if(value instanceof String){
			mapValues=JSONParseSupport.parseToMap(value);
		}
		pageContext.setAttribute(MAP_TAG_FLAG, this);
		ScopeUtils.setScopeAttribute(var, mapValues, scope, pageContext);
		return EVAL_BODY_INCLUDE;
	}
	@Override
	public int doEndTag() throws JspException {
		pageContext.removeAttribute(MAP_TAG_FLAG);
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
	public Map getMapValues() {
		return mapValues;
	}
	
	
}