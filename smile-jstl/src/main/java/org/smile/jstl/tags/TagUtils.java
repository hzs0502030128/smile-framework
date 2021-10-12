
package org.smile.jstl.tags;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.JspTag;

import org.smile.beans.BeanUtils;
import org.smile.beans.converter.BeanException;
import org.smile.io.CharArrayWriter;
import org.smile.jstl.tags.core.IfElseTag;
import org.smile.util.ObjectLenUtils;

/**
 * tag 工具类  获取属性
 * @author 胡真山
 * 2015年11月26日
 */
public class TagUtils{
	
	public static final String IF_ELSE_TEST="current_ifelse_test_var";
	
	/**
	 * Invokes tag body.
	 */
	public static void invokeBody(JspFragment body) throws JspException {
		if (body == null) {
			return;
		}
		try {
			body.invoke(null);
		} catch (IOException ioex) {
			throw new JspException("Tag body failed", ioex);
		}
	}

	/**
	 * Invokes tag body to provided writer.
	 */
	public static void invokeBody(JspFragment body, Writer writer) throws JspException {
		if (body == null) {
			return;
		}
		try {
			body.invoke(writer);
		} catch (IOException ioex) {
			throw new JspException("Tag body failed", ioex);
		}
	}

	/**
	 * Renders tag body to char array.
	 */
	public static char[] renderBody(JspFragment body) throws JspException {
		CharArrayWriter writer = new CharArrayWriter();
		invokeBody(body, writer);
		return writer.toCharArray();
	}

	/**
	 * Renders tag body to string.
	 * @see #renderBody(javax.servlet.jsp.tagext.JspFragment)
	 */
	public static String renderBodyToString(JspFragment body) throws JspException {
		char[] result = renderBody(body);
		return new String(result);
	}

	/**
	 * 设置ifelse的结果 
	 * @param parent
	 * @param isSucess
	 * @param pageContext
	 */
	public static void setIfElseSuccess(JspTag parent,Boolean isSucess,PageContext pageContext){
		if(parent!=null&&parent instanceof IfElseTag){
			((IfElseTag)parent).setSuccess(isSucess);
		}else{
			pageContext.setAttribute(IF_ELSE_TEST,isSucess);
		}
	}
	
	public static Boolean getIfElseSuccess(JspTag parent,PageContext pageContext){
		if(parent!=null&&parent instanceof IfElseTag){
			return ((IfElseTag)parent).isSuccess();
		}else{
			Object result=pageContext.getAttribute(IF_ELSE_TEST);
			if(result!=null){
				return (Boolean)result;
			}
			return false;
		}
	}
	/**
	 * 获取一个对象的属性  
	 * @param value 目标对象
	 * @param property 可以是属性 map的key 列表  数组的索引
	 * @return
	 * @throws JspException
	 */
	public  static Object getValueProperty(Object value,Object property) throws JspException{
		try {
			if(value==null){
				return value;
			}else if(value instanceof Map){
				return ((Map)value).get(property);
			}else if(ObjectLenUtils.hasLength(value)){
				return BeanUtils.getExpValue(value, (String)property);
			}
			return BeanUtils.getExpValue(value, (String)property);
		} catch (BeanException e) {
			throw new JspException("get "+value+"'s property "+property+" has a error ",e);
		}
	}
}
