package org.smile.json;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import org.smile.beans.BeanUtils;
import org.smile.beans.converter.BeanException;
import org.smile.collection.MapUtils;
import org.smile.collection.ResultMap;
import org.smile.json.format.SimpleSerializeConfig;
import org.smile.json.parser.JSONParseException;
import org.smile.log.LoggerHandler;

import junit.framework.TestCase;

public class TestJson extends TestCase{
	public void testJson() throws IOException, JSONParseException, BeanException{
		String json="{n:{n:'123'}}";
		JSONObject obj=new JSONObject(json);
		String s=(String) BeanUtils.getExpValue(obj, "n.n");
		System.out.println(new JSONString(s));
		obj.put("json", "name</age");
		obj.put("json2", new JSONObject(json));
		obj.put("date", new Date());
		obj.put("sqlDate", new java.sql.Date(System.currentTimeMillis()));
		JSONArray array=new JSONArray();
		array.add(new JSONObject(json));
		array.add(new JSONObject(json));
		obj.put("array",array);
		System.out.println(obj);
		obj=new JSONObject(obj.toJSONString());
		System.out.println(obj);
		JSONWriter writer=new JSONIOWriter(new FileWriter("d:/json.txt"),new SimpleSerializeConfig());
		obj.serializeWriter(writer);
		LoggerHandler.logger.info("2222");
	}
	
	public void testJsonMap() {
		JSONObject json=new JSONObject(MapUtils.hashMap("String", (Object)5));
		ResultMap m=MapUtils.resultMap();
		m.put("2", 34);
		json=new JSONObject(m);
	}
}
