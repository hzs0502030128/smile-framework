package org.smile.cache.redis;

import org.smile.cache.SimpleElement;

public class RedisCacheElement<K,V> extends SimpleElement<K,V>{
	public RedisCacheElement(K k,V v){
		this.key=k;
		this.cachedObject=v;
	}
}
