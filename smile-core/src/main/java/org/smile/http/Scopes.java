package org.smile.http;

public interface Scopes {
	
	public static final int PAGE_SCOPE=1;
	public static final int REQUEST_SCOPE=2;
	public static final int SESSION_SCOPE=3;
	public static final int APPLICATION_SCOPE=4;
	
	public static final String SCOPE_APPLICATION = "application";
	public static final String SCOPE_SESSION = "session";
	public static final String SCOPE_REQUEST = "request";
	public static final String SCOPE_PAGE = "page";
}
