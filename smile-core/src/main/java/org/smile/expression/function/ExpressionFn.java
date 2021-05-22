package org.smile.expression.function;

import java.util.List;

import org.smile.commons.NotImplementedException;
import org.smile.expression.Context;
import org.smile.expression.Expression;
import org.smile.function.CheckArgsCountFunction;

public abstract class ExpressionFn extends CheckArgsCountFunction{

	@Override
	public Object getFunctionValue(Object... args) {
		throw new NotImplementedException("not support this method");
	}
	
	public abstract Object evaluate(Context context,List<Expression> expression);

}
