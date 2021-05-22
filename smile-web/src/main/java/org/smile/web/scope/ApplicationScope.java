package org.smile.web.scope;

import java.util.Enumeration;

import javax.servlet.ServletContext;

public class ApplicationScope implements ScopeSupport{
	
	private ServletContext application;
	
	public ApplicationScope(ServletContext application){
		this.application=application;
	}
	
	@Override
	public Object getAttribute(String key) {
		return application.getAttribute(key);
	}

	@Override
	public void setAttribute(String key, Object value) {
		application.setAttribute(key, value);
	}

	@Override
	public void removeAttribute(String key) {
		application.removeAttribute(key);
	}

	@Override
	public Enumeration<String> getAttributes() {
		return application.getAttributeNames();
	}

}
