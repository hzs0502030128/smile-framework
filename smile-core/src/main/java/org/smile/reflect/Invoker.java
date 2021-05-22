package org.smile.reflect;

import java.lang.reflect.Method;

import org.smile.commons.SmileRunException;
/**
 * 用于对反射对象方法的封装
 * @author 胡真山
 * @Date 2016年2月1日
 */
public class Invoker {
	/**
	 * 反射方法的类
	 */
	private Class clazz;
	/**
	 * 封装方法
	 */
	private Method method;
	/**
	 * 
	 * @param clazz 方法的类
	 * @param method 方法名
	 * @param types 方法参数
	 */
	public Invoker(Class clazz,String method,Class ...types ){
		this.clazz=clazz;
		try {
			this.method=clazz.getMethod(method, types);
		} catch (Exception e) {
			throw new SmileRunException(" init invoker error "+clazz+" method:"+method+" types:"+types,e);
		}
	}
	
	public Invoker(Method method){
		this.clazz=method.getDeclaringClass();
		this.method=method;
	}
	/**
	 * 调用 方法
	 * @param target 调用的对象
	 * @param args 调用的参数
	 * @return
	 */
	public Object call(Object target,Object[] args){
		try {
			return method.invoke(target, args);
		} catch (Exception e) {
			throw new SmileRunException(e);
		}
	}
	/***
	 * 调用方法的类
	 * @return
	 */
	public Class getTargetClass(){
		return clazz;
	}
	/**
	 * 调用的方法
	 * @return
	 */
	public Method getMethod(){
		return method;
	}
}
