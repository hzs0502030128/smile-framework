package org.smile.cache.ecache;

import java.util.Collection;
import java.util.Iterator;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.smile.cache.Cache;
import org.smile.cache.CacheModel;
import org.smile.cache.CacheNameAware;
import org.smile.cache.CacheWrap;
import org.smile.cache.CacheWrapException;
import org.smile.cache.CacheWrapSupport;

/**
 * 使用ehcache实现封装的缓存
 * @author 胡真山
 *
 * @param <K>
 * @param <V>
 */
public class BaseEcache<K, V> implements Cache<K, V> ,CacheWrapSupport,CacheNameAware{

	protected net.sf.ehcache.Cache ecache;

	public BaseEcache() {}

	public BaseEcache(net.sf.ehcache.Cache cache) {
		this.ecache = cache;
	}

	@Override
	public long getCacheSize() {
		return ecache.getSize();
	}

	@Override
	public long getCacheTimeout() {
		return -1;
	}

	@Override
	public void put(K key, V object) {
		ecache.put(new Element(key, object));
	}

	@Override
	public void put(K key, V object, long timeout) {
		ecache.put(new Element(key, object, 0, convertTime(timeout)));
	}

	@Override
	public V get(K key) {
		Element e = ecache.get(key);
		if (e != null) {
			return (V) e.getObjectValue();
		}
		return null;
	}

	@Override
	public Iterator<V> iterator() {
		return new EcacheValueIterator();
	}

	@Override
	public int prune() {
		return 0;
	}

	@Override
	public boolean isFull() {
		return false;
	}

	@Override
	public void remove(K key) {
		ecache.remove(key);
	}

	@Override
	public void clear() {
		ecache.removeAll();
	}

	@Override
	public long size() {
		return ecache.getSize();
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public void put(CacheModel<K, V> model, long timeout) {
		ecache.put(new Element(model.getKey(), model.getObject()));
	}

	@Override
	public String getName() {
		return ecache.getName();
	}

	@Override
	public void remove(Collection<K> keys) {
		for (K k : keys) {
			ecache.remove(k);
		}
	}

	public void setEcache(net.sf.ehcache.Cache ecache) {
		this.ecache = ecache;
	}
	
	/**
	 * 重新包装
	 * @param wrap
	 */
	@Override
	public void setCacheWrap(CacheWrap wrap){
		Cache cache=wrap.getCache();
		if(cache instanceof BaseEcache){
			this.ecache=((BaseEcache) cache).getEcache();
		}else{
			throw new CacheWrapException("can wrap "+cache +" to "+getClass());
		}
	}

	protected int convertTime(long time) {
		return (int) (time / 1000);
	}

	public net.sf.ehcache.Cache getEcache() {
		return ecache;
	}

	@Override
	public Iterator<org.smile.cache.Element<K, V>> elements() {
		return new EcacheElementIterator();
	}
	
	class EcacheIterator{
		protected EcacheIterator(){
			nextValue();
		}
		
		Iterator<K> iterator=ecache.getKeys().iterator();
		
		Element nextValue;
		/**
		 * Resolves next value. 
		 * If next value doesn't exist, 
		 * next value will be <code>null</code>.
		 */
		protected void nextValue() {
			while (iterator.hasNext()) {
				nextValue = ecache.get(iterator.next());
				if (nextValue!=null&&nextValue.isExpired() == false) {
					return;
				}
			}
			nextValue = null;
		}

		/**
		 * Returns <code>true</code> if there are more elements in the cache.
		 */
		public boolean hasNext() {
			return nextValue != null;
		}

		/**
		 * Removes current non-expired element from the cache.
		 */
		public void remove() {
			iterator.remove();
		}
	}
	
	class EcacheValueIterator extends EcacheIterator implements Iterator<V>{
		@Override
		public V next() {
			V v= (V) nextValue.getObjectValue();
			nextValue();
			return v;
		}
	}
	
	class EcacheElementIterator extends EcacheIterator implements Iterator<org.smile.cache.Element<K, V>>{
		@Override
		public org.smile.cache.Element<K, V> next() {
			org.smile.cache.Element<K, V> next=new EcacheElement<K,V>(nextValue);
			nextValue();
			return next;
		}
		
	}

	@Override
	public void setCacheName(String name) {
		this.ecache=CacheManager.getInstance().getCache(name);
	}

	@Override
	public Collection<K> keys() {
		return ecache.getKeys();
	}

	@Override
	public Collection<K> noExpiredKeys() {
		return ecache.getKeysWithExpiryCheck();
	}

}
