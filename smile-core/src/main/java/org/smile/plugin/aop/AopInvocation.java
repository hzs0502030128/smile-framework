package org.smile.plugin.aop;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.smile.commons.NotImplementedException;
import org.smile.commons.SmileRunException;
import org.smile.plugin.Invocation;

public class AopInvocation implements Invocation {

	protected MethodInvocation invocation;

	public AopInvocation(MethodInvocation invocation) {
		this.invocation = invocation;
	}

	@Override
	public Method getMethod() {
		return invocation.getMethod();
	}

	@Override
	public Object[] getArgs() {
		return invocation.getArguments();
	}

	@Override
	public <T> T getTarget() {
		return (T) invocation.getThis();
	}

	@Override
	public <T> T proceed() throws Throwable {
		return (T) invocation.proceed();
	}

	@Override
	public void setArgs(Object[] args) {
		throw new NotImplementedException("not support this method ");
	}

}
