package org.smile.function;

public interface FunctionAware<T> {
	/**函数名称*/
	public String getName();
	/**参数表达式
	 * */
	public T[] getArgExpression();
}
