package org.smile.db;

import org.smile.db.handler.HumpResultSetMap;

import junit.framework.TestCase;

public class TestBaseData extends TestCase{
	public void testMap() {
		HumpResultSetMap res=new HumpResultSetMap();
		res.put("first_Name", "胡");
		res.put("second_name", "真山");
		res.put("addressName", "真山");
		System.out.println(res);
		System.out.println(res.get("first_name"));
	}
	
	public void testRemove() {
		HumpResultSetMap res=new HumpResultSetMap();
		res.put("first_name", "胡");
		res.put("second_name", "真山");
		res.put("addressName", "真山");
		System.out.println(res);
		res.remove("first_name");
		System.out.println(res.get("firstName"));
	}
}
