package org.smile.cache.simple;

import java.util.Iterator;

import org.smile.cache.Element;

public abstract class  CacheIterator<K,V>{

	private Iterator<? extends Element<K,V>> iterator;

	protected Element<K,V> nextValue;

	CacheIterator(AbstractCacheMap<K,V> abstractCacheMap) {
		iterator = abstractCacheMap.cacheMap.values().iterator();
		nextValue();
	}

	/**
	 * Resolves next value. 
	 * If next value doesn't exist, 
	 * next value will be <code>null</code>.
	 */
	protected void nextValue() {
		while (iterator.hasNext()) {
			nextValue = iterator.next();
			if (nextValue!=null&&nextValue.isExpired() == false) {
				return;
			}
		}
		nextValue = null;
	}

	/**
	 * Returns <code>true</code> if there are more elements in the cache.
	 */
	public boolean hasNext() {
		return nextValue != null;
	}

	/**
	 * Removes current non-expired element from the cache.
	 */
	public void remove() {
		iterator.remove();
	}
}
