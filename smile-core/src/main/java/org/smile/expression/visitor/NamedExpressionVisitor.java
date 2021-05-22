package org.smile.expression.visitor;

import org.smile.expression.FieldNameExpression;
import org.smile.expression.FunctionExpression;
import org.smile.expression.ParameterNameExpression;
import org.smile.expression.SystemWordExpression;

public interface NamedExpressionVisitor extends ExpressionVisitor{
	/**
	 *访问一个字段属性表达式
	 * @param e
	 */
	public void visit(FieldNameExpression e);
	/**
	 * 函数表达式
	 * @param e
	 */
	public void visit(FunctionExpression e);
	/**
	 * 参数表达式
	 * @param e
	 */
	public void visit(ParameterNameExpression e);
	/**
	 * 系统常量表达式
	 * @param e
	 */
	public void visit(SystemWordExpression e);
}
