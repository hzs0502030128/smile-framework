package org.smile.collection;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class TestCollections extends TestCase{
	
	public void testConcat() {
		List<Long> list=CollectionUtils.concat(new ArrayList<Long>(), CollectionUtils.linkedHashSet(1,2,3));
		assertEquals(list, CollectionUtils.arrayList(1,2,3));
	}
	
	public void testResultMap() {
		ResultMap map=MapUtils.resultMap();
		map.put("age", "1");
		assertEquals(new Integer(1), map.getInt("age"));
		assertEquals(new Boolean(true), map.getBoolean("age"));
	}
}
