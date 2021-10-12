package org.smile.jstl.tags;

import javax.servlet.jsp.PageContext;

import org.smile.beans.PropertyHandler;
import org.smile.beans.PropertyHandlers;
import org.smile.beans.converter.BeanException;
import org.smile.util.RegExp;
import org.smile.web.scope.ScopeUtils;

public class PropertySupport {
	
	private static RegExp reg=new RegExp("^%\\{ *.* *\\}$");
	
	public static boolean isExpression(Object string){
		if(string instanceof String){
			return reg.test((String)string);
		}
		return false;
	}
	
	public static String getExpressionText(String string){
		return string.substring(2,string.length()-1).trim();
	}
	
	/**
	 * 
	 * @param express 表达式%{user.name}样式
	 * @param pageContext
	 * @return
	 * @throws BeanException
	 */
	public static Object getExpressionValue(String express,PageContext pageContext) throws BeanException{
		String string=getExpressionText(express);
		return getPropertyValue(string, pageContext);
	}
	/**
	 * @param string 属性 user.name 样式
	 * @param pageContext
	 * @return
	 * @throws BeanException
	 */
	public static Object getPropertyValue(String string,PageContext pageContext) throws BeanException{
		int index=string.indexOf(".");
		if(index>0){
			Object obj=ScopeUtils.getAttribute(string.substring(0,index), pageContext);
			if(obj==null){
				return obj;
			}
			PropertyHandler handler=PropertyHandlers.getHanlder(obj.getClass());
			return handler.getExpFieldValue(obj,string.substring(index+1));
		}else{
			return ScopeUtils.getAttribute(string, pageContext);
		}
	}
}
