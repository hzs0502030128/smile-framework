package org.smile.collection;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.smile.commons.NotImplementedException;
/**
 * 用于对一个map的值进行扩展 
 * put新的值时不对原map的值进行覆盖
 * 但取值的时候是新值
 * @author 胡真山
 *
 * @param <K>
 * @param <V>
 */
public class ExtendsMap<K, V> extends HashMap<K, V> {
	/**父map*/
	private Map<K, V> superMap;
	/**用于获取values*/
	private transient Collection<V> values = null;

	public ExtendsMap(Map<K, V> map) {
		superMap = map;
	}

	@Override
	public int size() {
		int size=currentScopeSize();
		for(K k:superMap.keySet()) {
			if(!currentScopeHasKey(k)) {
				size++;
			}
		}
		return size;
	}

	@Override
	public boolean isEmpty() {
		return superMap.isEmpty()&&super.isEmpty();
	}
	
	@Override
	public boolean containsValue(Object value) {
		if(super.containsValue(value)){
			return true;
		}
		return superMap.containsValue(value);
	}

	@Override
	public boolean containsKey(Object key) {
		return super.containsKey(key)||superMap.containsKey(key);
	}

	@Override
	public V get(Object key) {
		if (super.containsKey(key)) {
			return super.get(key);
		}
		return superMap.get(key);
	}

	/**
	 * 移除时只会移除当前map中的内容，如父map中存在 当get的时候还是会存在值的 
	 */
	@Override
	public V put(K key, V value) {
		return super.put(key, value);
	}

	@Override
	public V remove(Object key) {
		return super.remove(key);
	}

	@Override
	public void clear() {
		super.clear();
	}

	@Override
	public Set<K> keySet() {
		return new AbstractSet<K>() {
			@Override
			public Iterator<K> iterator() {
				return new ExtendsMapIterator<K>() {
					@Override
					public K next() {
						return nextEntry().getKey();
					}
				};
			}

			@Override
			public int size() {
				return ExtendsMap.this.size();
			}
			
		};
	}
	/**
	 * 只是当前map的key,不包括源map的key
	 * @return
	 */
	public Set<K> currentScopeKeys(){
		return super.keySet();
	}
	/**
	 * 当前范围是否包含键
	 * @param key
	 * @return
	 */
	public boolean currentScopeHasKey(K key) {
		return super.containsKey(key);
	}
	/**
	 * 当前范围map的长度
	 * @return
	 */
	public int currentScopeSize() {
		return super.size();
	}

	@Override
	public Collection<V> values() {
		Collection<V> vs = values;
        return (vs != null ? vs : (values = new Values()));
	}

	Iterator<V> valueIterator() {
		return new ExtendsMapIterator<V>() {
			@Override
			public V next() {
				return nextEntry().getValue();
			}
		};
	}

	private abstract class ExtendsMapIterator<E> implements Iterator<E> {
		/**当前scop的map的键的迭代*/
		Iterator<K> crtKeyIt;
		/**源map的键的迭代*/
		Iterator<K> srcKeyIt;
		/**当前迭代的key*/
		K crtKey;

		ExtendsMapIterator() {
			crtKeyIt = currentScopeKeys().iterator();
			srcKeyIt=superMap.keySet().iterator();
			roll();
		}

		void roll() {
			if(crtKeyIt.hasNext()) {
				crtKey=crtKeyIt.next();
			}else{
				while(srcKeyIt.hasNext()) {
					K tmp=srcKeyIt.next();
					if(currentScopeHasKey(tmp)) {
						continue;
					}
					crtKey=tmp;
					return;
				}
				crtKey=null;
			}
		}
		@Override
		public final boolean hasNext() {
			return crtKey!=null;
		}
		
		final Map.Entry<K, V> nextEntry() {
			if(crtKey==null) {
				throw new IndexOutOfBoundsException("eof");
			}
			final V value = get(crtKey);
			BaseMapEntry<K,V> entry= new BaseMapEntry<K, V>(crtKey,value) {
				@Override
				public V setValue(V value) {
					this.v=value;
					if(currentScopeHasKey(k)){
						return put(k, value);
					}
					return null;
				}
			};
			roll();
			return entry;
		}
		
		public void remove() {
			ExtendsMap.this.remove(crtKey);
		}

	}

	private final class Values extends AbstractCollection<V> {
		
		public Iterator<V> iterator() {
			return valueIterator();
		}

		public int size() {
			return ExtendsMap.this.size();
		}

		public boolean contains(Object o) {
			return containsValue(o);
		}

		public void clear() {
			ExtendsMap.this.clear();
		}
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		return new AbstractSet<Map.Entry<K, V>>() {
			@Override
			public Iterator<java.util.Map.Entry<K, V>> iterator() {
				return new ExtendsMapIterator<Map.Entry<K, V>>() {
					@Override
					public Map.Entry<K, V> next() {
						return nextEntry();
					}
				};
			}

			@Override
			public int size() {
				return ExtendsMap.this.size();
			}
		};
	}

}
