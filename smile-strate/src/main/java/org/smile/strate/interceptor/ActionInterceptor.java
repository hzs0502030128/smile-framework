package org.smile.strate.interceptor;

import org.smile.plugin.Invocation;
import org.smile.plugin.MethodInterceptor;

/**
 * action 拦截器接口
 * @author 胡真山
 * @Date 2016年1月18日
 */
public  abstract class ActionInterceptor implements MethodInterceptor{
	
	/**拦截一个action 动作*/
	public abstract Object intercept(ActionInvocation invocation) throws Throwable;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		if(invocation instanceof ActionInvocation){
			return intercept((ActionInvocation)invocation);
		}
		return invocation.proceed();
	}
	
}
