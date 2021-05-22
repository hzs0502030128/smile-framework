package org.smile.expression.visitor;

import org.smile.expression.DoubleExpression;
import org.smile.expression.IntegerExpression;
import org.smile.expression.LongExpression;

public interface NumberExpressionVisitor extends ExpressionVisitor{
	
	public void visit(DoubleExpression e);
	
	public void visit(IntegerExpression e);
	
	public void visit(LongExpression e);
}
