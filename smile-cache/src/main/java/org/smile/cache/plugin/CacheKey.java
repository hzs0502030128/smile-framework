package org.smile.cache.plugin;

import java.io.Serializable;

public interface CacheKey<K> extends Serializable{
	/***
	 * 转成缓存的key
	 * @return
	 */
	public K toKey();
}
