package org.smile.http.client;

import java.net.URL;
import java.util.Map;

import org.smile.http.HttpMethod;

public interface HttpBase<T extends HttpBase<?>> {
	 /**
     * Get the URL
     * @return URL
     */
	public URL url();

    /**
     * Set the URL
     * @param url new URL
     * @return this, for chaining
     */
	public T url(URL url);

    /**
     * Get the request method
     * @return method
     */
    public HttpMethod method();

    /**
     * Set the request method
     * @param method new method
     * @return this, for chaining
     */
    public T method(HttpMethod method);

    /**
     * Get the value of a header. This is a simplified header model, where a header may only have one value.
     * <p>
     * Header names are case insensitive.
     * </p>
     * @param name name of header (case insensitive)
     * @return value of header, or null if not set.
     * @see #hasHeader(String)
     * @see #cookie(String)
     */
    public String header(String name);

    /**
     * Set a header. This method will overwrite any existing header with the same case insensitive name.
     * @param name Name of header
     * @param value Value of header
     * @return this, for chaining
     */
    public T header(String name, String value);

    /**
     * Check if a header is present
     * @param name name of header (case insensitive)
     * @return if the header is present in this request/response
     */
    public boolean hasHeader(String name);

    /**
     * Check if a header is present, with the given value
     * @param name header name (case insensitive)
     * @param value value (case insensitive)
     * @return if the header and value pair are set in this req/res
     */
    public boolean hasHeaderWithValue(String name, String value);

    /**
     * Remove a header by name
     * @param name name of header to remove (case insensitive)
     * @return this, for chaining
     */
    public T removeHeader(String name);

    /**
     * Retrieve all of the request/response headers as a map
     * @return headers
     */
    public Map<String, String> headers();

    /**
     * Get a cookie value by name from this request/response.
     * <p>
     * Response objects have a simplified cookie model. Each cookie set in the response is added to the response
     * object's cookie key=value map. The cookie's path, domain, and expiry date are ignored.
     * </p>
     * @param name name of cookie to retrieve.
     * @return value of cookie, or null if not set
     */
    public String cookie(String name);

    /**
     * Set a cookie in this request/response.
     * @param name name of cookie
     * @param value value of cookie
     * @return this, for chaining
     */
    public T cookie(String name, String value);

    /**
     * Check if a cookie is present
     * @param name name of cookie
     * @return if the cookie is present in this request/response
     */
    public boolean hasCookie(String name);

    /**
     * Remove a cookie by name
     * @param name name of cookie to remove
     * @return this, for chaining
     */
    public T removeCookie(String name);

    /**
     * Retrieve all of the request/response cookies as a map
     * @return cookies
     */
    public Map<String, String> cookies();
    
    public String contentType();
    
    public T contentType(String contentType);
    /***
     * set charset 
     * @param charset
     * @return
     */
    public T charset(String charset);
    /***
     * 
     * @return charset used
     */
    public String charset();
}
