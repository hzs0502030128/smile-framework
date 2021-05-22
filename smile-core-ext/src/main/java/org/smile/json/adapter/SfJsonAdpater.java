package org.smile.json.adapter;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.smile.util.DateUtils;

public class SfJsonAdpater extends  AbstractJsonAdpater {
	
	protected JsonConfig config=new JsonConfig();
	
	public SfJsonAdpater(){
		config.registerJsonValueProcessor(Date.class,new JsonValueProcessor() {
			@Override
			public Object processObjectValue(String key, Object value, JsonConfig c) {
				return DateUtils.defaultFormat((Date)value);
			}
			
			@Override
			public Object processArrayValue(Object value, JsonConfig c) {
				return DateUtils.defaultFormat((Date)value);
			}
		});
		config.registerJsonValueProcessor(Timestamp.class,new JsonValueProcessor() {
			@Override
			public Object processObjectValue(String key, Object value, JsonConfig c) {
				return DateUtils.defaultFormat((Date)value);
			}
			
			@Override
			public Object processArrayValue(Object value, JsonConfig c) {
				return DateUtils.defaultFormat((Date)value);
			}
		});
		config.registerJsonValueProcessor(java.sql.Date.class,new JsonValueProcessor() {
			@Override
			public Object processObjectValue(String key, Object value, JsonConfig c) {
				return DateUtils.formatOnlyDate((Date)value);
			}
			
			@Override
			public Object processArrayValue(Object value, JsonConfig c) {
				return DateUtils.formatOnlyDate((Date)value);
			}
		});
	}
	
	@Override
	public Map parseJSONObject(String json) {
		return JSONObject.fromObject(json);
	}

	@Override
	public List parseJSONArray(String json) {
		return JSONArray.fromObject(json,config);
	}

	@Override
	public String toJSONString(Object json) {
		if(json instanceof Collection){
			return JSONArray.fromObject(json,config).toString();
		}else if(json instanceof Object[]){
			return JSONArray.fromObject(json,config).toString();
		}
		return JSONObject.fromObject(json,config).toString();
	}

	@Override
	public String toJSONString(Collection json) {
		return JSONArray.fromObject(json,config).toString();
	}

	@Override
	public Object parseJSON(String json) {
		return JSONSerializer.toJSON(json);
	}
}
