package org.smile.cache.plugin.impl;

import org.smile.cache.plugin.CacheKey;
import org.smile.cache.plugin.ann.CacheWipe;
import org.smile.plugin.Invocation;


public class LayerKeyGenerator extends DefaultKeyGenerator{
	
	@Override
	public CacheKey generateWipe(Invocation invocation, CacheWipe wipe) {
		CacheKey key=super.generateWipe(invocation, wipe);
		if(wipe.ignoreArgs()){
			((LayerCacheKey)key).onlyKey();
		}
		return key;
	}

	@Override
	protected CacheKey createCacheKey(String key, String paramValues) {
		return new LayerCacheKey(key, paramValues);
	}
	
}
