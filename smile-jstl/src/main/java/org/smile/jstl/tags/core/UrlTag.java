
package org.smile.jstl.tags.core;

import java.net.URLEncoder;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.smile.util.StringUtils;
import org.smile.web.scope.ScopeUtils;

public class UrlTag extends TagSupport{
	
	protected static String URL_TAG_CURRENT="smile_current_url_tag";

	protected String action;
	
	protected String id;
	
	protected StringBuilder url;
	/**是否已经有参数了*/
	protected boolean hasParam=false;
	/***/
	protected String encode;
	
	protected String scope;

	@Override
	public int doStartTag() throws JspException {
		encode=pageContext.getRequest().getCharacterEncoding();
		int index=action.indexOf("?");
		if(index>0){
			hasParam=true;
			url=new StringBuilder(encode(action.substring(0, index), encode));
			url.append("?");
			url.append(encode(action.substring(index+1), encode));
		}else{
			hasParam=false;
			url=new StringBuilder(encode(action, encode));
		}
		pageContext.setAttribute(URL_TAG_CURRENT, this);
		return EVAL_BODY_INCLUDE;
	}
	
	protected String encode(String value,String encode) throws JspException{
		try{
			return URLEncoder.encode(value,encode);
		}catch(Exception e){
			throw new JspException(e);
		}
	}
		
	protected void addParam(String key,Object value) throws JspException{
		if(hasParam){
			url.append("&");
		}else{
			url.append("?");
			hasParam=true;
		}
		if (value instanceof Iterable) {
			value = StringUtils.join((Iterable) value, ';');
		} else if (value instanceof Object[]) {
			value = StringUtils.join((Object[]) value, ';');
		}
		url.append(encode(key,encode)).append("=").append(encode(String.valueOf(value),encode));
	}


	@Override
	public int doEndTag() throws JspException {
		ScopeUtils.setScopeAttribute(id, url.toString(), scope, pageContext);
		pageContext.removeAttribute(URL_TAG_CURRENT);
		return EVAL_PAGE;
	}

	public String getAction() {
		return action;
	}


	public void setAction(String action) {
		this.action = action;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
	
	
}