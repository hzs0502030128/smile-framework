package org.smile.expression;

import org.smile.expression.visitor.NumberExpressionVisitor;

/**
 * 双精度类型表达式
 * @author 胡真山
 *
 */
public class DoubleExpression extends NumberExpression<Double>{

	@Override
	public Double evaluate(Context root) {
		Number num=getNumberValue(root);
		if(num==null){
			return null;
		}
		return this.minus?-num.doubleValue():num.doubleValue();
	}
	@Override
	public void accept(NumberExpressionVisitor visitor) {
		visitor.visit(this);
	}

}
