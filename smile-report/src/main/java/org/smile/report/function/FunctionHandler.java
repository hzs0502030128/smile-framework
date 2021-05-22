package org.smile.report.function;

import org.smile.function.BaseFunctionInfo;


public interface FunctionHandler {
	/**
	 * 获取函数
	 * @param exp
	 * @return
	 */
	public IFunction getFunction(BaseFunctionInfo fun);
	/**
	 * 获取函数中的表达式
	 * @param exp
	 * @return
	 */
	public BaseFunctionInfo getFunctionInfo(String exp);
	/**
	 * 注册函数
	 * @param name
	 * @param function
	 */
	public <E extends IFunction> void registerFunction(String name,E function);
	/**
	 * 是否是函数表达式
	 * @param fun
	 * @return
	 */
	public boolean isFunction(BaseFunctionInfo fun);
}
