package org.smile.plugin;

import java.lang.reflect.Method;
/**
 * 被拦截的动作调用
 * @author 胡真山
 * 2015年9月25日
 */
public interface Invocation extends Joinpoint{
	/**
	 * 此动作的方法
	 * @return
	 */
	public Method getMethod();
	/**
	 * 此动作的参数
	 * @return
	 */
	public Object[] getArgs();
	/**
	 * 设置动作的执行参数
	 * @param objects
	 */
	public void setArgs(Object[] args);
	
}
