package org.smile.expression.function;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.smile.collection.ArrayIterable;
import org.smile.collection.BaseMapEntry;
import org.smile.expression.Context;
import org.smile.expression.EvaluateException;
import org.smile.expression.Expression;
import org.smile.reflect.ClassTypeUtils;

public class ForeachFunction extends ExpressionFn {

	static final String ForeachVar = "it";

	@Override
	public String getName() {
		return "foreach";
	}

	@Override
	public Object evaluate(Context context, List<Expression> expression) {
		Object it = context.get(ForeachVar);
		if (expression.size() < 2) {
			throw new EvaluateException("args length must great than 1");
		}
		try {
			Object first = expression.get(0).evaluate(context);
			if (first instanceof Iterable) {
				for (Object obj : (Iterable) first) {
					context.set(ForeachVar, obj);
					for (int i = 1; i < expression.size(); i++) {
						expression.get(i).evaluate(context);
					}
				}
			} else if (first instanceof Map) {//对map进行迭代
				Iterator<Map.Entry> iter = ((Map) first).entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry item = iter.next();
					BaseMapEntry entry = new BaseMapEntry();
					entry.setKey(item.getKey());
					entry.setValue(item.getValue());
					context.set(ForeachVar, entry);
					for (int i = 1; i < expression.size(); i++) {
						expression.get(i).evaluate(context);
					}
				}
			}else if(first instanceof Object[] || ClassTypeUtils.isBasicArrayType(first.getClass())){//迭代数组
				ArrayIterable iter=new ArrayIterable(first);
				for (Object obj : iter) {
					context.set(ForeachVar, obj);
					for (int i = 1; i < expression.size(); i++) {
						expression.get(i).evaluate(context);
					}
				}
			}
		} finally {
			if (it == null) {
				context.set(ForeachVar, null);
			} else {
				context.set(ForeachVar, it);
			}
		}
		return null;
	}

}
