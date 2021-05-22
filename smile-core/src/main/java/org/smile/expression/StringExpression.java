package org.smile.expression;

import org.smile.expression.visitor.ExpressionVisitor;

/**
 * 字符串表达式
 * @author 胡真山
 *
 */
public class StringExpression extends SimpleExpression<String>{
	
	protected String  value;
	@Override
	public String evaluate(Context root) {
		return value;
	}
	
	public StringExpression(String value){
		this.value=value;
	}

	@Override
	public String toString() {
		return "'"+value+"'";
	}
	
	
	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visit(this);
	}

}
