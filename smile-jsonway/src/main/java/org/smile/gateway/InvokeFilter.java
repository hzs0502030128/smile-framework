package org.smile.gateway;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InvokeFilter {
	/**
	 * @param service
	 * @param method
	 * @param args
	 */
	public Object invoke(HttpServletRequest request, HttpServletResponse response,Object service,Method method,Object[] args) throws Exception{
		return method.invoke(service, args);
	}
}
