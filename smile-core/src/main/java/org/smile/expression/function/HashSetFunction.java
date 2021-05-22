package org.smile.expression.function;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.smile.expression.Context;
import org.smile.expression.Expression;

public class HashSetFunction extends ExpressionFn {

	@Override
	public String getName() {
		return "hashset";
	}

	@Override
	public Object evaluate(Context context, List<Expression> expression) {
		Set<Object> result = new HashSet<Object>();
		for (int i = 0; i < expression.size(); i++) {
			Object arg = expression.get(i).evaluate(context);
			result.add(arg);
		}
		return result;
	}

}
