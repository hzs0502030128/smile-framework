
package org.smile.cache.simple;

import java.util.Collection;
import java.util.Iterator;

import org.smile.cache.Cache;
import org.smile.cache.CacheModel;
import org.smile.cache.Element;

/**
 * Simple no-cache implementations of {@link Cache} for situation when cache
 * needs to be quickly turned-off.
 */
public class NoCache<K, V> implements Cache<K, V> {


	@Override
	public long getCacheSize() {
		return 0;
	}

	public long getCacheTimeout() {
		return 0;
	}

	public void put(K key, V object) {
		// ignore
	}

	public void put(K key, V object, long timeout) {
		// ignore
	}

	public V get(K key) {
		return null;
	}

	public Iterator<V> iterator() {
		return null;
	}

	public int prune() {
		return 0;
	}

	public boolean isFull() {
		return true;
	}

	public void remove(K key) {
		// ignore
	}

	public void clear() {
		// ignore
	}

	@Override
	public long size() {
		return 0;
	}

	public boolean isEmpty() {
		return true;
	}

	@Override
	public void put(CacheModel<K, V> model, long timeout) {
		// ignore
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public void remove(Collection<K> keys) {
		
	}

	@Override
	public Iterator<Element<K, V>> elements() {
		return null;
	}

	@Override
	public Collection<K> keys() {
		return null;
	}

	@Override
	public Collection<K> noExpiredKeys() {
		return null;
	}
}
