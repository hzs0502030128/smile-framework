package org.smile.collection;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * 弱引用map
 * @author 胡真山
 * 此实现与{@link java.util.WeakHashMap}的实现略有区别
 * 
 * {@link java.util.WeakHashMap} 是对键的弱引用 
 * 
 * 本类是对值的弱引用  
 * 
 * @param <K>
 * @param <V>
 */
public class WeakHashMap<K,V> extends ReferenceHashMap<K,V>{
	public WeakHashMap(){
		super();
	}
	
	public WeakHashMap(Map<K,RefValue<K,V>> map){
		super(map);
	}
	
	public static <K,V> WeakHashMap<K,V> newConcurrentInstance(){
		return new WeakHashMap<K,V>(new ConcurrentHashMap<K, RefValue<K,V>>());
	}
	
	@Override
	protected RefValue<K,V> newRefValue(K k, V v) {
		return new WeakRefValue(k, v);
	}
	
	class WeakRefValue extends WeakReference<V> implements  RefValue<K, V>{
		K key;
		/***
		 * 使用的是 value做为参考的引用
		 * @param k
		 * @param v
		 */
		WeakRefValue(K k, V v) {
			super(v, rq);
			this.key=k;
		}
		@Override
		public K getKey() {
			return key;
		}
		@Override
		public V getValue() {
			return get();
		}
	}
}
