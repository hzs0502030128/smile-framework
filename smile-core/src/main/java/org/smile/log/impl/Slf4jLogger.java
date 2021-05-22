package org.smile.log.impl;

import org.smile.commons.Strings;
import org.smile.log.Logger;
/**
 * SLF4J logger adapter.
 */
public class Slf4jLogger implements Logger {

	private final org.slf4j.Logger logger;
	

	public Slf4jLogger(org.slf4j.Logger logger) {
		this.logger = logger;
	}

	public String getName() {
		return logger.getName();
	}

	public boolean isEnabled(Level level) {
		switch (level) {
			case TRACE: return logger.isTraceEnabled();
			case DEBUG: return logger.isDebugEnabled();
			case INFO: return logger.isInfoEnabled();
			case WARN: return logger.isWarnEnabled();
			case ERROR:
			case PRINT:
				return logger.isErrorEnabled();
			default:
				throw new IllegalArgumentException();
		}
	}

	public void log(Level level, Object message) {
		switch (level) {
			case TRACE: logger.trace(message==null?Strings.NULL:message.toString()); break;
			case DEBUG: logger.debug(message==null?Strings.NULL:message.toString()); break;
			case INFO: logger.info(message==null?Strings.NULL:message.toString()); break;
			case WARN: logger.warn(message==null?Strings.NULL:message.toString()); break;
			case ERROR:
			case PRINT:
				logger.error(message==null?Strings.NULL:message.toString()); break;
		}
	}

	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	public void trace(Object message) {
		logger.trace(message==null?Strings.NULL:message.toString());
	}

	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	public void debug(Object message) {
		logger.debug(message==null?Strings.NULL:message.toString());
	}

	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	public void info(Object message) {
		logger.info(message==null?Strings.NULL:message.toString());
	}

	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	public void warn(Object message) {
		logger.warn(message==null?Strings.NULL:message.toString());
	}

	public void warn(String message, Throwable throwable) {
		logger.warn(message, throwable);
	}

	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	public void error(Object message) {
		logger.error(message==null?Strings.NULL:message.toString());
	}

	public void error(String message, Throwable throwable) {
		logger.error(message, throwable);
	}

	@Override
	public void info(String message, Throwable throwable) {
		logger.info(message, throwable);
	}

	@Override
	public void debug(String message, Throwable throwable) {
		logger.debug(message, throwable);
	}

	@Override
	public void error(Throwable throwable) {
		error(Strings.BLANK, throwable);
	}

	@Override
	public void print(String message, Throwable throwable) {
		this.logger.error(message, throwable);
	}

	@Override
	public void print(Throwable throwable) {
		this.logger.error(Strings.BLANK, throwable);
	}

	@Override
	public void print(Object message) {
		this.logger.error(message==null?Strings.NULL:message.toString());
	}

}