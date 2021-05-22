package org.smile.cache.simple;

import java.util.Iterator;

public class CacheValuesIterator<K,V> extends CacheIterator<K,V> implements Iterator<V>{

	CacheValuesIterator(AbstractCacheMap<K, V> abstractCacheMap) {
		super(abstractCacheMap);
	}

	@Override
	public V next() {
		V next=nextValue.getObject();
		nextValue();
		return next;
	}

}
