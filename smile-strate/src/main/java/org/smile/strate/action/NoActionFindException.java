package org.smile.strate.action;

/***
 * 实始化异常
 * @author 胡真山
 *
 */
public class NoActionFindException extends StrateInitException {
	public NoActionFindException(String msg,Throwable e){
		super(msg, e);
	}
	public NoActionFindException(String msg){
		super(msg);
	}
}
