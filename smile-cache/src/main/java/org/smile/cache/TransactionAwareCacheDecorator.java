package org.smile.cache;

import java.util.Collection;
import java.util.Iterator;

public abstract class TransactionAwareCacheDecorator<K, V> implements Cache<K, V> {

	private final Cache<K, V> targetCache;

	/**
	 * Create a new TransactionAwareCache for the given target Cache.
	 * 
	 * @param targetCache
	 *            the target Cache to decorate
	 */
	public TransactionAwareCacheDecorator(Cache<K, V> targetCache) {
		if (targetCache == null) {
			throw new IllegalArgumentException("Target Cache must not be null");
		}
		this.targetCache = targetCache;
	}

	@Override
	public String getName() {
		return this.targetCache.getName();
	}

	@Override
	public void put(final K key, final V value) {
		if (isSynTransaction()) {
			registerPut(key);
		} else {
			this.targetCache.put(key, value);
		}
	}

	@Override
	public void remove(final K key) {
		if (isSynTransaction()) {
			registerRemove(key);
		}else {
			this.targetCache.remove(key);
		}
	}

	protected abstract boolean isSynTransaction();

	protected abstract void registerPut(final K key);

	protected abstract void registerRemove(final K key);

	@Override
	public void clear() {
		this.targetCache.clear();
	}

	@Override
	public long getCacheSize() {
		return this.targetCache.getCacheSize();
	}

	@Override
	public long getCacheTimeout() {
		return this.targetCache.getCacheTimeout();
	}

	@Override
	public void put(K key, V object, long timeout) {
		this.targetCache.put(key, object, timeout);
	}

	@Override
	public void put(CacheModel<K, V> model, long timeout) {
		this.targetCache.put(model, timeout);
	}

	@Override
	public V get(K key) {
		return this.targetCache.get(key);
	}

	@Override
	public Iterator<V> iterator() {
		return this.targetCache.iterator();
	}

	@Override
	public Iterator<Element<K, V>> elements() {
		return this.targetCache.elements();
	}

	@Override
	public int prune() {
		return this.targetCache.prune();
	}

	@Override
	public boolean isFull() {
		return this.targetCache.isFull();
	}

	@Override
	public void remove(Collection<K> keys) {
		this.targetCache.remove(keys);
	}

	@Override
	public long size() {
		return this.targetCache.size();
	}

	@Override
	public boolean isEmpty() {
		return this.targetCache.isEmpty();
	}

	@Override
	public Collection<K> keys() {
		return this.targetCache.keys();
	}

	@Override
	public Collection<K> noExpiredKeys() {
		return this.targetCache.noExpiredKeys();
	}
}
