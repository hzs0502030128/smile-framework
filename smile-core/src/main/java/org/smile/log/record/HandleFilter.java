package org.smile.log.record;

public interface HandleFilter {
	/**
	 * 是否需要处理日志
	 * @param record
	 * @return
	 */
	public boolean isLoggable(LogRecord record);
}
