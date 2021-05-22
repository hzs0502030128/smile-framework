package org.smile.log.record;

import java.util.Date;

import org.smile.log.Logger.Level;
import org.smile.util.DateUtils;

public class LogRecord {
	/**
	 * 调用的类
	 */
	protected String className;
	/**
	 * 调用的方法
	 */
	protected String methodName;
	/**
	 * 调用的行
	 */
	protected int lineNumber;
	/**
	 * 日志名称
	 */
	protected String loggerName;
	/**
	 * 产生logger的类名
	 */
	protected String fqcn;
	/**
	 * 日志等级
	 */
	protected Level level;
	/**
	 * 日志信息
	 */
	protected Object message;
	
	public LogRecord(Class fqcn,String loggerName,Level level,Object message){
		this(fqcn.getName(), loggerName, level, message);
	}
	
	public LogRecord(String fqcn,String loggerName,Level level,Object message){
		this.loggerName=loggerName;
		this.fqcn=fqcn;
		this.level=level;
		this.message=message;
		initInvokeInfo();
	}
	/**
	 * 初始化调用信息
	 */
	public void initInvokeInfo(){
		Exception exception = new Exception();
		StackTraceElement[] stackTrace = exception.getStackTrace();
		boolean fqcnFlag=false;
		for (StackTraceElement stackTraceElement : stackTrace) {
			className= stackTraceElement.getClassName();
			if(fqcnFlag){
				if(className.equals(fqcn)){
					continue;
				}
				methodName=stackTraceElement.getMethodName();
				lineNumber=stackTraceElement.getLineNumber();
				break;
			}
			if((!fqcnFlag)&&className.equals(fqcn)){
				fqcnFlag=true;
			}
		}
	}
	
	public String getClassName() {
		return className;
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
	
	public String getLoggerName() {
		return loggerName;
	}
	
	public String getFqcn() {
		return fqcn;
	}
	
	public String getDate(){
		return DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss SSS");
	}
	
	public Level getLevel() {
		return level;
	}
	public Object getMessage() {
		return message;
	}
	
	
}
