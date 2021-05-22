package org.smile.web.scope;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;

public class SessionScope implements ScopeSupport{
	
	private HttpSession session;
	
	public String getId(){
		return session.getId();
	}
	
	public SessionScope(HttpSession session){
		this.session=session;
	}
	
	@Override
	public Object getAttribute(String key) {
		return session.getAttribute(key);
	}

	@Override
	public void setAttribute(String key, Object value) {
		session.setAttribute(key, value);
	}

	@Override
	public void removeAttribute(String key) {
		session.removeAttribute(key);
	}

	@Override
	public Enumeration<String> getAttributes() {
		return session.getAttributeNames();
	}

}
