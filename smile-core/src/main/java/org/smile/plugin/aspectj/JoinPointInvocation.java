package org.smile.plugin.aspectj;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.smile.plugin.Invocation;

public class JoinPointInvocation implements Invocation {

	private ProceedingJoinPoint joinPoint;
	
	private Object[] args;
	
	public JoinPointInvocation(ProceedingJoinPoint joinPoint){
		this.joinPoint=joinPoint;
		this.args=joinPoint.getArgs();
	}
	@Override
	public Method getMethod() {
		Signature signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();
		return method;
	}

	@Override
	public Object[] getArgs() {
		return joinPoint.getArgs();
	}

	@Override
	public <T> T getTarget() {
		return (T)joinPoint.getTarget();
	}

	@Override
	public <T> T proceed() throws Throwable {
		return (T)joinPoint.proceed(args);
	}

	@Override
	public void setArgs(Object[] args) {
		this.args=args;
	}

}
