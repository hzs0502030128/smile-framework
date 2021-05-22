package org.smile.http;


public enum HttpMethod {
	
	GET(false), POST(true), PUT(true), DELETE(false), PATCH(true), HEAD(false), OPTIONS(false), TRACE(false);

	private final boolean hasBody;

	HttpMethod(boolean hasBody) {
		this.hasBody = hasBody;
	}

	public final boolean hasBody() {
		return hasBody;
	}
}
