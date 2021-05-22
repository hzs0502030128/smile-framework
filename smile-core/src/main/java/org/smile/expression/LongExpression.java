package org.smile.expression;

import org.smile.expression.visitor.NumberExpressionVisitor;

/**
 * 长整型表达式
 * @author 胡真山
 *
 */
public class LongExpression extends NumberExpression<Long> {

	@Override
	public Long evaluate(Context root) {
		Number num=getNumberValue(root);
		if(num==null){
			return null;
		}
		return this.minus?-num.longValue():num.longValue();
	}
	
	@Override
	public void accept(NumberExpressionVisitor visitor) {
		visitor.visit(this);
	}

}
