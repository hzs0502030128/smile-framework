package org.smile.core;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.smile.json.JSONString;
import org.smile.json.JSONValue;

public class JsonTest extends TestCase{
	public void testString(){
		String json="abcer\\/ss/ss\\dd";
		Map m=new HashMap();
		m.put("name", json);
		JSONString js=new JSONString(json);
		System.out.println(JSONValue.toJSONString(m));
		System.out.println(js);
	}
}
