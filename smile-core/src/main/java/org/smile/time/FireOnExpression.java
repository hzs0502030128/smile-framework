package org.smile.time;

import java.util.Date;

public interface FireOnExpression {

	/**
	 * 判断一个时间是不是在当前表达式之内
	 * @param times 要判断的时间
	 * @return
	 */
	boolean isValidTime(long times);

	/**
	 * 获取此时间后的下一次合符表达式的时间
	 * @param times 从此时间这后开始查找
	 * @return
	 */
	Date getNextValidTimeAfter(long times);

}