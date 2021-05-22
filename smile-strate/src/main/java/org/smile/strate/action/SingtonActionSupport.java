package org.smile.strate.action;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smile.http.HttpMethod;


public class SingtonActionSupport extends AbstractActionSupport{
	
	@Override
	public void initPerterties(HttpServletRequest request, HttpServletResponse response, HttpMethod type) throws Exception {
		
	}

	@Override
	public HttpServletRequest request() {
		return ActionContext.getRequest();
	}

	@Override
	public HttpServletResponse response() {
		return ActionContext.getResponse();
	}

	@Override
	public void addActionError(String error) {
		RequestAndResponse rar=ActionContext.getRequestAndResponse();
		rar.addActionError(error);
	}

	@Override
	public void addActionMessage(String msg) {
		RequestAndResponse rar=ActionContext.getRequestAndResponse();
		rar.addActionMessage(msg);
	}

	@Override
	public Locale locale() {
		RequestAndResponse rar=ActionContext.getRequestAndResponse();
		return rar.locale();
	}

	@Override
	public void addActionMessage(String key, String msg) {
		RequestAndResponse rar=ActionContext.getRequestAndResponse();
		rar.addActionMessage(key, msg);
	}

	@Override
	public void addActionError(String key, String error) {
		RequestAndResponse rar=ActionContext.getRequestAndResponse();
		rar.addActionError(key,error);
	}

	@Override
	public void setRequestAndResponse(HttpServletRequest request, HttpServletResponse response,HttpMethod method) {
		ActionContext.setRequestAndResponse(request, response,method);
	}

	@Override
	public void clearRequestAndResponse() {
		ActionContext.romoveRequestAndResponse();
	}

	@Override
	public boolean needRequestToAction() {
		//单例模式时默认不往action中赋值
		return false;
	}

	@Override
	public HttpMethod method() {
		return ActionContext.getRequestAndResponse().getMethod();
	}

	@Override
	public Map<String, String> getActionErrors() {
		return ActionContext.getRequestAndResponse().getActionErrors();
	}

	@Override
	public Map<String, String> getActionMessages() {
		return ActionContext.getRequestAndResponse().getActionMessages();
	}
}
