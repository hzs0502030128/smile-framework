package org.smile.db.function;

import org.smile.log.LoggerHandler;

public interface FunctionConverter extends LoggerHandler{
	/**
	 * 转换函数
	 * */
	public  void convert(SqlFunction f);
	
	
}
