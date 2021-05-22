package org.smile.expression;

import org.smile.expression.visitor.NumberExpressionVisitor;

/**
 * 整型表达式
 * @author 胡真山
 *
 */
public class IntegerExpression extends NumberExpression<Integer> {

	@Override
	public Integer evaluate(Context root) {
		Number num=getNumberValue(root);
		if(num==null){
			return null;
		}
		return this.minus?-num.intValue():num.intValue();
	}
	@Override
	public void accept(NumberExpressionVisitor visitor) {
		visitor.visit(this);
	}
}
