package org.smile.config;

import org.smile.beans.converter.BeanException;

public class BeanCreateException extends BeanException{
	
	public BeanCreateException(String msg,Throwable e){
		super(msg, e);
	}
	
	public BeanCreateException(String msg){
		super(msg);
	}
}
