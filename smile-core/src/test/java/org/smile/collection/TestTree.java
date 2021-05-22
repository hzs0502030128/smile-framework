package org.smile.collection;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

public class TestTree {
	@Test
	public void testBtree(){
		Map<Integer, Integer> st = new BTreeMap<Integer, Integer>();
		HashMap<Integer,Integer> hm=new LinkedHashMap<Integer,Integer>();
		long start=System.currentTimeMillis();
		for(int i=0;i<=1000;i++){
			st.put(i,i);
		}
		for(int j=0;j<5;j++){
			start=System.currentTimeMillis();
			for(int i=0;i<=1000000;i++){
				st.put(i,i);
			}
		}
		
		start=System.currentTimeMillis();
		for(int i=0;i<=1000000;i++){
			st.get(i);
		}
//		
//		System.out.println(st.lowerEntry(86));
//		System.out.println(st.higherEntry(4));
//		System.out.println(st.lastKey());
//		System.out.println(st.firstKey());
//		System.out.println(st.values());
//		System.out.println(st.keySet());
//		System.out.println(st.entrySet());
//		System.out.println(st.headMap(10));
//		System.out.println(st.tailMap(90));
	}
	
	@Test
	public void testTreeMap(){
		TreeMap map=new TreeMap();
		for(int i=1;i<=16;i+=2){
			map.put(i,i);
		}
		System.out.println(map.lowerEntry(3));
		System.out.println(map.higherEntry(5));
		System.out.println(map.ceilingEntry(5));
		System.out.println(map.floorEntry(4));
		Map subMap=map.subMap(3, 10);
		System.out.println(subMap);
		map.put(4, 4);
		map.put(6, 6);
		subMap.put(8, 8);
		System.out.println(subMap);
		System.out.println(map);
	}
	@Test
	public void testBtreeRemove(){
		BTreeMap<Integer, Integer> map=new BTreeMap<Integer, Integer>();
		for(int i=1;i<=16;i+=2){
			map.put(i,i);
		}
		map.remove(7);
		map.remove(5);
		map.put(7, 7);
		map.put(5, 5);
		map.put(6, 6);
		map.put(4, 4);
		map.put(8, 8);
		map.remove(13);
		map.remove(15);
		map.remove(9);
		map.remove(11);
		System.out.println(map.keySet());
		System.out.println(map.values());
		System.out.println(map.subMap(2, 6));
		for(Integer i:map.keySet()){
			if(i==5){
				map.remove(i);
			}
		}
		System.out.println(map.tailMap(3));
	}
}
