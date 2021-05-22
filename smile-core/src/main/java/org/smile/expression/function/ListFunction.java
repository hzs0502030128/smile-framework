package org.smile.expression.function;

import java.util.ArrayList;
import java.util.List;

import org.smile.expression.Context;
import org.smile.expression.Expression;

public class ListFunction extends ExpressionFn {

	@Override
	public String getName() {
		return "list";
	}

	@Override
	public Object evaluate(Context context, List<Expression> expression) {
		List<Object> result = new ArrayList<Object>();
		for (int i = 0; i < expression.size(); i++) {
			Object arg = expression.get(i).evaluate(context);
			result.add(arg);
		}
		return result;
	}

}
