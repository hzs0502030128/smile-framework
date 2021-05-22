package org.smile.log.impl;

import java.util.HashMap;
import java.util.Map;

import org.smile.commons.Strings;
import org.smile.log.Logger;

/**
 * JDK logger.
 * 配置样例  logger.properties 文件 
	level=debug
	logger.consle.handler=java.util.logging.ConsoleHandler
	logger.consle.level=debug
	#logger.consle.formatter=java.util.logging.SimpleFormatter
	logger.consle.formatter=org.smile.log.record.JDKRecordFormatter

	logger.file.handler=java.util.logging.FileHandler
	logger.file.pattern = ${user.dir}/java%g%u.log
	logger.file.level=debug
	logger.file.formatter=java.util.logging.XMLFormatter
	org.jj=debug
	test=info
 */
public class JDKLogger implements Logger{

	protected final java.util.logging.Logger logger;
	
	private final static Map<java.util.logging.Level,Level> jdkLevels=new HashMap<java.util.logging.Level,Level>();
	
	static{
		jdkLevels.put(java.util.logging.Level.FINER, Level.TRACE);
		jdkLevels.put(java.util.logging.Level.FINE, Level.DEBUG);
		jdkLevels.put(java.util.logging.Level.INFO, Level.INFO);
		jdkLevels.put(java.util.logging.Level.WARNING, Level.WARN);
		jdkLevels.put(java.util.logging.Level.SEVERE, Level.ERROR);
	}

	public JDKLogger(java.util.logging.Logger logger) {
		this.logger = logger;
	}

	/**
	 * Converts Jodd logging level to JDK.
	 */
	public static java.util.logging.Level toJdkLvl(Level level) {
		switch (level) {
			case TRACE: return java.util.logging.Level.FINER;
			case DEBUG: return java.util.logging.Level.FINE;
			case INFO:	return java.util.logging.Level.INFO;
			case WARN:	return java.util.logging.Level.WARNING;
			case ERROR:	return java.util.logging.Level.SEVERE;
			default:
				throw new IllegalArgumentException();
		}
	}
	/**
	 * jdk level 转换
	 * @param level
	 * @return
	 */
	public static Level parseJdkLvl(java.util.logging.Level level) {
		Level lvl= jdkLevels.get(level);
		if(lvl==null){
			return Level.TRACE;
		}
		return lvl;
	}

	public String getName() {
		return logger.getName();
	}

	public boolean isEnabled(Level level) {
		return logger.isLoggable(toJdkLvl(level));
	}

	public void log(Level level, Object message) {
		logger.log(toJdkLvl(level), message==null?Strings.NULL:message.toString());
	}

	public boolean isTraceEnabled() {
		return logger.isLoggable(java.util.logging.Level.FINER);
	}

	public void trace(Object message) {
		logger.log(java.util.logging.Level.FINER, message==null?Strings.NULL:message.toString());
	}

	public boolean isDebugEnabled() {
		return logger.isLoggable(java.util.logging.Level.FINE);
	}

	public void debug(Object message) {
		logger.log(java.util.logging.Level.FINE, message==null?Strings.NULL:message.toString());
	}

	public boolean isInfoEnabled() {
		return logger.isLoggable(java.util.logging.Level.INFO);
	}

	public void info(Object message) {
		logger.log(java.util.logging.Level.INFO, message==null?Strings.NULL:message.toString());
	}
	
	@Override
	public void info(String message, Throwable throwable) {
		logger.log(java.util.logging.Level.INFO,message, throwable);
	}

	public boolean isWarnEnabled() {
		return logger.isLoggable(java.util.logging.Level.WARNING);
	}

	public void warn(Object message) {
		logger.log(java.util.logging.Level.WARNING, message==null?Strings.NULL:message.toString());
	}

	public void warn(String message, Throwable throwable) {
		logger.log(java.util.logging.Level.WARNING, message, throwable);
	}

	public boolean isErrorEnabled() {
		return logger.isLoggable(java.util.logging.Level.SEVERE);
	}

	public void error(Object message) {
		logger.log(java.util.logging.Level.SEVERE, message==null?Strings.NULL:message.toString());
	}

	public void error(String message, Throwable throwable) {
		logger.log(java.util.logging.Level.SEVERE, message, throwable);
	}

	@Override
	public void debug(String message, Throwable throwable) {
		logger.log(java.util.logging.Level.FINE, message, throwable);
	}
	/**
	 * 设置日志级别
	 * @param level
	 */
	public void setLevel(Level level) {
		logger.setLevel(toJdkLvl(level));
	}

	@Override
	public void error(Throwable throwable) {
		error(Strings.BLANK, throwable);
	}

	@Override
	public void print(String message, Throwable throwable) {
		error(message, throwable);
	}

	@Override
	public void print(Throwable throwable) {
		error(throwable);
	}

	@Override
	public void print(Object message) {
		error(message);
	}

}