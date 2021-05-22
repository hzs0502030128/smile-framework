package org.smile.log.impl;

import java.util.Collection;

import org.smile.commons.Strings;
import org.smile.log.Logger;
import org.smile.log.record.Handler;
import org.smile.log.record.LogRecord;

/**
 * Simple logger.
 * Smile logger 实现 
 * 
 *  配置样例  logger.properties 文件
	level=debug
	logger.consle.handler=org.smile.log.record.ConsoleHandler
	logger.consle.level=debug
	logger.consle.formatter=org.smile.log.record.SimpleRecordFormatter
	
	logger.file.handler=org.smile.log.record.FileHandler
	logger.file.pattern = ${catalina.base}/logs/java'yyyy-MM-dd'.log
	logger.file.level=debug
	org.jj=warn
	test=debug
 */
public class SimpleLogger implements Logger {

	private final String name;
	
	private Level level;
	
	private Collection<Handler> handlers;

	public SimpleLogger(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isEnabled(Level level) {
		return level.isEnabledFor(this.level);
	}

	public void log(Level level, Object message) {
		log(level, message, null);
	}

	public boolean isTraceEnabled() {
		return Level.TRACE.isEnabledFor(level);
	}

	public void trace(Object message) {
		log(Level.TRACE, message, null);
	}

	public boolean isDebugEnabled() {
		return Level.DEBUG.isEnabledFor(level);
	}

	public void debug(Object message) {
		log(Level.DEBUG, message, null);
	}

	public boolean isInfoEnabled() {
		return Level.INFO.isEnabledFor(level);
	}

	public void info(Object message) {
		log(Level.INFO, message, null);
	}

	public boolean isWarnEnabled() {
		return Level.WARN.isEnabledFor(level);
	}

	public void warn(Object message) {
		log(Level.WARN, message, null);
	}

	public void warn(String message, Throwable throwable) {
		log(Level.WARN, message, throwable);
	}

	public boolean isErrorEnabled() {
		return Level.ERROR.isEnabledFor(level);
	}

	public void error(Object message) {
		log(Level.ERROR, message, null);
	}

	public void error(String message, Throwable throwable) {
		log(Level.ERROR, message, throwable);
	}

	/**
	 * logs log message.
	 */
	public void log(Level level, Class fqcn, Object message, Throwable throwable) {
		if (isEnabled(level)) {
			LogRecord record = new LogRecord(fqcn, name, level, message);
			for(Handler handler:handlers){
				handler.handle(record, throwable);
			}
		}
	}

	public void log(Level level, Object message, Throwable throwable) {
		log(level, SimpleLogger.class, message, throwable);
	}

	@Override
	public void info(String message, Throwable throwable) {
		log(Level.INFO, message, throwable);
	}

	@Override
	public void debug(String message, Throwable throwable) {
		log(Level.DEBUG, message, throwable);
	}

	public void setLevel(Level level) {
		this.level = level;
	}
	
	public void setHandler(Collection<Handler> handlers){
		this.handlers=handlers;
	}

	@Override
	public void error(Throwable throwable) {
		error(Strings.BLANK, throwable);
	}

	@Override
	public void print(String message, Throwable throwable) {
		log(Level.PRINT,message,throwable);
	}

	@Override
	public void print(Throwable throwable) {
		print(Strings.BLANK, throwable);
	}

	@Override
	public void print(Object message) {
		print(message==null?Strings.BLANK:message.toString(), null);
	}
}