package org.smile.cache.ecache;

import org.smile.cache.Element;

public class EcacheElement<K, V> implements Element<K, V> {

	private net.sf.ehcache.Element element;

	protected EcacheElement(net.sf.ehcache.Element ele) {
		this.element = ele;
	}

	@Override
	public K getKey() {
		return (K) element.getObjectKey();
	}

	@Override
	public V getObject() {
		return (V) element.getObjectValue();
	}

	@Override
	public long lastAccessTime() {
		return element.getLastAccessTime();
	}

	@Override
	public long accessCount() {
		return element.getHitCount();
	}

	@Override
	public boolean isExpired() {
		return element.isExpired();
	}

}
