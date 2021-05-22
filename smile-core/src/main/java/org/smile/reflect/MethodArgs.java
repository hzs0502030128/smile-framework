package org.smile.reflect;

import java.lang.reflect.Method;

/**
 * 方法参数
 * @author 胡真山
 */
public class MethodArgs {
	/**
	 * 方法名
	 */
	protected Method method;
	/**
	 * 参数方法名
	 */
	protected String[] paramNames;
	/**
	 * 方法信息封装
	 * @param method 封装的方法
	 * @param paramNames 此方法的参数名称
	 */
	public MethodArgs(Method method,String[] paramNames){
		this.method=method;
		this.paramNames=paramNames;
	}
	/**
	 * 方法
	 * @param method
	 */
	public MethodArgs(Method method){
		this.method=method;
		this.paramNames=MethodParamNameUtils.getParamNames(method);
	}
	/**
	 * 当前的方法
	 * @return
	 */
	public Method getMethod() {
		return method;
	}
	
	public void setMethod(Method method) {
		this.method = method;
	}
	/**
	 * 方法的名称
	 * @return
	 */
	public String[] getParamNames() {
		return paramNames;
	}
	
	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}
	
}
