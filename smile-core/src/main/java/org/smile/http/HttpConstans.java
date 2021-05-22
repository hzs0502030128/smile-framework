package org.smile.http;

import org.smile.util.RegExp;

/**
 * http协议的一些常量信息
 * @author 胡真山
 */
public interface HttpConstans {
	/**
	 * 授权信息头名
	 */
	public static final String AUTHOR="Authorization";
	/**
	 * 无授权信息状态
	 */
	public static final int STATUS_NOT_AUTHORIZED=401;
	/**正常OK*/
	public static final int STATUS_OK=200;
	/**请求错误*/
	public static final int STATUS_BAD=400;
	/**
	 * 认证查找正则
	 */
	public static final RegExp BASIC_AUTHOR_REG=new RegExp("Basic +");
	
	public static final String HEADER_IF_NONE = "If-None-Match";
	/**
	 * 基本认证信息前缀
	 */
	public static final String BASIC_AUTHOR_PREFIX="Basic ";
	
	/**
     * HTTP etag header
     */
    public static final String HEADER_ETAG = "ETag";

    /**
     * HTTP header for when a file was last modified
     */
    public static final String HEADER_LAST_MODIFIED = "Last-Modified";

    /**
     * HTTP header to request only modified data
     */
    public static final String HEADER_IF_MODIFIED = "If-Modified-Since";

    /**
     * The name of the user agent HTTP header
     */
    public static final String HEADER_USER_AGENT = "User-Agent";
    /***
     * HTML RESPONSE UTF-8
     */
    public static final String HTML_UTF_CONTENT_TYPE="text/html;charset=utf-8";
    /***
     * JSON RESPONSE UTF-8
     */
    public static final String JSON_UTF_CONTENT_TYPE="application/json;charset=utf-8";
    
    public static final String  CONTENT_ENCODING = "Content-Encoding";
    /**content-type header 名称*/
    public static final String CONTENT_TYPE = "Content-Type";
    /**
     * JSON RESPONSE
     */
    public static final String JSON_CONTENT_TYPE="application/json";
}
