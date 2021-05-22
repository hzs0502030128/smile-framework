package org.smile.cache.plugin.impl;

import org.smile.cache.plugin.CacheKey;

public class DefaultCacheKey implements CacheKey<String> {

	protected String value;

	public DefaultCacheKey(String key,String paramValues) {
		this.value=key+'-'+paramValues;
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public String toKey() {
		return value;
	}
}
