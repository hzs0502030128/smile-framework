package org.smile.expression;

import org.smile.expression.visitor.ExpressionVisitor;
import org.smile.expression.visitor.SymbolExpressionVisitor;

/**
 * 运算符表达式
 * @author 胡真山
 *
 */
public abstract class SymbolExpression extends SimpleExpression<String>{
	/**
	 * 比较、操作符
	 */
	protected String operate;
	
	@Override
	public String evaluate(Context root) {
		return operate;
	}
	
	public String getName(){
		return operate;
	}

	@Override
	public String toString() {
		return operate ;
	}
	/**操作两个表达式的结果*/
	public abstract Object evaluate(Context root,Expression left,Expression right);

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visit(this);
	}
	
	public abstract void accept(SymbolExpressionVisitor visitor);
	
}
