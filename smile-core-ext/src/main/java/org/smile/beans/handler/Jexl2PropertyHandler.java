package org.smile.beans.handler;

import java.util.Map;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.jexl2.ObjectContext;
import org.smile.beans.PropertyHandler;
import org.smile.beans.converter.BeanException;

public class Jexl2PropertyHandler implements PropertyHandler<Object> {
	/**
	 * 表达式引擎
	 */
	protected static JexlEngine jexl=new JexlEngine();
	
	@Override
	public Object getExpFieldValue(Object target, String exp) throws BeanException {
		if(target instanceof Map){
			return getValue((Map)target, exp);
		}else{
			return getValue(target, exp);
		}
	}

	@Override
	public void setExpFieldValue(Object target, String exp, Object value) throws BeanException {
		jexl.setProperty(target, exp, value);
	}
	
	
	 /**
	  * 获取表达式的值
	  * @param context
	  * @param el
	  * @return
	  */
	 public static Object getValue(JexlContext context,String el){
		 Expression e = jexl.createExpression(el);
		 return e.evaluate(context);
	 }
	 /**
	  * 从map中解析表达式
	  * @param map
	  * @param el
	  * @return
	  */
	 public static Object getValue(Map map,String el){
		 Expression e = jexl.createExpression(el);
		 JexlContext context=new MapContext(map);
		 return e.evaluate(context);
	 }
	 /**
	  * 从对象中解析表达式
	  * @param bean
	  * @param el
	  * @return
	  */
	 public static Object getValue(Object bean,String el){
		 Expression e = jexl.createExpression(el);
		 JexlContext context=new ObjectContext(jexl,bean);
		 return e.evaluate(context);
	 }
	 /**
	  * 解析表达式
	  * @param el
	  * @return
	  */
	 public static Expression parseEl(String el){
		 Expression exp=jexl.createExpression(el);
		 return exp;
	 }
	 /**
	  * 获取表达式的值
	  * @param context
	  * @param expressoin
	  * @return
	  */
	 public static Object getValue(JexlContext context,Expression expressoin){
		 return expressoin.evaluate(context);
	 }

}
