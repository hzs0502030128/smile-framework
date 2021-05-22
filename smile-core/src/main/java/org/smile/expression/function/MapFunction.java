package org.smile.expression.function;

import java.util.List;
import java.util.Map;

import org.smile.collection.LinkedHashMap;
import org.smile.expression.Context;
import org.smile.expression.EvaluateException;
import org.smile.expression.Expression;

public class MapFunction extends ExpressionFn {

	@Override
	public String getName() {
		return "map";
	}

	@Override
	public Object evaluate(Context context, List<Expression> expression) {
		Map result=new LinkedHashMap();
		if(expression.size()%2!=0){
			throw new EvaluateException("args count must be a digit of 2 ");
		}
		for (int i = 0; i < expression.size(); i=i+2) {
			Object key = expression.get(i).evaluate(context);
			Object value = expression.get(i+1).evaluate(context);
			result.put(key, value);
		}
		return result;
	}

}
