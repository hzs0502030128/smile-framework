package org.smile.log.impl;

import org.smile.log.Logger.Level;
/**
 * 用于配置一个日志输入信息
 * @author 胡真山
 *
 */
public class LogConfig {
	/**
	 * 处理日志的handler
	 */
	protected Class handler;
	/**
	 * 输出日志格式化
	 */
	protected Class formatter;
	/**
	 * 日志级别
	 */
	protected Level level;
	/**
	 * 日志文件的名称样式
	 */
	protected String pattern;
	/**
	 * 日志格式化模板
	 */
	protected String formatPattern;

	public Class getHandler() {
		return handler;
	}

	public void setHandler(Class handler) {
		this.handler = handler;
	}

	public Class getFormatter() {
		return formatter;
	}

	public void setFormatter(Class formatter) {
		this.formatter = formatter;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getFormatPattern() {
		return formatPattern;
	}

	public void setFormatPattern(String formatPattern) {
		this.formatPattern = formatPattern;
	}
	
	
}
