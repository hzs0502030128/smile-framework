package org.smile.strate.action;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smile.http.HttpMethod;
import org.smile.strate.ActionConstants;
/**
 * 用于封装当前线程中的请求与响应对象
 * @author 胡真山
 *
 */
public class RequestAndResponse {
	/**当前的请求对象*/
	private HttpServletRequest request;
	
	/**当前的响应对象*/
	private HttpServletResponse response;
	/***
	 * 错误信息
	 */
	protected Map<String, String> actionErrors;
	/**
	 * 提示信息
	 */
	protected Map<String, String> actionMessages;
	
	protected HttpMethod method;
	/**
	 * 添加错误信息
	 * 
	 * @param error
	 */
	public synchronized final void addActionError(String error) {
		if (actionErrors == null) {
			actionErrors = new LinkedHashMap<String, String>();
		}
		actionErrors.put(ActionConstants.errorkey + actionErrors.size(), error);
	}

	/**
	 * 添加提示信息
	 * 
	 * @param msg
	 */
	public synchronized final void addActionMessage(String msg) {
		if (actionMessages == null) {
			actionMessages = new LinkedHashMap<String, String>();
		}
		actionMessages.put(ActionConstants.messagekey + actionMessages.size(), msg);
	}

	public Map<String, String> getActionErrors() {
		return actionErrors;
	}

	public Map<String, String> getActionMessages() {
		return actionMessages;
	}

	public synchronized void addActionMessage(String key, String msg) {
		if (actionMessages == null) {
			actionMessages = new LinkedHashMap<String, String>();
		}
		actionMessages.put(key, msg);
	}

	public synchronized void addActionError(String key, String error) {
		if (actionErrors == null) {
			actionErrors = new LinkedHashMap<String, String>();
		}
		actionErrors.put(key, error);
	}
	
	public RequestAndResponse(HttpServletRequest request,HttpServletResponse response,HttpMethod method){
		this.request=request;
		this.response=response;
		this.method=method;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}
	
	public Locale locale() {
		return request.getLocale();
	}

	public HttpMethod getMethod() {
		return method;
	}
	
	
	
}
