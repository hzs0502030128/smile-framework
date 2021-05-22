package org.smile.expression;

import org.smile.expression.visitor.ExpressionVisitor;

/**
 * boolean型表达式
 * @author 胡真山
 *
 */
public class BooleanExpression extends SimpleExpression<Boolean>{
	
	protected Boolean value;
	
	public BooleanExpression(Boolean value){
		this.value=value;
	}
	
	@Override
	public Boolean evaluate(Context root) {
		return not^value;
	}

	@Override
	public String toString() {
		if(not){
			return "NOT "+String.valueOf(value);
		}
		return String.valueOf(value);
	}
	
	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visit(this);
	}
	
}
