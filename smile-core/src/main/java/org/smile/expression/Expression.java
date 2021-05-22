package org.smile.expression;

import org.smile.expression.visitor.ExpressionVisitor;

/**
 * 表达式接口
 * @author 胡真山
 *
 * @param <T>
 */
public interface Expression<T> {
	/**并且操作符*/
	public static final String AND = "AND";
	/**或者操作符*/
	public static final String OR = "OR";
	/**模糊操作符*/
	public static final String LIKE = "LIKE";
	
	public static final String IS="IS";
	/***
	 * 源文本
	 * @return
	 */
	public String getSource();
	/**
	 * 计算表达式
	 * @param root 表达式计算参数
	 * @return
	 */
	public T evaluate(Context root);
	/**
	 * 计算无变量表达式
	 * */
	public T evaluate();
	/**
	 * 接收一个访问
	 * @param visitor
	 */
	public  void accept(ExpressionVisitor visitor);
	
}
