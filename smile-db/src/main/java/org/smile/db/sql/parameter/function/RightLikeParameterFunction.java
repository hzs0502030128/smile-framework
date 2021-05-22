package org.smile.db.sql.parameter.function;

import org.smile.function.CheckArgsCountFunction;

/**
 * 右边匹配函数
 * @author 胡真山
 *
 */
public class RightLikeParameterFunction extends CheckArgsCountFunction{

	@Override
	public Object getFunctionValue(Object... args) {
		checkArgs(args);
		return "%"+args[0];
	}

	@Override
	public String getName() {
		return "rightlike";
	}

	@Override
	public int getSupportArgsCount() {
		return 1;
	}

}
