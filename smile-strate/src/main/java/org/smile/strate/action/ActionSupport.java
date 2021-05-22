package org.smile.strate.action;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smile.http.HttpMethod;
import org.smile.strate.ActionConstants;

public class ActionSupport extends AbstractActionSupport {
	/**
	 * request对象
	 */
	protected HttpServletRequest request;
	/**
	 * response对象
	 */
	protected HttpServletResponse response;
	/**
	 * 提交类型
	 */
	protected HttpMethod method;
	/***
	 * 错误信息
	 */
	protected Map<String, String> actionErrors;
	/**
	 * 提示信息
	 */
	protected Map<String, String> actionMessages;
	/**
	 * 国际化方言
	 */
	protected Locale locale;

	@Override
	public void initPerterties(HttpServletRequest request, HttpServletResponse response, HttpMethod type) throws Exception {
		this.method = type;
		this.request = request;
		this.response = response;
		this.locale = request.getLocale();
	}

	@Override
	public HttpServletRequest request() {
		return request;
	}

	@Override
	public HttpServletResponse response() {
		return response;
	}


	/**
	 * 添加错误信息
	 * 
	 * @param error
	 */
	public final void addActionError(String error) {
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
	public final void addActionMessage(String msg) {
		if (actionMessages == null) {
			actionMessages = new LinkedHashMap<String, String>();
		}
		actionMessages.put(ActionConstants.messagekey + actionMessages.size(), msg);
	}

	@Override
	public Map<String, String> getActionErrors() {
		return actionErrors;
	}

	@Override
	public Map<String, String> getActionMessages() {
		return actionMessages;
	}


	@Override
	public Locale locale() {
		return locale;
	}

	@Override
	public void addActionMessage(String key, String msg) {
		if (actionMessages == null) {
			actionMessages = new LinkedHashMap<String, String>();
		}
		actionMessages.put(key, msg);
	}

	@Override
	public void addActionError(String key, String error) {
		if (actionErrors == null) {
			actionErrors = new LinkedHashMap<String, String>();
		}
		actionErrors.put(key, error);
	}

	@Override
	public void setRequestAndResponse(HttpServletRequest request, HttpServletResponse response,HttpMethod method) {
		this.request=request;
		this.response=response;
		this.method=method;
	}

	@Override
	public void clearRequestAndResponse() {
		this.request=null;
		this.response=null;
		this.method=null;
	}

	@Override
	public HttpMethod method() {
		return this.method;
	}
}
