package org.smile.expression.function;

import java.lang.reflect.Constructor;
import java.util.List;

import org.smile.expression.Context;
import org.smile.expression.EvaluateException;
import org.smile.expression.Expression;
import org.smile.expression.FieldNameExpression;
import org.smile.expression.FunctionExpression;
import org.smile.expression.StringExpression;
import org.smile.reflect.ClassTypeUtils;
/**
 * 创建一个对象
 * @author 胡真山
 * new(className,arg1,arg2...)
 */
public class NewFunction extends ExpressionFn {

	@Override
	public String getName() {
		return "new";
	}

	@Override
	public Object evaluate(Context context, List<Expression> expression) {
		if(expression.size()<1){
			throw new EvaluateException("args must not empty");
		}
		Expression first=expression.get(0);
		String className=null;
		if(first instanceof FieldNameExpression){
			Object res=first.evaluate(context);
			if(res==null){
				className=((FieldNameExpression) first).getName();
			}else{
				if(res instanceof String){
					className=(String)res;
				}else{
					throw new EvaluateException("first args must is class name");
				}
			}
		}else if(first instanceof StringExpression||first instanceof FunctionExpression){
			className=(String)first.evaluate(context);
		}else{
			throw new EvaluateException("first args must is class name");
		}
		Class instanceClass;
		try {
			instanceClass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new EvaluateException(className,e);
		}
		Object[] params=new Object[expression.size()-1];
		for (int i = 1; i < expression.size(); i++) {
			Object arg = expression.get(i).evaluate(context);
			params[i-1]=arg;
		}
		Constructor constructor=ClassTypeUtils.getConstructors(instanceClass, params);
		if(constructor==null){
			throw new EvaluateException("there is no suitable constructor of class "+instanceClass);
		}
		
		try {
			return constructor.newInstance(params);
		} catch (Exception e) {
			throw new EvaluateException(this.toString(),e);
		}
	}

}
