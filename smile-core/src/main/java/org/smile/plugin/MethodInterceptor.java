package org.smile.plugin;


/**
 * 调用的代理
 * @author 胡真山
 * @Date 2016年1月15日
 */
public interface MethodInterceptor {
	/**
	 * 执行拦截方法
	 * @param invocation 被拦截的动作
	 * @return 
	 * @throws Exception
	 */
	public Object intercept(Invocation invocation) throws Throwable;
}
