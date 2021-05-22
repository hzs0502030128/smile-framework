package org.smile.cache.plugin.impl;

import org.smile.cache.simple.CacheObject;
import org.smile.cache.simple.LFUCache;
import org.smile.collection.ArrayHashMap;
/**
 * 内有层级结构的缓存实现
 * @author 胡真山
 *
 * @param <V>
 */
public class LayerLFUCache<V> extends LFUCache<String[],V> {
	
	public LayerLFUCache(){
		this(Short.MAX_VALUE);
	}

	public LayerLFUCache(int maxSize) {
		this(maxSize, 0);
	}

	private LayerLFUCache(int maxSize, long timeout) {
		this.cacheSize = maxSize;
		this.timeout = timeout;
		cacheMap = new ArrayHashMap<String,CacheObject<String[],V>>();
	} 
	
	public void removeNode(String[] key){
		((ArrayHashMap<String,CacheObject<String[],V>>)cacheMap).removeNode(key);
	}
}
