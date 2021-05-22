package org.smile.tag.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.smile.beans.converter.BaseTypeConverter;
import org.smile.beans.converter.BasicConverter;
import org.smile.json.JSON;
import org.smile.reflect.ClassTypeUtils;
import org.smile.util.DateUtils;

public class TagUtils{
	/**
	 * 表达式转成map
	 * @param value
	 * @return
	 * @throws JspException
	 */
	public static Map parseToMap(Object value) throws Exception{
		Map result=null;
		if(value!=null){
			if(value instanceof Map){
				result=(Map)value;
			}else if(value instanceof String){
				Object jsonValue=JSON.parse((String)value);
				if(jsonValue instanceof Map){
					return (Map)jsonValue;
				}else if(jsonValue instanceof List){
					return listToMap((List)jsonValue);
				}
			}
		}
		return result;
	}
	/**
	 * 列表转成map
	 * @param list
	 * @return
	 */
	public static Map listToMap(List list){
		Map map=new LinkedHashMap();
		for(int i=0;i<list.size();i++){
			map.put(list.get(i), list.get(++i));
		}
		return map;
	}
	
	public static Object convert(String type,Object value) throws Exception{
		Object realValue=null;
		Class basicClass=ClassTypeUtils.getBasicTypeClass(type);
		if(basicClass!=null){
			realValue=BaseTypeConverter.getInstance().convert(basicClass, value);
		}else if(ClassTypeUtils.isMapName(type)){
			realValue=parseToMap(value);
		}else if(ClassTypeUtils.isStringName(type)){
			realValue=BasicConverter.getInstance().convert(String.class, value);
		}else if(ClassTypeUtils.isDateName(type)){
			realValue=DateUtils.convertToDate(value);
		}
		return realValue;
	}
}
