package org.smile.expression;

import org.smile.expression.visitor.ExpressionVisitor;
import org.smile.expression.visitor.SymbolExpressionVisitor;

/**
 * IS操作符
 * @author 胡真山
 *
 */
public class IsSymbolExpression extends SymbolExpression {
	
	public IsSymbolExpression(String operate){
		this.operate=operate;
	}

	@Override
	public Object evaluate(Context root, Expression left, Expression right) {
		Object oneValue =left.evaluate(root);
		Object twoValue=right.evaluate(root);
		if(oneValue==null){
			return twoValue==null;
		}else{
			return oneValue.equals(twoValue);
		}
	}
	
	@Override
	public void accept(SymbolExpressionVisitor visitor) {
		visitor.visit(this);
	}
}
