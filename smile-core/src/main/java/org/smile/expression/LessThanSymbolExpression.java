package org.smile.expression;


/**
 * 小于操作符表达式
 * @author 胡真山
 *
 */
public class LessThanSymbolExpression extends CompareSymbolExpression{
	
	public LessThanSymbolExpression(){
		this.operate="<";
	}

	@Override
	protected boolean compare(double leftValue, double rightValue) {
		return leftValue<rightValue;
	}

	@Override
	protected boolean compare(Comparable leftValue, Object rightValue) {
		return leftValue.compareTo(rightValue)<0;
	}

}
