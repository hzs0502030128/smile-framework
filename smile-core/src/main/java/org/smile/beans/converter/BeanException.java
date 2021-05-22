package org.smile.beans.converter;

import org.smile.commons.SmileException;

/**
 * javabean转换异常
 * @author strive
 *
 */
public class BeanException extends SmileException {
	
	public BeanException(String message,Throwable cause) {
		super(message,cause);
	}
	public BeanException(Throwable cause) {
		super(cause);
	}
	public BeanException(String message) {
		super(message);
	}
	public BeanException(){
		super("bean  exception ");
	}
}
