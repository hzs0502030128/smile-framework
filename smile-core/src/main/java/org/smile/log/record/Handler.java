package org.smile.log.record;

import org.smile.log.Logger.Level;

public interface Handler {
	/**
	 * 处理日志
	 * @param record
	 */
	public void handle(LogRecord record,Throwable throwable);
	/**
	 * 设置过滤器
	 * @param filter
	 */
	public void setFilter(HandleFilter filter);
	/**
	 * 设置格式化对象
	 * @param formatter
	 */
	public void setFormatter(RecordFormatter formatter);
	/**
	 * 设置处理级别
	 * @param level
	 */
	public void setLevel(Level level);
	
}
