package org.smile.expression.visitor;

import org.smile.expression.ConditionSymbolExpression;
import org.smile.expression.InSymbolExpression;
import org.smile.expression.InstanceofSymbolExpression;
import org.smile.expression.IsSymbolExpression;
import org.smile.expression.LikeSymbolExpression;
import org.smile.expression.MathSymbolExpression;

public interface SymbolExpressionVisitor extends ExpressionVisitor{
	/**
	 * 条件操作符
	 * @param e
	 */
	public void visit(ConditionSymbolExpression e);
	/**
	 * instanceof 操作符
	 * @param e
	 */
	public void visit(InstanceofSymbolExpression e);
	/**
	 * is 操作符
	 * @param e
	 */
	public void visit(IsSymbolExpression e);
	/**
	 * in 操作符
	 * @param e
	 */
	public void visit(InSymbolExpression e);
	/**
	 * like 操作符
	 * @param e
	 */
	public void visit(LikeSymbolExpression e);
	/**
	 * 数学操作符
	 * @param e
	 */
	public void visit(MathSymbolExpression e);
}
