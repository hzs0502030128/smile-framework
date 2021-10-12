package org.smile.gateway.json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.smile.beans.converter.BasicConverter;
import org.smile.gateway.GatewayException;
import org.smile.json.JSON;
import org.smile.json.parser.JSONParseException;
import org.smile.reflect.ClassTypeUtils;

public class SmileJsonHandler implements JsonHandler{

	@Override
	public Object[] parserJsonToArray(String array) throws JSONParseException {
		return JSON.parseJSONArray(array).toArray();
	}

	@Override
	public String toJson(Object obj) {
		return JSON.toJSONString(obj);
	}

	@Override
	public Object toMethodParam(Type type,Object value) {
		try {
			if(type instanceof Class){
				return BasicConverter.getInstance().convert((Class)type, value);
			}else if(type instanceof ParameterizedType){
				Class clazz=(Class)((ParameterizedType)type).getRawType();
				return BasicConverter.getInstance().convert(clazz,ClassTypeUtils.getGenericObj(type), value);
			}
		} catch (Exception e) {
			throw new GatewayException("转换"+value+"成"+type,e);
		}
		throw new GatewayException("not support type"+type);
	}
}
