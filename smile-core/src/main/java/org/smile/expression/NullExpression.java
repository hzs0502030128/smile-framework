package org.smile.expression;

import org.smile.commons.Strings;
import org.smile.expression.visitor.ExpressionVisitor;

public class NullExpression extends SimpleExpression<Object>{

	@Override
	public Object evaluate(Context root) {
		return null;
	}

	@Override
	public String toString() {
		return (not?"NOT ":"")+Strings.NULL;
	}
	
	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visit(this);
	}

}
