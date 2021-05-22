package org.smile.report.function;


public abstract  class AbstractFunction implements IFunction{

	/**
	 * 是否需要字段的值
	 * 当返回false时 convert方法中expValue是不会有值的
	 * 返回true时convert方法中expValue会根据exp表达式的值从oneData中获取值
	 * @return
	 */
	@Override
	public boolean needFieldValue(){
		return true;
	}
	@Override
	public boolean needContext(){
		return false;
	}

}
