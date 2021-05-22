package org.smile.plugin;

/***
 * 拦截器接口
 * @author 胡真山
 * 2015年10月21日
 */
public interface Interceptor extends MethodInterceptor{
	/**
	 * 拦截目标对象  是使用动态代理实现
	 * 所以目标类必须实现接口
	 * @param target 拦截的目标 
	 * @return 返回一个被动态代理的对象
	 */
	public Object plugin(Object target);
	
}
