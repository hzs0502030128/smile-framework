package org.smile.expression.visitor;

import org.smile.expression.BinaryExpression;
import org.smile.expression.BooleanExpression;
import org.smile.expression.CharExpression;
import org.smile.expression.ConditionExpression;
import org.smile.expression.MultipleExpression;
import org.smile.expression.NamedExpression;
import org.smile.expression.NullExpression;
import org.smile.expression.NumberExpression;
import org.smile.expression.SingleExpression;
import org.smile.expression.StringExpression;
import org.smile.expression.SymbolExpression;
import org.smile.expression.TrinomialExpression;

public interface ExpressionVisitor {
	/**
	 * 访问运算符表达式
	 * @param expression
	 */
	public void visit(SymbolExpression expression);
	/***
	 * 访问条件表达式
	 * @param expression
	 */
	public void visit(ConditionExpression expression);
	/**
	 * 访问三目表达式
	 * @param expression
	 */
	public void visit(TrinomialExpression expression);
	/**
	 * 多表达式集合表达式
	 * @param expression
	 */
	public void visit(MultipleExpression expression);
	/**
	 * 访问数字表达式
	 * @param expression
	 */
	public void visit(NumberExpression expression);
	/**
	 * 访问字符串表达式
	 * @param expression
	 */
	public void visit(StringExpression expression);
	/**
	 * 访问null表达式
	 * @param expression
	 */
	public void visit(NullExpression expression);
	/**
	 * 访问boolean表达式
	 * @param expression
	 */
	public void visit(BooleanExpression expression);
	/**
	 * 访问字符表达式
	 * @param expression
	 */
	public void visit(CharExpression expression);
	/**
	 * 访问单目表达式
	 * @param expression
	 */
	public void visit(SingleExpression expression);
	/**
	 * 访问双目表达式
	 * @param expression
	 */
	public void visit(BinaryExpression expression);
	/***
	 * 
	 * @param expression
	 */
	public void visit(NamedExpression expression);
}
