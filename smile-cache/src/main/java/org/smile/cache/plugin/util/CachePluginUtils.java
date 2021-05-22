package org.smile.cache.plugin.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.smile.beans.PropertyHandler;
import org.smile.beans.converter.BeanException;
import org.smile.beans.handler.MapPropertyHandler;
import org.smile.cache.plugin.ann.CacheWipe;
import org.smile.cache.plugin.ann.CacheWipes;
import org.smile.collection.ArrayUtils;
import org.smile.collection.CollectionUtils;
import org.smile.commons.SmileRunException;
import org.smile.commons.Strings;
import org.smile.json.JSONFiledSerializer;
import org.smile.json.JSONSerializer;
import org.smile.reflect.MethodParamNameUtils;
import org.smile.util.RegExp;
/**
 * 插件工具类
 * @author 胡真山
 *
 */
public class CachePluginUtils {
	/**
	 * 用于对obj[name] 转成 obj.name 这样的方式兼容
	 */
	protected static RegExp paramExpStart=new RegExp("\\[");
	protected static RegExp paramExpEnd=new RegExp("\\]+");
	protected static JSONSerializer jsonSerializer=new JSONFiledSerializer();
	/**
	 * 获取一个方法中的清除缓存的配置
	 * @param method
	 * @return
	 */
	public static CacheWipe[] getCacheWipes(Method method) {
		LinkedList<CacheWipe> wipes = new LinkedList<CacheWipe>();
		CacheWipes handles = method.getAnnotation(CacheWipes.class);
		if (handles != null) {
			CollectionUtils.add(wipes, handles.value());
		}
		CacheWipe handle = method.getAnnotation(CacheWipe.class);
		if (handle != null) {
			wipes.add(handle);
		}
		return wipes.toArray(new CacheWipe[wipes.size()]);
	}

	/***
	 * 生成方法缓存的键值
	 * @param method
	 * @return
	 */
	public static String getMethodCacheKey(Method method) {
		return getMethodCacheKey(method.getDeclaringClass(), method);
	}

	/**
	 * 从方法构建缓存的key
	 * @param clazz
	 * @param method
	 * @return
	 */
	public static String getMethodCacheKey(Class<?> clazz, Method method) {
		StringBuilder sb = new StringBuilder();
		sb.append(clazz.getName()).append(Strings.DOT).append(method.getName());
		Class<?>[] paraTypes = method.getParameterTypes();
		sb.append(Strings.LEFT_BRACKET);
		if (ArrayUtils.notEmpty(paraTypes)) {
			for (int i = 0; i < paraTypes.length; i++) {
				sb.append(paraTypes[i].getName());
				if (i < paraTypes.length - 1) {
					sb.append(Strings.COMMA);
				}
			}
		}
		sb.append(Strings.RIGHT_BRACKET);
		return sb.toString();
	}

	/**
	 * 从方法参数中获取配置了的字段信息的值
	 * @param method 方法
	 * @param fieldNames 配置的字段信息
	 * @param args 方法的参数
	 * @return
	 */
	public static Map<String, Object> getMethodParam(Method method, String[] fieldNames, Object[] args) {
		String[] names = MethodParamNameUtils.getParamNames(method);
		if (ArrayUtils.isEmpty(names)) {
			return null;
		} else {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			for (int i = 0; i < names.length; i++) {
				paramMap.put(names[i], args[i]);
			}
			Map<String, Object> returnMap = new LinkedHashMap<String, Object>(fieldNames.length);
			PropertyHandler handler = new MapPropertyHandler();
			for (int i = 0; i < fieldNames.length; i++) {
				try {
					String currentFieldExp=convertArrayKeyToProperty(fieldNames[i]);
					returnMap.put(fieldNames[i], handler.getExpFieldValue(paramMap,currentFieldExp ));
				} catch (BeanException e) {
					throw new SmileRunException("get method cache filed name "+fieldNames[i]+" value exception ",e);
				}
			}
			return returnMap;
		}
	}
	
	/**
	 * 数组类型的参数名转成属性类型的参数名
	 * person[name] ==> person.name
	 * @param key
	 * @return
	 */
	protected static String convertArrayKeyToProperty(String key){
		key=paramExpStart.replaceAll(key,Strings.DOT);
		key=paramExpEnd.replaceAll(key, Strings.BLANK);
		return key;
	}
	/**
	 * 参数直接转成json字符串
	 * @param args
	 * @return
	 */
	public static String getMethodParamValues(Object[] args){
		return jsonSerializer.serialize(args);
	}
}
