package org.smile.beans;

import org.smile.beans.converter.BeanException;
/**
 * 设置属性值的时候抛出的异常
 * @author 胡真山
 *
 */
public class AccessPropertyException extends BeanException{
	
	public AccessPropertyException(String message,Throwable cause) {
		super(message,cause);
	}
	
	
	public AccessPropertyException(String message) {
		super(message);
	}
}
