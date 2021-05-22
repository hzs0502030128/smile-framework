package org.smile.function;

import java.util.Collection;

/**
 * 支持函数注册
 * @author 胡真山
 *
 */
public interface FunctionWrap {
	/**
	 * 注册函数
	 * @param f
	 */
	public void registFunction(final Function f); 
	/**
	 * 注册多个函数
	 * @param fs
	 */
	public void registFunctions(Collection<Function> fs);
	
}
