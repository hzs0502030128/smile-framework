package org.smile.cache.simple;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.smile.cache.Cache;
import org.smile.cache.CacheModel;
import org.smile.cache.Element;

/**
 * Default implementation of timed and size cache map.
 * Implementations should:
 * <ul>
 * <li>create a new cache map</li>
 * <li>implements own <code>prune</code> strategy</li>
 * </ul>
 * Uses <code>ReentrantReadWriteLock</code> to synchronize access.
 * Since upgrading from a read lock to the write lock is not possible,
 * be careful withing {@link #get(Object)} method.
 */
public abstract class AbstractCacheMap<K, V> implements Cache<K, V> {
	/**记录获取到的次数*/
	protected int hitCount;
	/**记录没有获取到的次数*/
	protected int missCount;

	protected Map<K, CacheObject<K, V>> cacheMap;
	//读写锁
	private final ReadWriteLock cacheLock = new ReentrantReadWriteLock();
	private final Lock readLock = cacheLock.readLock();
	private final Lock writeLock = cacheLock.writeLock();
	// max cache size, 0 = no limit
	protected int cacheSize;
	@Override
	public long getCacheSize() {
		return cacheSize;
	}

	// default timeout, 0 = no timeout
	protected long timeout;
	/**
	 * 缓存的名称
	 */
	protected String name;

	@Override
	public long getCacheTimeout() {
		return timeout;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Identifies if objects has custom timeouts.
	 * Should be used to determine if prune for existing objects is needed.
	 */
	protected boolean existCustomTimeout;

	/**
	 * Returns <code>true</code> if prune of expired objects should be invoked.
	 * For internal use.
	 */
	protected boolean isPruneExpiredActive() {
		return (timeout != 0) || existCustomTimeout;
	}

	@Override
	public void put(K key, V object) {
		put(key, object, timeout);
	}

	@Override
	public void put(CacheModel<K, V> model, long timeout) {
		put(model.getKey(), model.getObject(), timeout);
	}

	@Override
	public void put(K key, V object, long timeout) {
		writeLock.lock();
		try {
			CacheObject<K, V> co = new CacheObject<K, V>(key, object, timeout);
			if (timeout != 0) {
				existCustomTimeout = true;
			}
			if (isFull()) {
				pruneCache();
			}
			cacheMap.put(key, co);
		} finally {
			writeLock.unlock();
		}
	}

	public int getHitCount() {
		return hitCount;
	}

	public int getMissCount() {
		return missCount;
	}

	@Override
	public V get(K key) {
		readLock.lock();
		try {
			CacheObject<K, V> co = cacheMap.get(key);
			if (co == null) {
				missCount++;
				return null;
			}
			if (co.isExpired() == true) {
				cacheMap.remove(key);
				missCount++;
				return null;
			}
			hitCount++;
			return co.getObject();
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public Iterator<V> iterator() {
		return new CacheValuesIterator<K,V>(this);
	}

	protected abstract int pruneCache();

	@Override
	public final int prune() {
		writeLock.lock();
		try {
			return pruneCache();
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public boolean isFull() {
		if (cacheSize == 0) {
			return false;
		}
		return cacheMap.size() >= cacheSize;
	}

	@Override
	public void remove(K key) {
		writeLock.lock();
		try {
			cacheMap.remove(key);
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public void remove(Collection<K> keys) {
		writeLock.lock();
		try {
			for (K k : keys) {
				cacheMap.remove(k);
			}
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public void clear() {
		writeLock.lock();
		try {
			cacheMap.clear();
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public long size() {
		return cacheMap.size();
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}
	/**
	 * 设置缓存的最大数据限制
	 * @param cacheSize
	 */
	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize;
	}

	@Override
	public Iterator<Element<K, V>> elements() {
		return new CacheElementIterator<K, V>(this);
	}

	@Override
	public Collection<K> keys() {
		readLock.lock();
		try{
			return Collections.unmodifiableSet(cacheMap.keySet());
		}finally{
			readLock.unlock();
		}
	}

	@Override
	public Collection<K> noExpiredKeys() {
		writeLock.lock();
		try{
			// remove expired items and find cached object expired
			Iterator<CacheObject<K,V>> values = cacheMap.values().iterator();
			Set<K> keys=new LinkedHashSet<K>();
			while (values.hasNext()) {
				CacheObject<K,V> co = values.next();
				if (co.isExpired() == true) {
					values.remove();
					onRemove(co.getKey(), co.getObject());
					continue;
				}
				keys.add(co.getKey());
			}
			return keys;
		}finally{
			writeLock.unlock();
		}
	}
	
	
	/**
	 * Callback method invoked on cached object removal.
	 * By default does nothing.
	 */
	protected void onRemove(K key, V cachedObject) {}
	
}
