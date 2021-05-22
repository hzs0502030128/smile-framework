package org.smile.plugin.aop;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.smile.annotation.AnnotationUtils;
import org.smile.commons.ann.JoinMethod;
import org.smile.reflect.ClassTypeUtils;

public class AopJionMethodInterceptor implements MethodInterceptor{
	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		Method method=methodInvocation.getMethod();
		JoinMethod join=AnnotationUtils.getAnnotation(method,JoinMethod.class);
		if(join!=null){
			org.smile.plugin.MethodInterceptor interceptor=ClassTypeUtils.newInstance(join.type());
			return interceptor.intercept(new AopInvocation(methodInvocation));
		}else{
			return methodInvocation.proceed();
		}
	}
}
