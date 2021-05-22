package org.smile.report.function;

public interface IFunction {
	/**
	 * 转换对象值
	 * @param oneData 行对象
	 * @param exp 表达式
	 * @param expValue 字段值
	 * @return
	 */
	public Object convert(Object oneData,String exp,Object expValue);
	/**
	 * 是否需要字段的值
	 * 当返回false时 convert方法中expValue是不会有值的
	 * 返回true时convert方法中expValue会根据exp表达式的值从oneData中获取值
	 * @return
	 */
	public boolean needFieldValue();
	
	boolean needContext();
}
