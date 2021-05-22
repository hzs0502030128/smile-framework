package org.smile.beans.handler;

import java.util.Map;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.jexl3.ObjectContext;
import org.smile.beans.PropertyHandler;
import org.smile.beans.converter.BeanException;

public class Jexl3PropertyHandler implements PropertyHandler<Object> {
	/**
	 * 表达式引擎
	 */
	protected static JexlEngine jexl=new JexlBuilder().create();
	
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
		 JexlExpression e = jexl.createExpression(el);
		 return e.evaluate(context);
	 }
	 /**
	  * 从map中解析表达式
	  * @param map
	  * @param el
	  * @return
	  */
	 public static Object getValue(Map map,String el){
		 JexlExpression e = jexl.createExpression(el);
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
		 JexlExpression e = jexl.createExpression(el);
		 JexlContext context=new ObjectContext(jexl,bean);
		 return e.evaluate(context);
	 }
	 /**
	  * 解析表达式
	  * @param el
	  * @return
	  */
	 public static JexlExpression parseEl(String el){
		 JexlExpression exp=jexl.createExpression(el);
		 return exp;
	 }
	 /**
	  * 获取表达式的值
	  * @param context
	  * @param expressoin
	  * @return
	  */
	 public static Object getValue(JexlContext context,JexlExpression expressoin){
		 return expressoin.evaluate(context);
	 }

}
