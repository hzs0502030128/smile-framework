package org.smile.log;

/***
 * 实现些接口将会得到一个logger的实例 
 * 方便记得日志
 * @author 胡真山
 * 2015年9月22日
 */
public interface LoggerHandler {
	/***
	 * 一个日志实例
	 */
	public static final Logger logger=LoggerFactory.getLogger(LoggerHandler.class);
	
}
