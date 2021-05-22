package org.smile.expression.visitor;

import java.util.Iterator;

import org.smile.expression.BinaryExpression;
import org.smile.expression.BooleanExpression;
import org.smile.expression.CharExpression;
import org.smile.expression.ConditionExpression;
import org.smile.expression.Expression;
import org.smile.expression.MultipleExpression;
import org.smile.expression.NamedExpression;
import org.smile.expression.NullExpression;
import org.smile.expression.NumberExpression;
import org.smile.expression.SingleExpression;
import org.smile.expression.StringExpression;
import org.smile.expression.SymbolExpression;
import org.smile.expression.TrinomialExpression;

public class BaseExpressionVisitor implements ExpressionVisitor{

	@Override
	public void visit(ConditionExpression expression) {
		expression.getLeft().accept(this);;
		expression.getOprate().accept(this);
		expression.getRight().accept(this);
	}

	@Override
	public void visit(SingleExpression expression) {
		expression.getLeft().accept(this);;
	}

	@Override
	public void visit(BinaryExpression expression) {
		expression.getLeft().accept(this);;
		expression.getOprate().accept(this);
		expression.getRight().accept(this);
	}

	@Override
	public void visit(TrinomialExpression expression) {
		expression.getCondition().accept(this);
		expression.getLeft().accept(this);
		expression.getRight().accept(this);
	}

	@Override
	public void visit(MultipleExpression expression) {
		Iterator<Expression> iterator=expression.iterator();
		while(iterator.hasNext()) {
			iterator.next().accept(this);;
		}
	}

	@Override
	public void visit(NumberExpression expression) {
		
	}

	@Override
	public void visit(StringExpression expression) {
		
	}

	@Override
	public void visit(NullExpression expression) {
		
	}

	@Override
	public void visit(BooleanExpression expression) {
		
	}

	@Override
	public void visit(CharExpression expression) {
		
	}
	
	@Override
	public void visit(SymbolExpression expression) {
		
	}

	@Override
	public void visit(NamedExpression expression) {
		
	}
	
}
