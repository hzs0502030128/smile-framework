package org.smile.gateway.json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.smile.beans.converter.BasicConverter;
import org.smile.gateway.GatewayException;
import org.smile.reflect.ClassTypeUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;


/**
 * fastjson使用
 * @author 胡真山
 *
 */
public class FastJsonHandler implements JsonHandler{

	@Override
	public Object[] parserJsonToArray(String array)  {
		JSONArray jsonArgsArray =JSON.parseArray(array);
		return jsonArgsArray.toArray(); 
	}

	@Override
	public String toJson(Object obj) {
		return JSON.toJSONString(obj);
	}

	@Override
	public Object toMethodParam(Type type,Object value) {
		try {
			if(value instanceof JSON&&type instanceof Class){
				return JSON.toJavaObject((JSON)value,(Class)type);
			}else{
				if(type instanceof Class){
					return BasicConverter.getInstance().convert((Class)type,value);
				}else if(type instanceof ParameterizedType){
					Class clazz=(Class)((ParameterizedType)type).getRawType();
					return BasicConverter.getInstance().convert(clazz,ClassTypeUtils.getGenericObj(type), value);
				}
			}
		} catch (Exception e) {
			throw new GatewayException("转换"+value+"成"+type,e);
		}
		throw new GatewayException("not support type"+type);
	}

}
