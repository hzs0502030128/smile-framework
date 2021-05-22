package org.smile.expression;

import org.smile.expression.visitor.NamedExpressionVisitor;

/**
 *	系统关键字表达式
 * @author 胡真山
 *
 */
public class SystemWordExpression extends NamedExpression{

	@Override
	public Object evaluate(Context root) {
		return name;
	}
	
	@Override
	public void accept(NamedExpressionVisitor visitor) {
		visitor.visit(this);
	}

}
