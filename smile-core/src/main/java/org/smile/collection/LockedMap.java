package org.smile.collection;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/**
 * 只保证前Map的读写操作是线程安全的
 * 此map 的keyset   entryset value 的对象是 非线程安全是线程安全的
 * 如有些需求 请使用其它确保线程安全的类
 * @author 胡真山
 *
 * @param <K>
 * @param <V>
 */
public class LockedMap<K,V> extends AbstractMap<K, V>{
	//读写锁
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final Lock readLock = lock.readLock();
	private final Lock writeLock = lock.writeLock();
	/**源map*/
	private Map<K,V> map;
	/**entryset */
	private transient Set<Map.Entry<K,V>> entrySet = null; 
	
	public LockedMap(){
		this.map=new HashMap<K, V>();
	}
	
	public LockedMap(Map<K,V> map){
		this.map=map;
	}
	
	@Override
	public int size() {
		readLock.lock();
		try{
			return map.size();
		}finally{
			readLock.unlock();
		}
	}

	@Override
	public boolean isEmpty() {
		readLock.lock();
		try{
			return map.isEmpty();
		}finally{
			readLock.unlock();
		}
	}

	@Override
	public boolean containsKey(Object key) {
		readLock.lock();
		try{
			return map.containsKey(key);
		}finally{
			readLock.unlock();
		}
	}

	@Override
	public boolean containsValue(Object value) {
		readLock.lock();
		try{
			return map.containsValue(value);
		}finally{
			readLock.unlock();
		}
	}

	@Override
	public V get(Object key) {
		readLock.lock();
		try{
			return map.get(key);
		}finally{
			readLock.unlock();
		}
	}

	@Override
	public V put(K key, V value) {
		writeLock.lock();
		try{
			return map.put(key,value);
		}finally{
			writeLock.unlock();
		}
	}

	@Override
	public V remove(Object key) {
		writeLock.lock();
		try{
			return map.remove(key);
		}finally{
			writeLock.unlock();
		}
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		writeLock.lock();
		try{
			map.putAll(m);
		}finally{
			writeLock.unlock();
		}
	}

	@Override
	public void clear() {
		writeLock.lock();
		try{
			map.clear();
		}finally{
			writeLock.unlock();
		}
	}


	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		readLock.lock();
		try{
			if(this.entrySet==null){
				this.entrySet=new EntrySet();
			}
			return entrySet;
		}finally{
			readLock.unlock();
		}
	}
	
	private class EntrySet extends AbstractSet<Map.Entry<K,V>>{
		@Override
		public Iterator<Map.Entry<K, V>> iterator() {
			return new Iterator<Map.Entry<K, V>>(){
				Iterator<Map.Entry<K, V>> it=map.entrySet().iterator();
				Map.Entry<K, V> sv;
				V next;
				@Override
				public boolean hasNext() {
					readLock.lock();
					try{
						while(next==null&&it.hasNext()){
							sv=it.next();
							next=sv.getValue();
						}
						return next!=null;
					}finally{
						readLock.unlock();
					}
				}

				@Override
				public Map.Entry<K, V> next() {
					readLock.lock();
					try{
						if(next!=null||hasNext()){
							Map.Entry<K,V> entry= new BaseMapEntry<K,V>(sv.getKey(),next);
							next=null;
							return entry;
						}
						return null;
					}finally{
						readLock.unlock();
					}
				}

				@Override
				public void remove() {
					writeLock.lock();
					try{
						map.remove(sv.getKey());
					}finally{
						writeLock.unlock();
					}
				}
			};
		}

		@Override
		public int size() {
			readLock.lock();
			try{
				return map.size();
			}finally{
				readLock.unlock();
			}
		}
	}
}
