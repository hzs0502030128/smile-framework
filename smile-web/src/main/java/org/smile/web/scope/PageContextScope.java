package org.smile.web.scope;

import java.util.Enumeration;

import javax.servlet.jsp.PageContext;

import org.smile.http.Scopes;

public class PageContextScope implements ScopeSupport{
	
	private PageContext page;
	
	public PageContextScope(PageContext page){
		this.page=page;
	}
	
	@Override
	public Object getAttribute(String key) {
		return page.getAttribute(key);
	}

	@Override
	public void setAttribute(String key, Object value) {
		page.setAttribute(key, value);
	}

	@Override
	public void removeAttribute(String key) {
		page.removeAttribute(key);
	}

	@Override
	public Enumeration<String> getAttributes() {
		return page.getAttributeNamesInScope(Scopes.PAGE_SCOPE);
	}

}
