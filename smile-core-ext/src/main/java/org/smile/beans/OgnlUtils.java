package org.smile.beans;

import org.smile.beans.converter.BeanException;
import org.smile.beans.handler.OgnlPropertyHandler;
import org.smile.commons.SmileRunException;
import org.smile.log.LoggerHandler;

public class OgnlUtils implements LoggerHandler{
	
	private static  PropertyHandler handler;
	
	private static boolean ognlSupport=true;
	
	static {
		try{
			handler=new OgnlPropertyHandler();
		}catch(Throwable e){
			logger.info("不支持OGNL表达式");
		}
	}
	
	public static <T> T getValue(String exp,Object root){
		try {
			return (T)handler.getExpFieldValue(root, exp);
		} catch (BeanException e) {
			throw new SmileRunException(e);
		}
	}
	
	public static void setValue(Object target,String exp,Object value){
		try {
			handler.setExpFieldValue(target, exp, value);
		} catch (BeanException e) {
			throw new SmileRunException(e);
		}
	}
}
