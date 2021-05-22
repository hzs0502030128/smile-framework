package org.smile.expression;

import org.smile.commons.Strings;


/**
 * 双目运行表达式
 * 二元运算表达式
 * @author 胡真山
 *
 */
public abstract class BinaryExpression<T> extends  SingleExpression<T>{

	/**右边表达式*/
	protected Expression right;
	/**
	 * 比较、操作符
	 */
	protected SymbolExpression operate;

	public BinaryExpression(SymbolExpression operate) {
		this.operate=operate;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		if(not){
			str.append(" NOT ");
		}
		str.append("(");
		append(left,str);
		str.append(Strings.SPACE).append(operate).append(Strings.SPACE);
		append(right,str);
		str.append(")");
		return str.toString();
	}

	public Expression getRight() {
		return right;
	}

	public void setRight(Expression right) {
		this.right = right;
	}

	public SymbolExpression getOprate() {
		return operate;
	}

	public void setOperate(SymbolExpression oprate) {
		this.operate = oprate;
	}
	
	
}
