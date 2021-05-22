package org.smile.web.scope;

import java.util.Enumeration;

import javax.servlet.ServletRequest;

public class RequestScope implements ScopeSupport{
	
	private ServletRequest request;
	
	public RequestScope(ServletRequest request){
		this.request=request;
	}
	
	@Override
	public Object getAttribute(String key) {
		return request.getAttribute(key);
	}

	@Override
	public void setAttribute(String key, Object value) {
		request.setAttribute(key, value);
	}

	@Override
	public void removeAttribute(String key) {
		request.removeAttribute(key);
	}

	@Override
	public Enumeration<String> getAttributes() {
		return request.getAttributeNames();
	}

}
