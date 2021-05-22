package org.smile.collection;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * 软引用 map  内存不足时会被清除内容
 * @author 胡真山
 *
 * @param <K>
 * @param <V>
 */
public class SoftHashMap<K,V> extends ReferenceHashMap<K,V>{
	
	public SoftHashMap(){
		super();
	}
	
	public SoftHashMap(Map<K,RefValue<K,V>> map){
		super(map);
	}
	
	public static <K,V> SoftHashMap<K,V> newConcurrentInstance(){
		return new SoftHashMap<K,V>(new ConcurrentHashMap<K, RefValue<K,V>>());
	}
	/**
	 * 创建一个具有固定长度的map当长度
	 * 大于固定长度的时候采购先进先同的策略移除先进的key
	 * @param <K>
	 * @param <V>
	 * @param len 固定的长度
	 * @return
	 */
	public static <K,V> SoftHashMap<K,V> newFixedLenInstance(int len){
		Map<K,RefValue<K,V>> realmMap=new LockedMap<K,RefValue<K,V>>(new FixedLenHashMap<K,RefValue<K,V>>(len));
		return new SoftHashMap<K,V>(realmMap);
	}
	
	@Override
	protected RefValue<K,V> newRefValue(K k, V v) {
		return new SoftRefValue(k, v);
	}
	
	class SoftRefValue extends SoftReference<V> implements  RefValue<K, V>{
		K key;
		SoftRefValue(K k, V v) {
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
