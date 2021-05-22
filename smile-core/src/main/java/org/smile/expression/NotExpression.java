package org.smile.expression;

import org.smile.expression.visitor.ExpressionVisitor;

public class NotExpression extends SingleExpression<Object>{

	public NotExpression(Expression one) {
		super(one);
		this.not=true;
	}

	@Override
	public Object evaluate(Context root) {
		Object result=left.evaluate(root);
		if(result instanceof Boolean){
			return not^(Boolean)result;
		}
		if(not){
			return result!=null;
		}
		return result;
	}
	
	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visit(this);
	}
}
