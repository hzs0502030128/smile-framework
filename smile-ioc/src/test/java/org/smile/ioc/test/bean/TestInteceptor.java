package org.smile.ioc.test.bean;

import org.smile.commons.ann.Intercept;
import org.smile.plugin.Interceptor;
import org.smile.plugin.Invocation;
import org.smile.plugin.Plugin;
@Intercept
public class TestInteceptor implements Interceptor{
	
	String name;
	public TestInteceptor(String name) {
		this.name=name;
	}
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		return name+","+invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

}
