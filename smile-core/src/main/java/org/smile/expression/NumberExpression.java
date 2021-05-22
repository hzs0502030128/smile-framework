package org.smile.expression;

import org.smile.expression.visitor.ExpressionVisitor;
import org.smile.expression.visitor.NumberExpressionVisitor;

public class NumberExpression<T extends Number> extends SimpleExpression<T>{
	
	protected Number number;
	
	protected boolean minus;
	
	Expression numberExpression;
	
	public void setNumber(Number number){
		this.number=number;
	}
	
	@Override
	public T evaluate(Context root) {
		Number num=getNumberValue(root);
		if(num==null){
			return null;
		}
		if(num instanceof Integer){
			return (T)new Integer(this.minus?-num.intValue():num.intValue());
		}else{
			return (T)new Double(this.minus?-num.doubleValue():num.doubleValue());
		}
	}
	
	@Override
	public String toString() {
		return number.toString();
	}

	public void setMinus(boolean minus) {
		this.minus = minus;
	}

	public void setNumberExpression(Expression numberExpression) {
		this.numberExpression=numberExpression;
	}
	
	protected Number getNumberValue(Context root){
		if(number!=null){
			return number;
		}else if(this.numberExpression!=null){
			return (Number)numberExpression.evaluate(root);
		}
		return null;
	}
	
	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visit(this);
	}
	
	public void accept(NumberExpressionVisitor visitor) {
		visitor.visit(this);
	}
}
