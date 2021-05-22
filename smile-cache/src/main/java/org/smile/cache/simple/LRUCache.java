package org.smile.cache.simple;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Iterator;

/**
 * 近期最少使用算法
 * @author 胡真山
 *
 * @param <K>
 * @param <V>
 */
public class LRUCache<K, V> extends AbstractCacheMap<K, V> {

	public LRUCache(int cacheSize) {
		this(cacheSize, 0);
	}

	/**
	 * Creates a new LRU cache.
	 */
	public LRUCache(int cacheSize, long timeout) {
		this.cacheSize = cacheSize;
		this.timeout = timeout;
		cacheMap = new LinkedHashMap<K, CacheObject<K,V>>(cacheSize + 1, 1.0f, true) {
			/**实现 LRU 当长度大于最大时 移除最前面的元素  参考  accessOrder*/
			@Override
			protected boolean removeEldestEntry(Map.Entry eldest) {
				return LRUCache.this.removeEldestEntry(size());
			}
		};
	}

	/**
	 * Removes the eldest entry if current cache size exceed cache size.
	 */
	protected boolean removeEldestEntry(int currentSize) {
		if (cacheSize == 0) {
			return false;
		}
		return currentSize > cacheSize;
	}

	// ---------------------------------------------------------------- prune

	/**
	 * Prune only expired objects, <code>LinkedHashMap</code> will take care of LRU if needed.
	 */
	@Override
	protected int pruneCache() {
		if (isPruneExpiredActive() == false) {
			return 0;
		}
        int count = 0;
		Iterator<CacheObject<K,V>> values = cacheMap.values().iterator();
		while (values.hasNext()) {
			CacheObject<K,V> co = values.next();
			if (co.isExpired() == true) {
				values.remove();
				count++;
			}
		}
		return count;
	}
}
