package org.smile.collection;

import java.util.AbstractSet;
import java.util.Iterator;

public class NoCaseStringSet extends AbstractSet<String>{
	
	private transient KeyNoCaseHashMap<Object> map;

    // Dummy value to associate with an Object in the backing Map
    private static final Object PRESENT = new Object();
	
	public NoCaseStringSet(String ...strings ){
		map=new KeyNoCaseHashMap<Object>();
		if(strings!=null){
			for(String s:strings){
				add(s);
			}
		}
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean contains(Object o) {
		return map.containsKey(o);
	}

	@Override
	public Iterator<String> iterator() {
		return map.keySet().iterator();
	}


	@Override
	public boolean add(String e) {
		return map.put(e,PRESENT)==null;
	}

	@Override
	public boolean remove(Object o) {
		return map.remove(o)==PRESENT;
	}
}
