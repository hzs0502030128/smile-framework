package org.smile.beans;

import org.smile.beans.converter.BeanException;
/**
 * 不存在属性定义是抛出的异常
 * @author 胡真山
 *
 */
public class NoPropertyException extends BeanException{
	
	public NoPropertyException(String message,Throwable cause) {
		super(message,cause);
	}
	
	public NoPropertyException(String message) {
		super(message);
	}
	
	public NoPropertyException(Class clazz,String name){
		super("not exists property named "+name+" in class "+clazz);
	}
}
