package org.smile.expression;

import java.math.BigDecimal;

/**
 * 乘法操作符表达式
 * @author 胡真山
 *
 */
public class MultiplySymbolExpression extends MathSymbolExpression{
	
	public MultiplySymbolExpression(){
		this.operate="*";
	}
	
	@Override
	public Object evaluate(Context root, Expression left, Expression right) {
		Object oneValue =left.evaluate(root);
		Object twoValue=right.evaluate(root);
		if(oneValue instanceof Number&&twoValue instanceof Number){
			BigDecimal startVal=new BigDecimal(String.valueOf((Number) oneValue));
			BigDecimal endVal=new BigDecimal(String.valueOf((Number) twoValue));
			BigDecimal result=startVal.multiply(endVal);
			return convertNumber(result);
		}else if(oneValue instanceof Character && twoValue instanceof Character){
			return ((Character)oneValue).charValue()*((Character)twoValue).charValue();
		}
		throw notSupportOperateException(oneValue, twoValue);
	}

}
