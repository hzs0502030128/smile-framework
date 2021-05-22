package org.smile.cache;

public class SimpleElement<K,V> implements Element<K,V>{

	protected K key;
	
	protected V cachedObject;
	
	@Override
	public K getKey() {
		return key;
	}

	@Override
	public V getObject() {
		return cachedObject;
	}

	@Override
	public long lastAccessTime() {
		return 0;
	}

	@Override
	public long accessCount() {
		return 0;
	}

	@Override
	public boolean isExpired() {
		return false;
	}

}
