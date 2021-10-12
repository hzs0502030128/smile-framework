package org.smile.gateway.json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.smile.beans.converter.BasicConverter;
import org.smile.gateway.GatewayException;
import org.smile.reflect.ClassTypeUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;
/**
 * net.sf.json.JSONArray 实现
 * @author 胡真山
 *
 */
public class SfJsonHandler implements JsonHandler{

	@Override
	public Object[] parserJsonToArray(String array) throws Exception {
		return JSONArray.fromObject(array).toArray();
	}

	@Override
	public String toJson(Object obj) throws Exception {
		return JSONSerializer.toJSON(obj).toString();
	}

	@Override
	public Object toMethodParam(Type type, Object value) {
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
