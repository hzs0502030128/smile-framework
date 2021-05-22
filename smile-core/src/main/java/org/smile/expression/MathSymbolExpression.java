package org.smile.expression;

import java.math.BigDecimal;

import org.smile.expression.visitor.SymbolExpressionVisitor;
import org.smile.math.NumberUtils;
import org.smile.util.StringUtils;

public class MathSymbolExpression extends SymbolExpression {
	/**供继承*/
	protected MathSymbolExpression(){}
	
	public MathSymbolExpression(String operate){
		this.operate=operate;
	}

	@Override
	public Object evaluate(Context root, Expression left, Expression right) {
		Object oneValue =left.evaluate(root);
		Object twoValue=right.evaluate(root);
		String op=operate;
		if (oneValue instanceof Number&&twoValue instanceof Number) {
			BigDecimal startVal=new BigDecimal(String.valueOf((Number) oneValue));
			BigDecimal endVal=new BigDecimal(String.valueOf((Number) twoValue));
			if (op.equals("-")) {
				return startVal.subtract(endVal);
			} else if (op.equals("+")) {
				return startVal.add(endVal);
			} else if (op.equals("*")) {
				return startVal.multiply(endVal);
			} else if (op.equals("/")) {
				try{
					return startVal.divide(endVal);
				}catch(ArithmeticException e){
					BigDecimal result=startVal.divide(endVal,8,BigDecimal.ROUND_HALF_UP);
					return new BigDecimal(NumberUtils.format(result,"#.########"));
				}
			} else if (op.equals("%")) {
				return startVal.divideAndRemainder(endVal)[1];
			}else if(op.equals("^")){
				return startVal.intValue()^endVal.intValue();
			}else if(op.equals("&")){
				return startVal.intValue()&endVal.intValue();
			}else if(op.equals("|")){
				return startVal.intValue()|endVal.intValue();
			}
		}else if (oneValue instanceof String || twoValue instanceof String) {
			if (op.equals("+")) {
				return String.valueOf(oneValue)+String.valueOf(twoValue);
			} else if (op.equals("-")) {
				return StringUtils.remove(String.valueOf(oneValue), String.valueOf(twoValue));
			}
		}else if(oneValue instanceof Boolean || twoValue instanceof Boolean){
			if(op.equals("^")){
				return (Boolean)oneValue^(Boolean)twoValue;
			}else if(op.equals("&")){
				return (Boolean)oneValue&(Boolean)twoValue;
			}else if(op.equals("|")){
				return (Boolean)oneValue|(Boolean)twoValue;
			}
		}else if(oneValue instanceof Character || twoValue instanceof Character){
			if (op.equals("+")) {
				return String.valueOf(oneValue)+String.valueOf(twoValue);
			}else if(op.equals("^")){
				return ((Character)oneValue).charValue()^((Character)twoValue).charValue();
			}else if(op.equals("&")){
				return ((Character)oneValue).charValue()&((Character)twoValue).charValue();
			}else if(op.equals("|")){
				return ((Character)oneValue).charValue()|((Character)twoValue).charValue();
			}
		}
		throw new EvaluateException("the condition value must type of  comparable 1{"+left+"="+oneValue+"},2{operat="+operate+"},3{"+right+"="+twoValue+"}");
	}
	
	/**
	 * 转成数字类型
	 * @param value
	 * @return
	 */
	protected double convertToDouble(Object value){
		if(value instanceof Number){
			return ((Number) value).doubleValue();
		}else if(value instanceof String){
			return Double.valueOf((String)value);
		}else{
			return Double.valueOf(value.toString());
		}
	}
	/**
	 * 确认数字类型的返回类型
	 * @param value
	 * @return
	 */
	protected Object convertNumber(Number value){
		Number number=(Number)value;
		if(number.longValue()==number.intValue()){
			if(number.intValue()==number.doubleValue()){
				return number.intValue();
			}
			return number.doubleValue();
		}else{
			return number.longValue();
		}
	}
	
	protected EvaluateException notSupportOperateException(Object leftValue,Object rightValue){
		return new EvaluateException("not support "+this.operate+" between "+leftValue+" and "+rightValue);
	}
	
	@Override
	public void accept(SymbolExpressionVisitor visitor) {
		visitor.visit(this);
	}
}
