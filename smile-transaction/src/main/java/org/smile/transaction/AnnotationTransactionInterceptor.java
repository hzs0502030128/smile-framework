package org.smile.transaction;

import java.lang.reflect.Method;
import java.util.Set;

import org.smile.annotation.AnnotationUtils;
import org.smile.commons.ann.Intercept;
import org.smile.plugin.Interceptor;
import org.smile.plugin.Invocation;
import org.smile.plugin.Plugin;
import org.smile.reflect.ClassTypeUtils;
import org.smile.reflect.MethodUtils;
@Intercept(method ="*")
public class AnnotationTransactionInterceptor implements Interceptor {
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Method method = invocation.getMethod();
		//再看方法上有无注解
        Transactional annotation =AnnotationUtils.getAnnotation(method,Transactional.class);
        if(annotation==null) {//先看类上有无注解
        	Class<?> targetClass=method.getDeclaringClass();
        	annotation = AnnotationUtils.getAnnotation(targetClass,Transactional.class);
        }
        if (annotation == null) {
            return invocation.proceed();
        }
        Propagation propagation=annotation.value();
        if(Propagation.NEW==propagation) {
        	return doTransactionInvoke(invocation);
        }else if(Propagation.REQUIRED==propagation) {
        	if(TransactionUtils.isBeginTransaction()) {
        		return invocation.proceed();
        	}else {
        		return doTransactionInvoke(invocation);
        	}
        }else if(Propagation.SUPPORT==propagation) {
        	if(TransactionUtils.isSynTransaction()) {
        		return invocation.proceed();
        	}
        	return doNoTransactionInvoke(invocation);
        }
        return invocation.proceed();
	}
	/**
	 * 没有事务的处理
	 * @param invocation
	 * @return
	 * @throws Exception
	 */
	public Object doNoTransactionInvoke(Invocation invocation) throws Exception {
		try{
			TransactionUtils.startManagered(false);
			Object result= invocation.proceed();
			return result;
		}catch(Throwable e){
			throw new TransactionException(e);
		}finally{
			TransactionUtils.endManagered();
			TransactionUtils.closeAllTransactions();
		}
	}
	/**
	 * 有事务的处理
	 * @param invocation
	 * @return
	 * @throws Exception
	 */
	private Object doTransactionInvoke(Invocation invocation) throws Exception {
		try {
        	TransactionUtils.startManagered(true);
            Object result = invocation.proceed();
            TransactionUtils.commitAllTransactions();
			return result;
		}catch(Throwable e){
			TransactionUtils.rollBackAllTransactions();
			throw new TransactionException(e);
		}finally{
			TransactionUtils.endManagered();
			TransactionUtils.closeAllTransactions();
		}
	}

	@Override
	public Object plugin(Object target) {//跟据接口中是否配置有事务注解来确认是否需要代理
		Class<?> clazz=target.getClass();
		Transactional annotation=null;
		Set<Class> interfaceClasses=ClassTypeUtils.getAllInterfacesAsSet(clazz);
		for(Class cls:interfaceClasses) {
			annotation= AnnotationUtils.getAnnotation(cls,Transactional.class);
	        if(annotation==null) {//再看方法上有无注解
	        	Set<Method> methods=MethodUtils.getAllDeclaredMethods(cls);
	        	for(Method m:methods) {
	        		annotation=AnnotationUtils.getAnnotation(m,Transactional.class);
	        		if(annotation!=null) {
	        			break;
	        		}
	        	}
	        }
		}
		if(annotation!=null) {//有注解时才需要代理
			return Plugin.wrap(target, this);
		}
		return target;
	}
}
