package org.smile.strate;

import java.lang.reflect.Method;
import java.util.List;

import org.smile.commons.SmileRunException;
import org.smile.plugin.MethodInterceptor;
import org.smile.strate.action.Action;
import org.smile.strate.action.ActionElement;
import org.smile.strate.action.ClassElement;
import org.smile.strate.dispatch.DispatchContext;
import org.smile.strate.form.ActionFormResult;
import org.smile.strate.form.ActionParameterFiller;
import org.smile.strate.interceptor.ActionInvocation;

/**
 * strate 跳转执行类
 * 以field属性
 * @author 胡真山
 * @Date 2016年1月18日
 */
public class FieldStrateExecutor extends AbstractStrateExecutor{
	
	@Override
	public Object doActionMethod(DispatchContext context,ActionFormResult fillResult) throws StrateException{
		ActionElement actionElement=context.getActionElement();
		Action action=context.getAction();
		try {
			// 先从配置文件获取方法 如果没有配置 再从提交参数中获取 还是没有 则抛出异常
			String methodName = context.getActionMethodName();
			// 处理服务方法 action 主方法
			Method method = action.getClass().getMethod(methodName);
			// 拦截器处理
			List<MethodInterceptor> interceptors = actionElement.getPackageElement().getInterceptors();
			ActionInvocation invocation = new ActionInvocation(interceptors,context,method);
			invocation.setActionElement(actionElement);
			return invocation.proceed();
		}catch(RuntimeException re){
			throw re;
		} catch (Throwable e) {
			throw new StrateException(action.getClass()+" do action method "+actionElement.getMethod()+" has a error ", e);
		}
	}
	
	
	@Override
	public void initOnClassElement(ClassElement ce) {
		
	}
	
	@Override
	public ActionFormResult requestToAction(DispatchContext context) throws Exception{
		return ActionParameterFiller.fillRequestToAction(context);
	}
	

	@Override
	public Object doOtherActionMethod(DispatchContext context) throws StrateException {
		try {
			// 处理服务方法 action 主方法
			Method method = context.getActionMethod();
			List<MethodInterceptor> interceptors=context.getActionElement().getPackageElement().getInterceptors();
			// 拦截器处理
			ActionInvocation invocation = new ActionInvocation(interceptors,context, method);
			return (String) invocation.proceed();
		} catch (Throwable e) {
			throw new StrateException(context.getAction().getClass()+" do action method "+context.getActionElement().getMethod()+" has a error ", e);
		}
	}
	
	@Override
	public Method getActionMethod(Class actionClass, String method) {
		try {
			return actionClass.getMethod(method);
		} catch (Exception e) {
			throw new SmileRunException(e);
		}
	}
}
