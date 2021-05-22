package org.smile.commons;

/**
 * 没有实现的方法
 * @author 胡真山
 *
 */
public class NotImplementedException extends SmileRunException{

	public NotImplementedException() {
		super("not support this method ");
	}
	
	public NotImplementedException(String msg) {
		super(msg);
	}

}
