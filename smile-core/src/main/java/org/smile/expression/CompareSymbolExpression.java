package org.smile.expression;

import java.util.Date;

import org.smile.util.DateUtils;
/**
 * 比较操作符的表达式
 * @author 胡真山
 *
 */
public abstract class CompareSymbolExpression extends MathSymbolExpression{

	@Override
	public Object evaluate(Context root, Expression left, Expression right) {
		Object oneValue =left.evaluate(root);
		Object twoValue=right.evaluate(root);
		Object result=null;
		if (oneValue instanceof Number) {
			double startVal = ((Number) oneValue).doubleValue();
			if(twoValue==null){
				result= compareToNull(oneValue);
			}else{
				double endVal =convertToDouble(twoValue);
				result= compare(startVal, endVal);
			}
		}else if (oneValue instanceof Comparable) {
			Comparable startVal = (Comparable) oneValue;
			if (oneValue instanceof Date) {
				twoValue = DateUtils.convertToDate(twoValue);
			}
			if(twoValue==null){
				result= compareToNull(oneValue);
			}else{
				result= compare(startVal, twoValue);
			}
		}
		if(result!=null){
			if(root!=null){
				root.onCompareSymbol(result, left, oneValue, right, twoValue);
			}
			return result;
		}
		return compareOther(oneValue, twoValue);
	};
	/**
	 * 比较两个数字类型大小
	 * @param leftValue
	 * @param rightValue
	 * @return
	 */
	protected abstract boolean compare(double leftValue,double rightValue);
	
	protected abstract boolean compare(Comparable leftValue,Object rightValue);
	/***
	 * 与空比较
	 * @return
	 */
	protected boolean compareToNull(Object oneValue){
		throw notSupportOperateException(oneValue, null) ;
	}
	/***
	 * 其它比较
	 * @param oneValue
	 * @param twoValue
	 * @return
	 */
	protected boolean compareOther(Object oneValue,Object twoValue){
		throw notSupportOperateException(oneValue, twoValue) ;
	}
}
