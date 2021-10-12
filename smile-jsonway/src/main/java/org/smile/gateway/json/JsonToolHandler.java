package org.smile.gateway.json;

import java.io.StringReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.smile.beans.converter.BasicConverter;
import org.smile.gateway.GatewayException;
import org.smile.reflect.ClassTypeUtils;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.parser.JSONParser;

/**
 * com.sdicons.json 实现
 * @author 胡真山
 *
 */
public class JsonToolHandler implements JsonHandler{

	@Override
	public Object[] parserJsonToArray(String array)  {
		try{
			JSONArray jsonArgsArray = (JSONArray)(new JSONParser(new StringReader(array))).nextValue();
			return jsonArgsArray.getValue().toArray(); 
		}catch(Exception e){
			throw new GatewayException("解析"+array+"成数组失败",e);
		}
	}

	@Override
	public String toJson(Object obj) {
		try {
			return JSONMapper.toJSON(obj).render(false);
		} catch (MapperException e) {
			throw new GatewayException("转换成json失败",e);
		}
	}

	@Override
	public Object toMethodParam(Type type,Object value) {
		try {
			if(value instanceof JSONValue){
				if(type instanceof Class){
					return JSONMapper.toJava((JSONValue)value,(Class)type);
				}else if(type instanceof ParameterizedType){
					return JSONMapper.toJava((JSONValue)value,(ParameterizedType)type);
				}
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
