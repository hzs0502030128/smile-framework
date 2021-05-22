package org.smile.expression;

import java.math.BigDecimal;
/**
 * 除法操作符表达式
 * @author 胡真山
 *
 */
public class DivisionSymbolExpression extends MathSymbolExpression{
	public DivisionSymbolExpression(){
		this.operate="/";
	}
	
	@Override
	public Object evaluate(Context root, Expression left, Expression right) {
		Object oneValue =left.evaluate(root);
		Object twoValue=right.evaluate(root);
		if(oneValue instanceof Number&&twoValue instanceof Number){
			BigDecimal startVal=new BigDecimal(String.valueOf((Number) oneValue));
			BigDecimal endVal=new BigDecimal(String.valueOf((Number) twoValue));
			BigDecimal result;
			try{
				result= startVal.divide(endVal);
			}catch(ArithmeticException e){
				//除不尽时保留位数
				result=startVal.divide(endVal,root==null?8:root.divideScale(),BigDecimal.ROUND_HALF_UP);
			}
			return convertNumber(result);
		}else if(oneValue instanceof Character && twoValue instanceof Character){
			return ((Character)oneValue).charValue()/((Character)twoValue).charValue();
		}
		throw notSupportOperateException(oneValue, twoValue);
	}

}
