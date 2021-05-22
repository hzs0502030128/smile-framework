package org.smile.expression;

import org.smile.expression.visitor.ExpressionVisitor;

/**
 * 字符表达式
 * @author 胡真山
 *
 */
public class CharExpression extends SimpleExpression<Character>{
	
	protected Character value;
	@Override
	public Character evaluate(Context root) {
		return value;
	}
	
	public CharExpression(Character value){
		this.value=value;
	}
	
	public CharExpression(String value){
		if(value.length()>0){
			this.value=value.charAt(0);
		}
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
