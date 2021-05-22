package org.smile.expression;

import java.util.Set;

import org.smile.expression.Engine.FunctionKey;
import org.smile.function.Function;
import org.smile.function.FunctionWrap;
/**
 * 表达式执行内容的一个上下文
 * 用于保存变量 定义变量
 * @author 胡真山
 *
 */
public interface Context extends FunctionWrap{
	/**获取一个表达式的值*/
	public Object get(String exp);
	/**设置一个表达式的值*/
	public void set(String exp,Object value);
	/**重新设置root的值*/
	public void setRoot(Object rootObj);
	/**获取参数中获取表达式的值*/
	public Object getParameter(String param);
	/**设置参数对象*/
	public void setParameters(Object params);
	/**
	 * 设置参数内容值
	 * @param name
	 * @param value
	 */
	public void setParameter(String name,Object value);
	/**获取函数*/
	public Function getFunction(FunctionKey name);
	/**
	 * 当运行比较表达式是执行
	 * @param result 表达式运行的结果
	 * @param left 左表达式
	 * @param leftValue 左表达式值
	 * @param right 右表达式
	 * @param rightValue 右表达式的值
	 */
	public void onCompareSymbol(Object result,Expression left,Object leftValue, Expression right,Object rightValue);
	/**
	 * 是否已经绑定了引擎
	 * @return
	 */
	boolean isBinddEngin();
	/**
	 * 除法保留小数位
	 * @return
	 */
	public int divideScale();
	/**
	 * 设置除法不尽时保留位数
	 * @param scale
	 */
	public void divideScale(int scale);
	/**
	 * 是否可以执行代码
	 * @return
	 */
	public boolean canExecuteCode();
	
	/**
	 *  所有的变量的名称
	 * @return
	 */
	public Set<String> keys();
}
