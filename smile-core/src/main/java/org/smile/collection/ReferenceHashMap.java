package org.smile.collection;

import java.lang.ref.ReferenceQueue;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
/**
 *	 软引用Map
 * @author 胡真山
 *
 * @param <K>
 * @param <V>
 */
public abstract class ReferenceHashMap<K,V> extends AbstractMap<K,V> {
	/**用于保存数据的map*/
	private Map<K, RefValue<K,V>> map;
	/**用于保存被回收了的对象*/
	protected ReferenceQueue<V> rq = new ReferenceQueue<V>();
	//view 序列化时不需要
	private transient Set<Map.Entry<K,V>> entrySet = null;

	public ReferenceHashMap(){
		map=new HashMap<K,RefValue<K,V>>();
	}
	
	protected ReferenceHashMap(Map<K,RefValue<K,V>> map){
		this.map=map;
	}
	
	
	protected interface RefValue<K,V>{
		K getKey();
		V getValue();
	}

	/***
	 * 处理被回收了的key
	 */
	private void processQueue() {
		RefValue<K,V> sv = null;
		while ((sv = (RefValue<K,V>) rq.poll()) != null) {
			map.remove(sv.getKey());
		}
	}
	
	protected abstract RefValue<K,V> newRefValue(K k,V v);

	@Override
	public V get(Object key) {
		RefValue<K,V> value = map.get(key);
		if (value == null)
			return null;
		V v=value.getValue();
		if (v== null) {
			processQueue();
			return null;
		} else {
			return v;
		}
	}

	@Override
	public V put(K k, V v) {
		RefValue<K,V> ret=map.put(k,newRefValue(k, v));
		if(ret!=null){
			return ret.getValue();
		}
		return null;
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		return entrySet==null?entrySet=new EntrySet():entrySet;
	}
	
	class EntrySet extends AbstractSet<Map.Entry<K,V>>{
		@Override
		public Iterator<Map.Entry<K, V>> iterator() {
			return new Iterator<Map.Entry<K, V>>(){
				Iterator<RefValue<K,V>> it=map.values().iterator();
				RefValue<K,V> sv;
				V next;
				@Override
				public boolean hasNext() {
					while(next==null&&it.hasNext()){
						sv=it.next();
						next=sv.getValue();
					}
					return next!=null;
				}

				@Override
				public Map.Entry<K, V> next() {
					if(next!=null||hasNext()){
						Map.Entry<K,V> entry= new BaseMapEntry<K,V>(sv.getKey(),next);
						next=null;
						return entry;
					}
					return null;
				}

				@Override
				public void remove() {
					map.remove(sv.getKey());
				}};
		}

		@Override
		public int size() {
			processQueue();
			return map.size();
		}
	}

	@Override
	public void clear() {
		processQueue();
		map.clear();
	}

	@Override
	public int size() {
		processQueue();
		return map.size();
	}

	@Override
	public V remove(Object k) {
		RefValue<K,V> value = map.remove(k);
		if (value == null)
			return null;
		return value.getValue();
	}
}