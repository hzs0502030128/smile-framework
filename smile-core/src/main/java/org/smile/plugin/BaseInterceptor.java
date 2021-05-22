package org.smile.plugin;

/**
 * 一个简单的拦截器  
 * 在执行之前做一些事
 * 在之后做一些事
 * @author 胡真山
 * 2015年9月25日
 */
public class BaseInterceptor implements Interceptor{

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		doBefore(invocation);
		Object result=null;
		try{
			result=invocation.proceed();
		}catch(Throwable e){
			doException(e,invocation);
		}finally{
			doFinally(invocation,result);
		}
		return doAfter(invocation,result);
	}
	
	protected void doFinally(Invocation invocation, Object result) throws Throwable{
		
	}

	/**
	 * 拦截之前 处理
	 * @param invocation
	 */
	protected void doBefore(Invocation invocation) throws Throwable{
		
	}
	/**
	 * 拦截之后处理
	 * @param invocation
	 */
	protected Object doAfter(Invocation invocation,Object proceeResult) throws Throwable{
		return proceeResult;
	}
	/**
	 * 异常时处理
	 */
	protected void doException(Throwable e,Invocation invocation) throws Throwable{
		throw e;
	}
}
