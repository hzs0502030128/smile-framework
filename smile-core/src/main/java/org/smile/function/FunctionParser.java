package org.smile.function;

public interface FunctionParser {
	/**
	 * 解析表达式
	 * @param exp
	 * @return
	 */
	public BaseFunctionInfo parse(String exp);
}
