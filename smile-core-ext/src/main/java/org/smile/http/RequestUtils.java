package org.smile.http;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smile.beans.BeanProperties;
import org.smile.beans.PropertyHandler;
import org.smile.beans.PropertyHandlers;
import org.smile.beans.converter.BasicConverter;
import org.smile.beans.converter.BeanException;
import org.smile.beans.converter.ConvertException;
import org.smile.collection.ArrayUtils;
import org.smile.commons.Strings;
import org.smile.log.LoggerHandler;
import org.smile.reflect.Generic;
import org.smile.util.RegExp;
/**
 * request 请求处理数据的工具类
 * @author 胡真山
 *
 */
public class RequestUtils implements LoggerHandler {
	
	public static RegExp paramExpStart=new RegExp("\\[");
	
	public static RegExp paramExpEnd=new RegExp("\\]+");
	
	/**
	 * get data form request convert to a bean 
	 * @param request
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public static void requestToBean(HttpServletRequest request, Object bean) {
		Enumeration<String> enuma = request.getParameterNames();
		BeanProperties beanProperties = BeanProperties.NORAL_CAN_NO_PROPERTY;
		while (enuma.hasMoreElements()) {
			String key = enuma.nextElement();
			String[] value = (String[]) request.getParameterValues(key);
			Object paramValue=value;
			if (ArrayUtils.isEmpty(value)) {
				continue;
			}
			try {
				beanProperties.setExpFieldValue(bean, convertArrayKeyToProperty(key), paramValue);
			} catch (BeanException e) {
				logger.info("request " + value + " to bean key " + key + " 出错", e);
			}
		}
		if (bean instanceof RequestBeanAWare) {
			((RequestBeanAWare) bean).setRequest(request);
		}
	}
	/**
	 * 请求的头信息赋到一个bean对象中
	 * @param request
	 * @param bean
	 */
	public static void requestHeaderToBean(HttpServletRequest request, Object bean) {
		Enumeration<String> headerNames = request.getHeaderNames();
		PropertyHandler handler = PropertyHandlers.getHanlder(bean.getClass(), false);
		while (headerNames.hasMoreElements()) {
			String key = headerNames.nextElement();
			String value = request.getHeader(key);
			try {
				handler.setExpFieldValue(bean, key, value);
			} catch (BeanException e) {
				logger.info("request " + value + " to bean key " + key + " 出错", e);
			}
		}
	}
	/**
	 * 从request中获取一个参数  转换为目标类型
	 * @param request 
	 * @param name 参数名
	 * @param clazz 转换的目标类型
	 * @param generic 目标类型的泛型
	 * @return
	 */
	public static <E> E getRequestParameter(HttpServletRequest request, String name, Class<E> clazz, Class[] generic) {
		String[] value = request.getParameterValues(name);
		if (ArrayUtils.notEmpty(value)) {
			try {
				return BasicConverter.getInstance().convert(clazz, new Generic(generic), value);
			} catch (ConvertException e) {
				logger.info("request " + value + "  key " + name + "convert to" + clazz + " 出错", e);
			}
		}
		return null;
	}
	/**
	 * 不进行泛型转换的 获取参数方法   
	 * 参考 getRequestParameter(HttpServletRequest request, String name, Class<E> clazz, Class[] generic)
	 * @param request
	 * @param name
	 * @param clazz
	 * @return
	 */
	public static <E> E getRequestParameter(HttpServletRequest request, String name, Class<E> clazz){
		return getRequestParameter(request, name, clazz,null);
	}

	/**
	 * 获取浏览器的名称
	 * @param request
	 * @return
	 */
	public static String getBrowserName(HttpServletRequest request) {
		String agent = request.getHeader("User-Agent").toLowerCase();
		return getBrowserName(agent);
	}

	/**
	* 获取浏览器版本信息
	* @Title: getBrowserName
	* @author:wolf
	* @param agent
	* @return
	*/

	private static String getBrowserName(String agent) {
		if (agent.indexOf("msie 7") > 0) {
			return "ie7";
		} else if (agent.indexOf("msie 8") > 0) {
			return "ie8";
		} else if (agent.indexOf("msie 9") > 0) {
			return "ie9";
		} else if (agent.indexOf("msie 10") > 0) {
			return "ie10";
		} else if (agent.indexOf("msie") > 0) {
			return "ie";
		} else if (agent.indexOf("opera") > 0) {
			return "opera";
		} else if (agent.indexOf("opera") > 0) {
			return "opera";
		} else if (agent.indexOf("firefox") > 0) {
			return "firefox";
		} else if (agent.indexOf("webkit") > 0) {
			return "webkit";
		} else if (agent.indexOf("gecko") > 0 && agent.indexOf("rv:11") > 0) {
			return "ie11";
		} else {
			return "Others";
		}
	}

	/**
	 * 数组类型的参数名转成属性类型的参数名
	 * person[name] ==> person.name
	 * @param key
	 * @return
	 */
	public static String convertArrayKeyToProperty(String key){
		key=paramExpStart.replaceAll(key, Strings.DOT);
		key=paramExpEnd.replaceAll(key,Strings.BLANK);
		return key;
	}
	/**
	 * 服务器设置不缓存页面
	 * @param response
	 */
	public static void setNoCache(HttpServletResponse response){
		response.setHeader("Cache-Control", "no-cache");  
		response.setHeader("Pragma", "no-cache");  
		response.setDateHeader("Expires", 0); 
	}
}
