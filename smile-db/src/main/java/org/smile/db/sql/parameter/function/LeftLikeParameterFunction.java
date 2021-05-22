package org.smile.db.sql.parameter.function;

import org.smile.function.CheckArgsCountFunction;

/**
 * 左边匹配函数
 * @author 胡真山
 */
public class LeftLikeParameterFunction extends CheckArgsCountFunction{

	@Override
	public Object getFunctionValue(Object... args) {
		checkArgs(args);
		return args[0]+"%";
	}

	@Override
	public String getName() {
		return "leftlike";
	}

	@Override
	public int getSupportArgsCount() {
		return 1;
	}
	
}
