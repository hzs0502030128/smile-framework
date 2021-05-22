package org.smile.cache.simple;

import java.util.LinkedHashMap;
import java.util.Iterator;
/**
 * 先进行出算法缓存
 * @author 胡真山
 *
 * @param <K>
 * @param <V>
 */
public class FIFOCache<K, V> extends AbstractCacheMap<K, V> {

	public FIFOCache(int cacheSize) {
		this(cacheSize, 0);
	}

	/**
	 * Creates a new LRU cache.
	 */
	public FIFOCache(int cacheSize, long timeout) {
		this.cacheSize = cacheSize;
		this.timeout = timeout;
		cacheMap = new LinkedHashMap<K,CacheObject<K,V>>(cacheSize + 1, 1.0f, false);
	}


	// ---------------------------------------------------------------- prune

	/**
	 * Prune expired objects and, if cache is still full, the first one.
	 */
	@Override
	protected int pruneCache() {
        int count = 0;
		CacheObject<K,V> first = null;
		Iterator<CacheObject<K,V>> values = cacheMap.values().iterator();
		while (values.hasNext()) {
			CacheObject<K,V> co = values.next();
			if (co.isExpired() == true) {
				values.remove();
				count++;
			}
			if (first == null) {
				first = co;
			}
		}
		if (isFull()) {
			if (first != null) {
				cacheMap.remove(first.getKey());
				count++;
			}
		}
		return count;
	}
}
