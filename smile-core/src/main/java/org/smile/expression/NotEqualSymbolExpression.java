package org.smile.expression;


/**
 * 等于操作符表达式
 * @author 胡真山
 *
 */
public class NotEqualSymbolExpression extends CompareSymbolExpression{
	
	public NotEqualSymbolExpression(){
		this.operate="!=";
	}
	

	@Override
	protected boolean compare(double leftValue, double rightValue) {
		return leftValue!=rightValue;
	}

	@Override
	protected boolean compare(Comparable leftValue, Object rightValue) {
		return leftValue.compareTo(rightValue)!=0;
	}


	@Override
	protected boolean compareOther(Object oneValue, Object twoValue) {
		return oneValue!=twoValue;
	}


	@Override
	protected boolean compareToNull(Object oneValue) {
		return oneValue!=null;
	}
	
	

}
