package org.smile.log.record;
/**
 * 日志记录格式化处理类接口
 * @author 胡真山
 *
 */
public interface RecordFormatter {
	/**
	 * 格式化日志信息为一个输出的字符串
	 * @param record
	 * @return
	 */
	public String format(LogRecord record);
	/**
	 * 设置格式化模板
	 * @param pattern
	 */
	public void setPattern(String pattern);
}
