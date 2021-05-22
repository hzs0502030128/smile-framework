package org.smile.cache.plugin.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.smile.cache.plugin.CacheKey;
import org.smile.cache.plugin.CacheKeyOperator;
import org.smile.cache.plugin.ann.CacheWipe;

public class LayerEhcacheKeyOperator implements CacheKeyOperator<String[],LayerEhcache<Object>>{
	@Override
	public void remove(LayerEhcache<Object> cache, CacheKey<String[]> key, CacheWipe wipe) {
		if(wipe.ignoreArgs()){
			cache.removeNode(key.toKey());
		}else{
			cache.remove(key.toKey());
		}
	}

	@Override
	public void add(LayerEhcache<Object> cache, CacheKey<String[]> key, Object value, long timeout) {
		cache.put(key.toKey(), value, timeout);
	}

	@Override
	public void remove(LayerEhcache<Object> cache, Collection<CacheKey<String[]>> keys) {
		List<String[]> list=new LinkedList<String[]>();
		for(CacheKey<String[]> k:keys){
			list.add(k.toKey());
		}
		cache.remove(list);
	}

	@Override
	public Object get(LayerEhcache<Object> cache, CacheKey<String[]> key) {
		return cache.get(key.toKey());
	}

	@Override
	public void add(LayerEhcache<Object> cache, CacheKey<String[]> key, Object value) {
		cache.put(key.toKey(), value);
	}
}
