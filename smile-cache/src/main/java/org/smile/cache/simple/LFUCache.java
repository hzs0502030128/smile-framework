package org.smile.cache.simple;

import java.util.HashMap;
import java.util.Iterator;
/**
 * 即最不经常使用页置换算法的缓存
 * 以访问次数决定是否清除
 * @author 胡真山
 *
 * @param <K>
 * @param <V>
 */
public class LFUCache<K,V> extends AbstractCacheMap<K,V> {
	
	public LFUCache(){
		this(Short.MAX_VALUE);
	}

	public LFUCache(int maxSize) {
		this(maxSize, 0);
	}

	public LFUCache(int maxSize, long timeout) {
		this.cacheSize = maxSize;
		this.timeout = timeout;
		cacheMap = new HashMap<K, CacheObject<K,V>>(maxSize + 1);
	}

	// ---------------------------------------------------------------- prune

	/**
	 * Prunes expired and, if cache is still full, the LFU element(s) from the cache.
	 * On LFU removal, access count is normalized to value which had removed object.
	 * Returns the number of removed objects.
	 */
	@Override
	protected int pruneCache() {
        int count = 0;
		CacheObject<K,V> comin = null;
		// remove expired items and find cached object with minimal access count
		Iterator<CacheObject<K,V>> values = cacheMap.values().iterator();
		while (values.hasNext()) {
			CacheObject<K,V> co = values.next();
			if (co.isExpired() == true) {
				values.remove();
				onRemove(co.getKey(), co.getObject());
				count++;
				continue;
			}
			
			if (comin == null) {
				comin = co;
			} else {
				if (co.accessCount < comin.accessCount) {
					comin = co;
				}
			}
		}

		if (isFull() == false) {
			return count;
		}

		// decrease access count to all cached objects
		if (comin != null) {
			long minAccessCount = comin.accessCount;
			values = cacheMap.values().iterator();
			while (values.hasNext()) {
				CacheObject<K, V> co = values.next();
				co.accessCount -= minAccessCount;
				if (co.accessCount <= 0) {
					values.remove();
					onRemove(co.getKey(), co.getObject());
					count++;					
				}
			}
		}
		return count;
	}
}
