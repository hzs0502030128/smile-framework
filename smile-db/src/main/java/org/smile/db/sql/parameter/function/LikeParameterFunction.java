package org.smile.db.sql.parameter.function;

import org.smile.function.CheckArgsCountFunction;

public class LikeParameterFunction extends CheckArgsCountFunction{
	
	@Override
	public String getName() {
		return "like";
	}

	@Override
	public Object getFunctionValue(Object... args) {
		checkArgs(args);
		return  "%"+args[0]+"%";
	}

	@Override
	public int getSupportArgsCount() {
		return 1;
	}

}
