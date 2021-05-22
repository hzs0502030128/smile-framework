package org.smile.strate.handler;

import org.smile.strate.StrateException;

/**
 * 实例化action异常
 * @author strive
 *
 */
public class StrateBeanHandlerException extends StrateException {

	public StrateBeanHandlerException(String message){
		super(" create action object case a exception ："+message);
	}
	public StrateBeanHandlerException(Throwable e){
		this(e.getMessage(),e);
	}
	public StrateBeanHandlerException(String message, Throwable e) {
		super("create action object case a exception ："+message,e);
	}
	
}
