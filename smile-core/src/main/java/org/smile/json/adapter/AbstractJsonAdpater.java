package org.smile.json.adapter;

import java.util.Collection;
import java.util.List;

import org.smile.beans.converter.BasicConverter;
import org.smile.beans.converter.ConvertException;
import org.smile.commons.SmileRunException;
import org.smile.json.JSONValue;
import org.smile.json.adapter.JsonAdpater;
import org.smile.json.format.DefaultSerializeConfig;
import org.smile.reflect.Generic;


public abstract class AbstractJsonAdpater implements JsonAdpater{

	@Override
	public <T> T parseJSONObject(String json, Class<T> javaType) {
		try {
			return BasicConverter.getInstance().convert(javaType, parseJSONObject(json));
		} catch (ConvertException e) {
			throw new SmileRunException("转为java对象失败 -->"+javaType, e);
		}
	}

	@Override
	public <T> List<T> parseJSONArray(String json, Class<T> javaType) {
		try {
			return BasicConverter.getInstance().convert(List.class,new Generic(new Class[]{javaType}), parseJSONArray(json));
		} catch (ConvertException e) {
			throw new SmileRunException("转为java对象失败 -->"+javaType, e);
		}
	}
	
	@Override
	public String toJSONString(Object obj) {
		return JSONValue.toJSONString(obj,DefaultSerializeConfig.NULL_VIEW);
	}
	
	@Override
	public String toJSONString(Collection json) {
		return JSONValue.toJSONString(json,DefaultSerializeConfig.NULL_VIEW);
	}
}
