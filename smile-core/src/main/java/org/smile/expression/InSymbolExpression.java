package org.smile.expression;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;

import org.smile.collection.ArrayUtils;
import org.smile.expression.visitor.ExpressionVisitor;
import org.smile.expression.visitor.SymbolExpressionVisitor;
import org.smile.reflect.ClassTypeUtils;


/**
 * IS操作符
 * @author 胡真山
 *
 */
public class InSymbolExpression extends SymbolExpression {
	
	public InSymbolExpression(String operate){
		this.operate=operate;
	}
	
	public InSymbolExpression() {
		this.operate="IN";
	}

	@Override
	public Object evaluate(Context root, Expression left, Expression right) {
		Object oneValue =left.evaluate(root);
		Object twoValue=right.evaluate(root);
		if(twoValue instanceof Collection) {
			return ((Collection) twoValue).contains(oneValue);
		}else if(twoValue instanceof Object[]) {
			return ArrayUtils.arrayContains((Object[])twoValue, oneValue);
		}else if(ClassTypeUtils.isBasicArrayType(twoValue.getClass())) {
			int len=Array.getLength(twoValue);
			for(int i=0;i<len;i++) {
				Object v=Array.get(twoValue, i);
				if(v.equals(oneValue)) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public void accept(SymbolExpressionVisitor visitor) {
		visitor.visit(this);
	}
}
