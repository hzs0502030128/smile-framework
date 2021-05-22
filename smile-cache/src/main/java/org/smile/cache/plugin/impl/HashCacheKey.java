package org.smile.cache.plugin.impl;

import org.smile.cache.plugin.CacheKey;

public class HashCacheKey implements CacheKey<HashCacheKey> {

	protected final int hashCode;

	protected String value;

	public HashCacheKey(String key, String paramValues) {
		this.value=key+paramValues;
		this.hashCode = value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null){
			return false;
		}
		if(obj instanceof CacheKey){
			HashCacheKey other = (HashCacheKey) obj;
			if (hashCode != other.hashCode()){
				return false;
			}
			return value.equals(other.value);
		}
		return false;
	}

	@Override
	public final int hashCode() {
		return hashCode;
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public HashCacheKey toKey() {
		return this;
	}
}
