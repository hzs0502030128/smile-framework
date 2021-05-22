package org.smile.json.adapter;

import java.util.List;
import java.util.Map;

import org.smile.commons.SmileRunException;
import org.smile.json.JSONArray;
import org.smile.json.JSONObject;
import org.smile.json.JSONValue;
import org.smile.json.parser.JSONParseException;
/**
 * 使用smile json 工具实现
 * @author 胡真山
 *
 */
public class SmileJsonAdpater extends  AbstractJsonAdpater {

	@Override
	public Map parseJSONObject(String json){
		try{
			return new JSONObject(json);
		}catch(JSONParseException e){
			throw new SmileRunException("parse json exception",e);
		}
	}

	@Override
	public List parseJSONArray(String json){
		try{
			return new JSONArray(json);
		}catch(JSONParseException e){
			throw new SmileRunException("parse json exception",e);
		}
	}


	@Override
	public Object parseJSON(String json) {
		try {
			return JSONValue.parse(json);
		} catch (JSONParseException e) {
			throw new SmileRunException("parse json exception",e);
		}
	}
}
