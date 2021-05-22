package org.smile.plugin;

public class CglibBaseInterceptor extends BaseInterceptor{

	@Override
	public Object plugin(Object target) {
		return CglibPlugin.wrap(target, this);
	}
	
}
