package org.smile.log.impl;

import org.smile.commons.Strings;
import org.smile.log.Logger;
import org.smile.log.record.LogRecord;
import org.smile.log.record.RecordFormatter;
import org.smile.log.record.SimpleRecordFormatter;
import org.smile.util.SysUtils;

/**
 * Dummy logger.  
 * 在初始化其它日志之前默认使用的日志
 */
public class NOPLogger implements Logger {
	
	private final String name;
	
	private final static RecordFormatter formatter=new SimpleRecordFormatter();

	protected NOPLogger(String name) {
		LogRecord record=new LogRecord("org.smile.log.LoggerHandler",name,Level.INFO,"NOPLogger init");
		SysUtils.println(formatter.format(record));
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isEnabled(Level level) {
		return false;
	}

	public void log(Level level, Object message) {
	}

	public boolean isTraceEnabled() {
		return false;
	}

	public void trace(Object message) {

	}

	public boolean isDebugEnabled() {
		return false;
	}

	public void debug(Object message) {
	}

	public boolean isInfoEnabled() {
		return false;
	}

	public void info(Object message) {
	}

	public boolean isWarnEnabled() {
		return false;
	}

	public void warn(Object message) {
		
	}

	public void warn(String message, Throwable throwable) {
		
	}

	public boolean isErrorEnabled() {
		return true;
	}

	public void error(Object message) {
		LogRecord record=new LogRecord(NOPLogger.class, name, Level.ERROR, message);
		SysUtils.println(formatter.format(record));
	}

	public void error(String message, Throwable throwable) {
		LogRecord record=new LogRecord(NOPLogger.class, name, Level.ERROR, message);
		SysUtils.println(formatter.format(record),throwable);
	}

	@Override
	public void info(String message, Throwable throwable) {
		
	}

	@Override
	public void debug(String message, Throwable throwable) {
		
	}

	@Override
	public void error(Throwable throwable) {
		LogRecord record=new LogRecord(NOPLogger.class, name, Level.ERROR, Strings.BLANK);
		SysUtils.println(formatter.format(record),throwable);
	}

	@Override
	public void print(String message, Throwable throwable) {
		LogRecord record=new LogRecord(NOPLogger.class, name, Level.ERROR,message);
		SysUtils.println(formatter.format(record),throwable);
	}

	@Override
	public void print(Throwable throwable) {
		print(Strings.BLANK, throwable);
	}

	@Override
	public void print(Object message) {
		print(message==null?Strings.BLANK:message.toString(),null);
	}

}