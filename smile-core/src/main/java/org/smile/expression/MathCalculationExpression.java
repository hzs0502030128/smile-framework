package org.smile.expression;

import org.smile.expression.visitor.ExpressionVisitor;

/***
 * 数学计算表达式
 * @author 胡真山
 *
 */
public class MathCalculationExpression extends BinaryExpression<Object>{

	public MathCalculationExpression(SymbolExpression oprate) {
		super(oprate);
	}
	
	@Override
	public Object evaluate(Context root) {
		return operate.evaluate(root, left, right);
	}

	public void setLeft(Expression left) {
		this.left = left;
	}

	public void setRight(Expression right) {
		this.right = right;
	}
	
	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visit(this);
	}
}
