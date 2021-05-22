package org.smile.expression.function;

import java.util.List;

import org.smile.expression.Context;
import org.smile.expression.EvaluateException;
import org.smile.expression.Expression;

public class IfElseFunction extends ExpressionFn {
	
	@Override
	public String getName() {
		return "ifelse";
	}

	@Override
	public Object evaluate(Context context, List<Expression> expression) {
		if (expression.size() < 2 || expression.size()>3) {
			throw new EvaluateException("args length must 2 or 3");
		}
		Object first = expression.get(0).evaluate(context);
		if(first instanceof Boolean){
			Boolean test=(Boolean)first;
			if(test){
				return expression.get(1).evaluate(context);
			}else if(expression.size()==3){
				return expression.get(2).evaluate(context);
			}else {
				return null;
			}
		}
		throw new EvaluateException("first arg must return boolean ");
	}

}
