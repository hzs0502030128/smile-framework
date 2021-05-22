package org.smile.expression.function;

import java.util.List;

import org.smile.expression.Context;
import org.smile.expression.EvaluateException;
import org.smile.expression.Expression;

public class WhileFunction extends ExpressionFn {
	
	@Override
	public String getName() {
		return "while";
	}

	@Override
	public Object evaluate(Context context, List<Expression> expression) {
		if (expression.size() < 2) {
			throw new EvaluateException("args length must great than 1");
		}
		Object first = expression.get(0).evaluate(context);
		if(first instanceof Boolean){
			Boolean test=(Boolean)first;
			while(test){
				for (int i = 1; i < expression.size(); i++) {
					expression.get(i).evaluate(context);
				}
				test=(Boolean)expression.get(0).evaluate(context);
			}
		}
		return null;
	}

}
