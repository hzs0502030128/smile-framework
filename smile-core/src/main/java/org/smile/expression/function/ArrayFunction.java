package org.smile.expression.function;

import java.util.List;

import org.smile.expression.Context;
import org.smile.expression.Expression;

public class ArrayFunction extends ExpressionFn {

	@Override
	public String getName() {
		return "array";
	}

	@Override
	public Object evaluate(Context context, List<Expression> expression) {
		Object[] result = new Object[expression.size()];
		for (int i = 0; i < expression.size(); i++) {
			Object arg = expression.get(i).evaluate(context);
			result[i]=arg;
		}
		return result;
	}

}
