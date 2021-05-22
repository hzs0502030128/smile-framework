package org.smile.expression;


/**
 * 单目运算表达式
 * @author 胡真山
 *
 * @param <T>
 */
public abstract class SingleExpression<T> extends SimpleExpression<T>{
	/**第一个参数*/
	protected Expression left;

	public SingleExpression(Expression one) {
		this.left = one;
	}
	
	public SingleExpression(){}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		if(not){
			str.append(" NOT ");
		}
		append(left,str);
		return str.toString();
	}

	protected void append(Object obj, StringBuilder str) {
		if (obj instanceof String) {
			str.append("'").append(obj).append("'");
		}else{
			str.append(obj);
		}
	}

	public Expression getLeft() {
		return left;
	}

	public void setLeft(Expression left) {
		this.left = left;
	}
	
	
}
