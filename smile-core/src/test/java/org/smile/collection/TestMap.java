package org.smile.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import junit.framework.TestCase;

public class TestMap extends TestCase {
	@Test
	public void testWeakMap(){
		Map map=new WeakHashMap();
		map.put("name",new String("胡真山"));
		System.out.println(map);
		System.gc();
		System.out.println(map);
	}
	@Test
	public void testThreadLocalMap(){
		ThreadLocalMap<String, String> map=new ThreadLocalMap();
		Map<String, String> realMap=map.get();
		realMap.put("string", "胡真山");
		System.out.println(map);
	}
	@Test
	public void testSoftMap(){
		Map map=new SoftHashMap();
		int l=100;
		List list=new ArrayList(l);
		for(int i=0;i<l;i++){
			map.put(i,new byte[1024*1024]);
		}
		System.out.println(map.size()+":"+map.keySet());
	}
	@Test
	public void testExtendsMap() {
		ExtendsMap map=new ExtendsMap(MapUtils.hashMap("key", 123));
		map.put("old", 234);
		map.put("old2", 2343);
		assertEquals(map.keySet(),CollectionUtils.hashSet("old","old2","key"));
	}
	
	public void testLikeMap() {
		KeyLikeHashMap<Object> map=new KeyLikeHashMap<Object>();
		map.put("first_name", "胡");
		map.put("second_name", "真山");
		map.put("firstName", "李");
		assertEquals(map.get("first_name"),"李");
	}
	
	public void testBoolean() {
		KeyLikeHashMap<Object> map=new KeyLikeHashMap<Object>();
		map.put("first_name", "胡");
		map.put("bool", new Byte((byte)1));
		map.put("firstName", "李");
		assertEquals(map.getBoolean("bool"),Boolean.TRUE);
	}
}
