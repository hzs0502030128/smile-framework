package org.smile.collection;

import java.util.Map;


public final class UnmodifiableMap<K, V> extends AbstractMapDecorator<K, V>{
	
	public UnmodifiableMap(Map<K, V> map){
		super(map);
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public V put(K key, V value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> mapToCopy) {
		throw new UnsupportedOperationException();
	}

	@Override
	public V remove(Object key) {
		throw new UnsupportedOperationException();
	}
	
	
}
