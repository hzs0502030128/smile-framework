package org.smile.cache.simple;

import org.smile.cache.CacheModel;
import org.smile.cache.SimpleElement;

/**
 * 缓存的对象
 * @author 胡真山
 */
public class CacheObject<K,V> extends SimpleElement<K,V>{
	/**最后访问时间*/
	long lastAccess;		
	/**访问次数*/
	long accessCount;		
	/**
	 * 过期时间 objects timeout (time-to-live), 0 = no timeout
	 */
	long timeout;
	/**
	 * @param <K>  缓存对象的键
	 * @param <V>  缓存的对象
	 * @param key
	 * @param object
	 * @param ttl
	 */
	CacheObject(K key, V object, long ttl) {
		this.key = key;
		this.cachedObject = object;
		this.timeout = ttl;
		this.lastAccess = System.currentTimeMillis();
	}
	
	CacheObject(CacheModel<K, V> model,long timeout){
		this.key=model.getKey();
		this.cachedObject=model.getObject();
		this.timeout=timeout;
		this.lastAccess=System.currentTimeMillis();
	}

					
	/**
	 * 是事已经过期
	 * @return
	 */
	@Override
	public boolean isExpired() {
		if (timeout == 0) {
			return false;
		}
		return lastAccess + timeout < System.currentTimeMillis();
	}
	/**
	 * 缓存的对象
	 * @return
	 */
	@Override
	public V getObject() {
		lastAccess = System.currentTimeMillis();
		accessCount++;
		return cachedObject;
	}

	@Override
	public K getKey() {
		return key;
	}

	@Override
	public long lastAccessTime() {
		return lastAccess;
	}

	@Override
	public long accessCount() {
		return accessCount;
	}
}
