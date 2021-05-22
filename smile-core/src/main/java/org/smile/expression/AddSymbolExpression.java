package org.smile.expression;

import java.math.BigDecimal;

import org.smile.commons.Strings;
/**
 * 加法操作符表达式
 * @author 胡真山
 *
 */
public class AddSymbolExpression extends MathSymbolExpression{
	
	public AddSymbolExpression(){
		this.operate=Strings.PLUS;
	}

	@Override
	public Object evaluate(Context root, Expression left, Expression right) {
		Object oneValue =left.evaluate(root);
		Object twoValue=right.evaluate(root);
		if (oneValue instanceof String || twoValue instanceof String) {
			return String.valueOf(oneValue)+String.valueOf(twoValue);
		}else if(oneValue instanceof Number&&twoValue instanceof Number){
			BigDecimal startVal=new BigDecimal(String.valueOf((Number) oneValue));
			BigDecimal endVal=new BigDecimal(String.valueOf((Number) twoValue));
			BigDecimal result=startVal.add(endVal);
			return convertNumber(result);
		}else if(oneValue instanceof Character || twoValue instanceof Character){
			return String.valueOf(oneValue)+String.valueOf(twoValue);
		}
		throw notSupportOperateException(oneValue, twoValue);
	}

}
