package org.smile.json.adapter;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
/**
 * 使用fastjson工具包实现
 * @author 胡真山
 *
 */
public class FastJsonAdpater extends  AbstractJsonAdpater {
	
	@Override
	public Map parseJSONObject(String json) {
		return JSONObject.parseObject(json);
	}

	@Override
	public List parseJSONArray(String json) {
		return JSONArray.parseArray(json);
	}

	@Override
	public String toJSONString(Object json) {
		return JSON.toJSONString(json);
	}

	@Override
	public String toJSONString(Collection json) {
		return JSONArray.toJSONString(json);
	}

	@Override
	public Object parseJSON(String json) {
		return JSON.parse(json);
	}

}
