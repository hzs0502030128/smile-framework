package org.smile.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.smile.collection.CollectionUtils;
import org.smile.util.StringUtils;

public class TestCollection extends TestCase{
	public void testConcat(){
		Collection a=CollectionUtils.linkedList("A","B");
		List b =CollectionUtils.linkedList("A","C","D");
		b.add(a);
		List c=CollectionUtils.concat(new LinkedList(), a,b);
		assertEquals("A,B,A,C,D,A,B", StringUtils.join(c,","));
		
		Set set=CollectionUtils.concat(new LinkedHashSet(), a,b);
		assertEquals("A,B,C,D", StringUtils.join(set,","));
		
	}
	
	public void testMap(){
		Map m=new HashMap(55);
		m.put(1, 11);
		m.put(65, 11);
		System.out.println(m);
	}
}
