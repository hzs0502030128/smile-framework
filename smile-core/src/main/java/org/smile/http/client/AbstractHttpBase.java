package org.smile.http.client;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import org.smile.commons.Strings;
import org.smile.http.HttpMethod;

public abstract class AbstractHttpBase<T extends HttpBase<?>> implements HttpBase<T> {
	
	protected String contentType;

	protected int statusCode;

	protected String statusMessage;
	/** 服务器地址 */
	protected URL url;
	/** 请求的方法 */
	protected HttpMethod method;
	/** 字符编码 */
	protected String charset=Strings.UTF_8;
	/** 头信息 */
	protected Map<String, String> headers;
	/** cookie信息封装 */
	protected Map<String, String> cookies;

	protected AbstractHttpBase() {
		headers = new LinkedHashMap<String, String>();
		cookies = new LinkedHashMap<String, String>();
	}
	@Override
	public URL url() {
		return url;
	}
	@Override
	public T url(URL url) {
		this.url = url;
		return (T) this;
	}
	@Override
	public HttpMethod method() {
		return method;
	}
	@Override
	public T method(HttpMethod method) {
		this.method = method;
		return (T) this;
	}
	@Override
	public String header(String name) {
		return getHeaderCaseInsensitive(name);
	}
	@Override
	public T header(String name, String value) {
		removeHeader(name); // ensures we don't get an "accept-encoding" and a
							// "Accept-Encoding"
		headers.put(name, value);
		return (T) this;
	}
	@Override
	public boolean hasHeader(String name) {
		return getHeaderCaseInsensitive(name) != null;
	}

	/**
	 * Test if the request has a header with this value (case insensitive).
	 */
	@Override
	public boolean hasHeaderWithValue(String name, String value) {
		return hasHeader(name) && header(name).equalsIgnoreCase(value);
	}

	@Override
	public T removeHeader(String name) {
		Map.Entry<String, String> entry = scanHeaders(name); // remove is case
																// insensitive
																// too
		if (entry != null) headers.remove(entry.getKey()); // ensures correct
															// case
		return (T) this;
	}

	@Override
	public Map<String, String> headers() {
		return headers;
	}

	private String getHeaderCaseInsensitive(String name) {
		// quick evals for common case of title case, lower case, then scan for
		// mixed
		String value = headers.get(name);
		if (value == null) value = headers.get(name.toLowerCase());
		if (value == null) {
			Map.Entry<String, String> entry = scanHeaders(name);
			if (entry != null) value = entry.getValue();
		}
		return value;
	}

	private Map.Entry<String, String> scanHeaders(String name) {
		String lc = name.toLowerCase();
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			if (entry.getKey().toLowerCase().equals(lc)) return entry;
		}
		return null;
	}
	@Override
	public String cookie(String name) {
		return cookies.get(name);
	}

	@Override
	public T cookie(String name, String value) {
		cookies.put(name, value);
		return (T) this;
	}

	@Override
	public boolean hasCookie(String name) {
		return cookies.containsKey(name);
	}

	@Override
	public T removeCookie(String name) {
		cookies.remove(name);
		return (T) this;
	}

	@Override
	public Map<String, String> cookies() {
		return cookies;
	}

	@Override
	public String contentType() {
		return this.contentType;
	}

	@Override
	public T charset(String charset) {
		this.charset = charset;
		return (T)this;
	}
	
	@Override
	public T contentType(String contentType) {
		this.contentType=contentType;
		return (T)this;
	}
	
	@Override
	public String charset() {
		return this.charset;
	}
	
	
}
