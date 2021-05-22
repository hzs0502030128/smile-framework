package org.smile.log.impl;

import org.smile.commons.ExceptionUtils;
import org.smile.commons.Strings;
import org.smile.log.Logger;
import org.smile.util.SysUtils;

/**
 * Java Commons logging logger.
 * log4j.properties
 * #Created by JInto - www.guh-software.de
	#Fri Mar 03 17:49:10 CST 2017
	log4j.appender.CONSOLE=org.apache.log4j.ConsoleAppender
	log4j.appender.CONSOLE.layout=org.apache.log4j.PatternLayout
	log4j.appender.CONSOLE.layout.ConversionPattern=[%-d{MM-dd HH\:mm\:ss,SSS}] [%p]- %l %m%n
	log4j.appender.D=org.apache.log4j.DailyRollingFileAppender
	log4j.appender.D.DatePattern=yyyy-MM-dd-HH'.log'
	log4j.appender.D.File=${srm_logs}/srm_roll.log
	log4j.appender.D.layout=org.apache.log4j.PatternLayout
	log4j.appender.D.layout.ConversionPattern=[%-d{MM-dd HH\:mm\:ss,SSS}] [%p]-[%l] %m%n
	log4j.appender.db=org.apache.log4j.jdbc.JDBCAppender
	log4j.appender.db.BufferSize=1 
	log4j.appender.db.URL=${srm_url}
	log4j.appender.db.driver=${srm_driver}
	log4j.appender.db.layout=org.apache.log4j.PatternLayout  
	log4j.appender.db.password=${srm_password}
	log4j.appender.db.sql=insert into T_SYSTEM_LOG (operator,type,date,message) values ('%X{operator}','%X{type}','%d{yyyy-MM-dd HH\:mm\:ss}','%m')  
	log4j.appender.db.user=${srm_username}
	log4j.appender.fileLog=org.apache.log4j.RollingFileAppender
	log4j.appender.fileLog.File=${srm_logs}/srm.log
	log4j.appender.fileLog.MaxBackupIndex=5
	log4j.appender.fileLog.MaxFileSize=500KB
	log4j.appender.fileLog.layout=org.apache.log4j.PatternLayout
	log4j.appender.fileLog.layout.ConversionPattern=[%-d{MM-dd HH\:mm\:ss,SSS}] [%p]-[%l] %m%n
	log4j.logger.com.mindray.srm.action.admin.PwdResetAction=INFO,db
	log4j.logger.com.mindray.srm.action.admin.UserAction=INFO,db
	log4j.logger.com.mindray.srm.action.admin.UserRoleAction=INFO,db
	log4j.logger.com.opensymphony=WARN
	log4j.logger.freemarker=WARN
	log4j.logger.net.sf=INFO
	log4j.logger.noModule=ERROR
	log4j.logger.org.apache=ERROR
	log4j.logger.org.apache.struts2=ERROR
	log4j.logger.org.hibernate=ERROR 
	log4j.logger.org.logicalcobwebs=ERROR
	log4j.logger.org.smile.hot=ERROR
	log4j.logger.org.springframework=WARN
	log4j.rootLogger=${log4j}

 */
public class Log4jLogger implements Logger {

	private final org.apache.log4j.Logger logger;
	
	private static final String FQCN = Log4jLogger.class.getName();

	public Log4jLogger(org.apache.log4j.Logger log) {
		this.logger = log;
	}
	@Override
	public String getName() {
		return logger.toString();
	}
	@Override
	public boolean isEnabled(Level level) {
		switch (level) {
			case TRACE: return logger.isTraceEnabled();
			case DEBUG: return logger.isDebugEnabled();
			case INFO: return logger.isInfoEnabled();
			case WARN: return logger.isEnabledFor(org.apache.log4j.Level.WARN);
			case ERROR: return logger.isEnabledFor(org.apache.log4j.Level.ERROR);
			default:
				throw new IllegalArgumentException();
		}

	}
	@Override
	public void log(Level level, Object message) {
		switch (level) {
			case TRACE: logger.trace(message); break;
			case DEBUG: logger.debug(message); break;
			case INFO: logger.info(message); break;
			case WARN: logger.warn(message); break;
			case ERROR: logger.error(message); break;
			default:SysUtils.println(message);
		}
	}
	@Override
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}
	@Override
	public void trace(Object message) {
		logger.log(FQCN, org.apache.log4j.Level.TRACE,message,null);
	}
	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}
	@Override
	public void debug(Object message) {
		logger.log(FQCN, org.apache.log4j.Level.DEBUG,message,null);
	}
	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}
	@Override
	public void info(Object message) {
		logger.log(FQCN, org.apache.log4j.Level.INFO,message,null);
	}
	
	@Override
	public boolean isWarnEnabled() {
		return logger.isEnabledFor(org.apache.log4j.Level.WARN);
	}
	@Override
	public void warn(Object message) {
		logger.log(FQCN, org.apache.log4j.Level.WARN,message,null);
	}
	@Override
	public void warn(String message, Throwable throwable) {
		logger.log(FQCN, org.apache.log4j.Level.WARN,message, throwable);
	}
	@Override
	public boolean isErrorEnabled() {
		return logger.isEnabledFor(org.apache.log4j.Level.ERROR);
	}
	@Override
	public void error(Object message) {
		logger.log(FQCN, org.apache.log4j.Level.ERROR,message,null);
	}
	@Override
	public void error(String message, Throwable throwable) {
		logger.log(FQCN, org.apache.log4j.Level.ERROR,message, throwable);
	}

	@Override
	public void info(String message, Throwable throwable) {
		logger.log(FQCN, org.apache.log4j.Level.INFO,message, throwable);
	}

	@Override
	public void debug(String message, Throwable throwable) {
		logger.log(FQCN, org.apache.log4j.Level.DEBUG,message, throwable);
	}

	@Override
	public void error(Throwable throwable) {
		logger.log(FQCN, org.apache.log4j.Level.ERROR,Strings.BLANK, throwable);
	}

	@Override
	public void print(String message, Throwable throwable) {
		error(message, throwable);
	}

	@Override
	public void print(Throwable throwable) {
		error( ExceptionUtils.getExceptionMsgDetail(throwable));
	}

	@Override
	public void print(Object message) {
		error(message);
	}

}