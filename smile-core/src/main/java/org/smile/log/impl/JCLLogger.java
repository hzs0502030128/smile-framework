package org.smile.log.impl;

import org.apache.commons.logging.Log;
import org.smile.commons.Strings;
import org.smile.log.Logger;

/**
 * Java Commons logging logger.
 */
public class JCLLogger implements Logger {

	private final Log logger;

	public JCLLogger(Log log) {
		this.logger = log;
	}

	public String getName() {
		return logger.toString();
	}

	public boolean isEnabled(Level level) {
		switch (level) {
			case TRACE: return logger.isTraceEnabled();
			case DEBUG: return logger.isDebugEnabled();
			case INFO: return logger.isInfoEnabled();
			case WARN: return logger.isWarnEnabled();
			case ERROR: return logger.isErrorEnabled();
			default:
				throw new IllegalArgumentException();
		}

	}

	public void log(Level level, Object message) {
		switch (level) {
			case TRACE: logger.trace(message); break;
			case DEBUG: logger.debug(message); break;
			case INFO: logger.info(message); break;
			case WARN: logger.warn(message); break;
			case ERROR: logger.error(message); break;
		}
	}

	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	public void trace(Object message) {
		logger.trace(message);
	}

	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	public void debug(Object message) {
		logger.debug(message);
	}

	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	public void info(Object message) {
		logger.info(message);
	}

	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	public void warn(Object message) {
		logger.warn(message);
	}

	public void warn(String message, Throwable throwable) {
		logger.warn(message, throwable);
	}

	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	public void error(Object message) {
		logger.error(message);
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