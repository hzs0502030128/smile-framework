package org.smile.strate.dispatch;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smile.commons.SmileRunException;
import org.smile.http.HttpMethod;
import org.smile.strate.ActionConstants;
import org.smile.strate.StrateException;
import org.smile.strate.action.Action;
import org.smile.strate.action.ActionElement;
import org.smile.util.StringUtils;

public class DispatchContext {
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String uri;
	private HttpMethod httpMothod;
	private Action action;
	private ActionElement actionElement;
	private StrateDispatcher dispatcher;
	private ActionURLInfo actionUrlInfo;
	
	public DispatchContext(HttpServletRequest request,HttpServletResponse response,HttpMethod httpMothod) {
		this.request=request;
		this.response=response;
		this.httpMothod=httpMothod;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public ActionElement getActionElement() {
		return actionElement;
	}

	public void setActionElement(ActionElement actionElement) {
		this.actionElement = actionElement;
	}

	

	public HttpMethod getHttpMothod() {
		return httpMothod;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public StrateDispatcher getDispatcher() {
		return dispatcher;
	}

	public void setDispatcher(StrateDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public String getContextPath() {
		return this.request.getContextPath();
	}

	public ActionURLInfo getActionUrlInfo() {
		return actionUrlInfo;
	}


	public void setActionUrlInfo(ActionURLInfo actionUrlInfo) {
		this.actionUrlInfo = actionUrlInfo;
	}
	
	public Method getActionMethod() throws NoSuchMethodException, SecurityException {
		return action.getClass().getMethod(getActionMethodName());
	}
	
	public Class getActionClass() {
		return action.getClass();
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public ActionURLInfo parseURI(String uri) {
		return this.dispatcher.parseURI(request.getContextPath(), uri);
	}
	
	public Object onOtherActionDo() throws StrateException {
		return actionElement.getExecutor().doOtherActionMethod(this);
	}
	
	/**
	 * 	先从配置文件获取方法 如果没有配置 再从提交参数中获取 还是没有 则抛出异常
	 * @param actionElement
	 * @param request
	 * @return
	 * @throws StrateException
	 */
	public  String getActionMethodName()  {
		String methodName = actionElement.getMethod();
		if (StringUtils.isEmpty(methodName)) {
			methodName = request.getParameter(ActionConstants.actionMethodParam);
		}
		if (StringUtils.isEmpty(methodName)) {
			throw new SmileRunException(" please give a designated parameter method ");
		}
		return methodName;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	

}
