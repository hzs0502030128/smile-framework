package org.smile.function;

import java.lang.reflect.Method;

import org.smile.reflect.MethodUtils;

/**
 * 可配置的函数
 * @author 胡真山
 *
 */
public class ConfigurableFunction implements Function{
	/**函数名称*/
	private String name;
	/**此函数执行的方法,需是一个静态的方法*/
	private Method functionMethod;

	public ConfigurableFunction(String name,Method method){
		this.name=name;
		this.functionMethod=method;
	}
	
	@Override
	public Object getFunctionValue(Object... args) {
		return MethodUtils.staticInvoke(functionMethod, args);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getSupportArgsCount() {
		return functionMethod.getParameterTypes().length;
	}
}
