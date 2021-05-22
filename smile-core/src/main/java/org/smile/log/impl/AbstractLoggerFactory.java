package org.smile.log.impl;

import java.util.concurrent.ConcurrentHashMap;

import org.smile.log.Logger;
import org.smile.log.LoggerFactoryInterface;

public abstract class AbstractLoggerFactory implements LoggerFactoryInterface{
	/**
	 * 用于缓存已经创建了的日志名称
	 */
	protected ConcurrentHashMap<String, Logger> loggers=new ConcurrentHashMap<String, Logger>();
	
	@Override
	public Logger getLogger(String name) {
		Logger logger=loggers.get(name);
		if(logger!=null){
			return logger;
		}
		Logger newLogger=newInstanceLogger(name);
		Logger oldLogger=loggers.putIfAbsent(name, newLogger);
		return oldLogger==null?newLogger:oldLogger;
	}
	
	protected abstract Logger newInstanceLogger(String name);

}
