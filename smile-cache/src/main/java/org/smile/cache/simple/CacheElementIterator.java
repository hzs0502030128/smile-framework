package org.smile.cache.simple;

import java.util.Iterator;

import org.smile.cache.Element;

public class CacheElementIterator<K,V> extends CacheIterator<K,V> implements Iterator<Element<K,V>>{

	CacheElementIterator(AbstractCacheMap<K, V> abstractCacheMap) {
		super(abstractCacheMap);
	}

	@Override
	public Element<K,V> next() {
		Element<K,V> next= nextValue;
		nextValue();
		return next;
	}

}
