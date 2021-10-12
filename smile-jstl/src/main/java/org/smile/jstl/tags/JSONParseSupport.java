package org.smile.jstl.tags;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.smile.json.JSON;

public class JSONParseSupport{
	/**
	 * 表达式转成map
	 * @param value
	 * @return
	 * @throws JspException
	 */
	public static Map parseToMap(Object value) throws JspException{
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
}
