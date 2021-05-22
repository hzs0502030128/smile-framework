package org.smile.web.scope;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.smile.http.Scopes;
import org.smile.util.StringUtils;

public class ScopeUtils implements Scopes{
	private static Map<String,Integer> scopeMap=new LinkedHashMap<String,Integer>();
	static{
		scopeMap.put(SCOPE_PAGE, PAGE_SCOPE);
		scopeMap.put(SCOPE_REQUEST,REQUEST_SCOPE);
		scopeMap.put(SCOPE_SESSION, SESSION_SCOPE);
		scopeMap.put(SCOPE_APPLICATION, APPLICATION_SCOPE);
	}
	
	public static Integer getScopeIndex(String scopeName,Integer def){
		if(StringUtils.isEmpty(scopeName)){
			return def;
		}else{
			return scopeMap.get(scopeName);
		}
	}
	
	public static Integer getScopeIndex(String scopeName){
		return getScopeIndex(scopeName, null);
	}
	
	
	/**
	 * get scope attribute.
	 */
	public static Object getScopeAttribute(String name,String scope, PageContext pageContext) throws JspException {
		Integer scopeIndex=getScopeIndex(scope, PAGE_SCOPE);
		if(scopeIndex==null){
			throw new JspException("Invalid scope: " + scope);
		}
		return pageContext.getAttribute(name,scopeIndex);
	}
	
	/**
	 * 从所有的scope中获取
	 * @param name
	 * @param pageContext
	 * @return
	 */
	public static Object getAttribute(String name, PageContext pageContext){
		for(Integer index:scopeMap.values()){
			Object value=pageContext.getAttribute(name, index);
			if(value!=null){
				return value;
			}
		}
		return null;
	}
	
	/**
	 * Sets scope attribute.
	 */
	public static void setScopeAttribute(String name, Object value, String scope, PageContext pageContext) throws JspException {
		Integer scopeIndex=getScopeIndex(scope, PAGE_SCOPE);
		if(scopeIndex==null){
			throw new JspException("Invalid scope: " + scope);
		}
		pageContext.setAttribute(name, value, scopeIndex);
	}

	/**
	 * Removes scope attribute.
	 */
	public static void removeScopeAttribute(String name, String scope, PageContext pageContext) throws JspException {
		Integer scopeIndex=getScopeIndex(scope, PAGE_SCOPE);
		if(scopeIndex==null){
			throw new JspException("Invalid scope: " + scope);
		}
		pageContext.removeAttribute(name,scopeIndex);
	}
}
