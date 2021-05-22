package org.smile.collection;

import java.util.Map;

public class BaseMapEntry<K,V> implements Map.Entry<K, V>{
	K k;
	V v;
	public BaseMapEntry(K k,V v){
		this.k=k;
		this.v=v;
	}
	
	public BaseMapEntry(){}
	
	public BaseMapEntry(Map.Entry<K,V> entry){
		this.k=entry.getKey();
		this.v=entry.getValue();
	}
	
	@Override
	public K getKey() {
		return k;
	}

	@Override
	public V getValue() {
		return v;
	}

	@Override
	public V setValue(V value) {
		return v=value;
	}
	
	public void setKey(K k){
		this.k=k;
	}

	@Override
	public String toString() {
		return k+":"+v;
	}
	
	

}
