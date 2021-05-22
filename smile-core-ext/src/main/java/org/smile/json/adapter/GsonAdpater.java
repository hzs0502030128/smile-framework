package org.smile.json.adapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.smile.reflect.TypeToken;

import com.google.gson.Gson;
/**
 * 使用fastjson工具包实现
 * @author 胡真山
 *
 */
public class GsonAdpater extends  AbstractJsonAdpater {
	
	@Override
	public Map parseJSONObject(String json) {
		return new Gson().fromJson(json, HashMap.class);
	}

	@Override
	public List parseJSONArray(String json) {
		return new Gson().fromJson(json, ArrayList.class);
	}

	@Override
	public String toJSONString(Object json) {
		return new Gson().toJson(json);
	}

	@Override
	public String toJSONString(Collection json) {
		return  new Gson().toJson(json);
	}

	@Override
	public Object parseJSON(String json) {
		return  new Gson().fromJson(json,Object.class);
	}

	@Override
	public <T> T parseJSONObject(String json, Class<T> javaType) {
		return new Gson().fromJson(json,javaType);
	}

	@Override
	public <T> List<T> parseJSONArray(String json, Class<T> javaType) {
		Type type  = new TypeToken<List<T>>(javaType){}.getType();
		return new Gson().fromJson(json,type);
	}
}
