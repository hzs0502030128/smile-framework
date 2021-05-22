package org.smile.http.client;

import java.net.Proxy;
import java.util.Collection;
import java.util.Map;


public interface HttpRequest<T extends HttpRequest<?>> extends HttpBase<T>{
	/**
     * Get the proxy used for this request.
     * @return the proxy; <code>null</code> if not enabled.
     */
    Proxy proxy();

    /**
     * Update the proxy for this request.
     * @param proxy the proxy ot use; <code>null</code> to disable.
     * @return this Request, for chaining
     */
    T proxy(Proxy proxy);
    
    boolean hasProxy();

    /**
     * Set the HTTP proxy to use for this request.
     * @param host the proxy hostname
     * @param port the proxy port
     * @return this Connection, for chaining
     */
    T proxy(String host, int port);

    /**
     * Get the request timeout, in milliseconds.
     * @return the timeout in milliseconds.
     */
    int timeout();

    /**
     * Update the request timeout.
     * @param millis timeout, in milliseconds
     * @return this Request, for chaining
     */
    T timeout(int millis);

    /**
     * Get the maximum body size, in bytes.
     * @return the maximum body size, in bytes.
     */
    int maxBodySize();

    /**
     * Update the maximum body size, in bytes.
     * @param bytes maximum body size, in bytes.
     * @return this Request, for chaining
     */
    T maxBodySize(int bytes);

    /**
     * Get the current followRedirects configuration.
     * @return true if followRedirects is enabled.
     */
    boolean followRedirects();

    /**
     * Configures the request to (not) follow server redirects. By default this is <b>true</b>.
     * @param followRedirects true if server redirects should be followed.
     * @return this Request, for chaining
     */
    T followRedirects(boolean followRedirects);

    /**
     * Get the current ignoreHttpErrors configuration.
     * @return true if errors will be ignored; false (default) if HTTP errors will cause an IOException to be
     * thrown.
     */
    boolean ignoreHttpErrors();

    /**
     * Configures the request to ignore HTTP errors in the response.
     * @param ignoreHttpErrors set to true to ignore HTTP errors.
     * @return this Request, for chaining
     */
    T ignoreHttpErrors(boolean ignoreHttpErrors);

    /**
     * Get the current ignoreContentType configuration.
     * @return true if invalid content-types will be ignored; false (default) if they will cause an IOException to
     * be thrown.
     */
    boolean ignoreContentType();

    /**
     * Configures the request to ignore the Content-Type of the response.
     * @param ignoreContentType set to true to ignore the content type.
     * @return this Request, for chaining
     */
    T ignoreContentType(boolean ignoreContentType);

    /**
     * Get the current state of TLS (SSL) certificate validation.
     * @return true if TLS cert validation enabled
     */
    boolean validateTLSCertificates();

    /**
     * Set TLS certificate validation.
     * @param value set false to ignore TLS (SSL) certificates
     */
    void validateTLSCertificates(boolean value);

    /**
     * Set a POST (or PUT) request body. Useful when a server expects a plain request body, not a set for URL
     * encoded form key/value pairs. E.g.:
     * <code><pre>
     * .requestBody(json)
     * .header("Content-Type", "application/json")
     * .post();</pre></code>
     * If any data key/vals are supplied, they will be send as URL query params.
     * @return this Request, for chaining
     */
    T requestBody(String body);

    /**
     * Get the current request body.
     * @return null if not set.
     */
    String requestBody();
    /**
     * 
     * @return
     */
    T requestJsonBody(String json);

    /**
     * Sets the post data character set for x-www-form-urlencoded post data
     * @param charset character set to encode post data
     * @return this Request, for chaining
     */
    T postDataCharset(String charset);

    /**
     * Gets the post data character set for x-www-form-urlencoded post data
     * @return character set to encode post data
     */
    String postDataCharset();
    /***
     *提交的数据 
     * @return
     */
    Collection<RequestValue> datas();

	T data(RequestValue keyval);
	/**
	 * 设置提交数据
	 * @param name 提交数据名称
	 * @param value 提交数据值
	 * @return
	 */
	T data(String name,String value);
	/**
	 * 设置多个提交数据
	 * @param datas
	 * @return
	 */
	T data(Map<String,Object> datas);

	/***
	 * 是否需要多媒体提交
	 * @return
	 */
	boolean needsMultipart(); 
	
	/***
	 * 是不是有请求body
	 * @return
	 */
	public boolean hasRequestBody();
}
