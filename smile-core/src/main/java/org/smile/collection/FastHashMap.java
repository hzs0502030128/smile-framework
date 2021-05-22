package org.smile.collection;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class FastHashMap<K,V> extends HashMap<K,V> {
	/**真实操作的Map*/
    private volatile HashMap realMap = null;
    /**包装后的map 是否是fast模式 */
    private volatile Map wrapMap=null;

    public FastHashMap() {
        super();
        this.realMap = new HashMap<K,V>();
        this.wrapMap=new SynchronizedMap();
    }
    /**
     * 是否fast模式构造函数
     * @param fasted
     */
    public FastHashMap(boolean fasted){
    	super();
    	this.realMap=new HashMap();
    	if(fasted){
        	this.wrapMap=new FastedMap();
        }else{
        	this.wrapMap=new SynchronizedMap();
        }
    }

    /**
     * 默认非fast模式的 初始长度的map
     * @param capacity
     */
    public FastHashMap(int capacity) {
        super();
        this.realMap = new HashMap<K,V>(capacity);
        this.wrapMap=new SynchronizedMap();
    }

    /**
     * 初始长度与装载因子
     * @param capacity
     * @param factor
     */
    public FastHashMap(int capacity, float factor) {
        super();
        this.realMap = new HashMap<K,V>(capacity, factor);
        this.wrapMap=new SynchronizedMap();
    }

    /**
     * @param map 用于包装的map
     * @param fast 是否是快速模式
     */
    public FastHashMap(Map map,boolean fast) {
        super();
        if(map instanceof HashMap){
        	this.realMap=(HashMap<K, V>) map;
        }else{
        	this.realMap = new HashMap<K,V>(map);
        }
        if(fast){
        	this.wrapMap=new FastedMap();
        }else{
        	this.wrapMap=new SynchronizedMap();
        }
    }
    /**
     * 快速模式
     */
    public void toFasted(){
    	this.wrapMap=new FastedMap();
    }
    /**
     * 使用同步方式
     */
    public void toSynchronized(){
    	this.wrapMap=new SynchronizedMap();
    }

    @Override
    public V get(Object key) {
       return (V) this.wrapMap.get(key);
    }
    
    @Override
    public int size() {
        return this.wrapMap.size();
    }
    @Override
    public boolean isEmpty() {
        return this.wrapMap.isEmpty();
    }
    @Override
    public boolean containsKey(Object key) {
       return this.wrapMap.containsValue(key);
    }
    @Override
    public boolean containsValue(Object value) {
        return this.wrapMap.containsValue(value);
    }

    @Override
    public V put(K key, V value) {
        return (V) this.wrapMap.put(key, value);
    }

    @Override
    public void putAll(Map in) {
       this.wrapMap.putAll(in);
    }

    @Override
    public V remove(Object key) {
        return (V) this.wrapMap.remove(key);
    }
    @Override
    public void clear() {
       this.wrapMap.clear();
    }
    @Override
    public boolean equals(Object o) {
        // Simple tests that require no synchronization
        if (o == this) {
            return (true);
        } else if (!(o instanceof Map)) {
            return (false);
        }
       return wrapMap.equals(o);
    }
    @Override
    public int hashCode() {
       return this.wrapMap.hashCode();
    }
    @Override
    public Object clone() {
        FastHashMap<K,V> results = null;
        results = new FastHashMap(new HashMap(realMap),(wrapMap instanceof FastHashMap.FastedMap));
        return results;
    }
    @Override
    public Set entrySet() {
        return this.wrapMap.entrySet();
    }
    @Override
    public Set keySet() {
        return this.wrapMap.keySet();
    }
    @Override
    public Collection values() {
        return this.wrapMap.values();
    }
    /**
     * 获取封装的map
     * @return
     */
    public Map getWrapMap(){
    	return this.wrapMap;
    }

    private abstract class CollectionView implements Collection {
    	/**是否是fast模式*/
        protected volatile boolean fast = false;
        
        protected abstract Collection get(Map map);
        
        protected abstract Object iteratorNext(Map.Entry entry);

        @Override
        public void clear() {
            if (fast) {
                synchronized (FastHashMap.this) {
                    realMap = new HashMap();
                }
            } else {
                synchronized (realMap) {
                    get(realMap).clear();
                }
            }
        }
        @Override
        public boolean remove(Object o) {
            if (fast) {
                synchronized (FastHashMap.this) {
                    HashMap temp = (HashMap) realMap.clone();
                    boolean r = get(temp).remove(o);
                    realMap = temp;
                    return r;
                }
            } else {
                synchronized (realMap) {
                    return get(realMap).remove(o);
                }
            }
        }
        @Override
        public boolean removeAll(Collection o) {
            if (fast) {
                synchronized (FastHashMap.this) {
                    HashMap temp = (HashMap) realMap.clone();
                    boolean r = get(temp).removeAll(o);
                    realMap = temp;
                    return r;
                }
            } else {
                synchronized (realMap) {
                    return get(realMap).removeAll(o);
                }
            }
        }
        @Override
        public boolean retainAll(Collection o) {
            if (fast) {
                synchronized (FastHashMap.this) {
                    HashMap temp = (HashMap) realMap.clone();
                    boolean r = get(temp).retainAll(o);
                    realMap = temp;
                    return r;
                }
            } else {
                synchronized (realMap) {
                    return get(realMap).retainAll(o);
                }
            }
        }
        @Override
        public int size() {
            if (fast) {
                return get(realMap).size();
            } else {
                synchronized (realMap) {
                    return get(realMap).size();
                }
            }
        }

        @Override
        public boolean isEmpty() {
            if (fast) {
                return get(realMap).isEmpty();
            } else {
                synchronized (realMap) {
                    return get(realMap).isEmpty();
                }
            }
        }
        @Override
        public boolean contains(Object o) {
            if (fast) {
                return get(realMap).contains(o);
            } else {
                synchronized (realMap) {
                    return get(realMap).contains(o);
                }
            }
        }
        @Override
        public boolean containsAll(Collection o) {
            if (fast) {
                return get(realMap).containsAll(o);
            } else {
                synchronized (realMap) {
                    return get(realMap).containsAll(o);
                }
            }
        }
        @Override
        public Object[] toArray(Object[] o) {
            if (fast) {
                return get(realMap).toArray(o);
            } else {
                synchronized (realMap) {
                    return get(realMap).toArray(o);
                }
            }
        }
        @Override
        public Object[] toArray() {
            if (fast) {
                return get(realMap).toArray();
            } else {
                synchronized (realMap) {
                    return get(realMap).toArray();
                }
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (fast) {
                return get(realMap).equals(o);
            } else {
                synchronized (realMap) {
                    return get(realMap).equals(o);
                }
            }
        }
        @Override
        public int hashCode() {
            if (fast) {
                return get(realMap).hashCode();
            } else {
                synchronized (realMap) {
                    return get(realMap).hashCode();
                }
            }
        }
        @Override
        public boolean add(Object o) {
            throw new UnsupportedOperationException();
        }
        @Override
        public boolean addAll(Collection c) {
            throw new UnsupportedOperationException();
        }
        @Override
        public Iterator iterator() {
            return new CollectionViewIterator();
        }

        @Override
		public String toString() {
        	StringBuilder sb=new StringBuilder(64);
        	sb.append("[");
        	for(Object obj:this){
        		sb.append(obj).append(",");
        	}
        	if(sb.length()>1){
        		sb.setLength(sb.length()-1);
        	}
        	sb.append("]");
			return sb.toString();
		}



		private class CollectionViewIterator implements Iterator {

            private Map expected;
            private Map.Entry lastReturned = null;
            private Iterator iterator;

            public CollectionViewIterator() {
                this.expected = realMap;
                this.iterator = expected.entrySet().iterator();
            }
            @Override
            public boolean hasNext() {
                if (expected != realMap) {
                    throw new ConcurrentModificationException();
                }
                return iterator.hasNext();
            }
            @Override
            public Object next() {
                if (expected != realMap) {
                    throw new ConcurrentModificationException();
                }
                lastReturned = (Map.Entry)iterator.next();
                return iteratorNext(lastReturned);
            }
            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException();
                }
                if (fast) {
                    synchronized (FastHashMap.this) {
                        if (expected != realMap) {
                            throw new ConcurrentModificationException();
                        }
                        FastHashMap.this.remove(lastReturned.getKey());
                        lastReturned = null;
                        expected = realMap;
                    }
                } else {
                    iterator.remove();
                    lastReturned = null;
                }
            }
        }
    }

    /**
     * Set implementation over the keys of the FastHashMap
     */
    private class KeySet extends CollectionView implements Set {
    
    	KeySet(boolean fast){
    		this.fast=fast;
    	}
    	@Override
        protected Collection get(Map map) {
            return map.keySet();
        }
    	@Override
        protected Object iteratorNext(Map.Entry entry) {
            return entry.getKey();
        }
    
    }
    
    /**
     * Collection implementation over the values of the FastHashMap
     */
    private class Values extends CollectionView {
    	Values(boolean fast){
    		this.fast=fast;
    	}
    	@Override
        protected Collection get(Map map) {
            return map.values();
        }
    	@Override
        protected Object iteratorNext(Map.Entry entry) {
            return entry.getValue();
        }
    }
    
    /**
     * Set implementation over the entries of the FastHashMap
     */
    private class EntrySet extends CollectionView implements Set {
    
    	EntrySet(boolean fast){
    		this.fast=false;
    	}
    	
    	@Override
        protected Collection get(Map map) {
            return map.entrySet();
        }
    	@Override
        protected Object iteratorNext(Map.Entry entry) {
            return entry;
        }
    
    }
    /***
     * 使用快速模式的Map
     * @author 胡真山
     *
     * @param <K>
     * @param <V>
     */
    private class FastedMap implements Map{
		@Override
		public void clear() {
			synchronized (FastHashMap.this) {
                realMap = new HashMap();
            }
		}

		@Override
		public boolean containsKey(Object key) {
			return  realMap.containsKey(key);
		}

		@Override
		public boolean containsValue(Object value) {
			return realMap.containsValue(value);
		}


		@Override
		public Object get(Object key) {
			return realMap.get(key);
		}

		@Override
		public boolean isEmpty() {
			return realMap.isEmpty();
		}


		@Override
		public Object put(Object key, Object value) {
			synchronized (FastHashMap.this) {
				HashMap temp = (HashMap) realMap.clone();
                Object result = temp.put(key, value);
                realMap = temp;
                return (result);
            }
		}

		@Override
		public void putAll(Map in) {
			synchronized (FastHashMap.this) {
                HashMap<K,V> temp = (HashMap) realMap.clone();
                temp.putAll(in);
                realMap = temp;
            }
		}

		@Override
		public Object remove(Object key) {
			synchronized (FastHashMap.this) {
                HashMap<K,V> temp = (HashMap) realMap.clone();
                V result = temp.remove(key);
                realMap = temp;
                return (result);
            }
		}

		@Override
		public int size() {
			return realMap.size();
		}

		@Override
		public Collection values() {
			return new Values(true);
		}

		@Override
		public Set entrySet() {
			return new EntrySet(true);
		}

		@Override
		public Set keySet() {
			return new KeySet(true);
		}
		
		

		@Override
		public String toString() {
			return new EntrySet(true).toString();
		}

		@Override
		public boolean equals(Object other) {
			Map mo=(Map)other;
			if (mo.size() != realMap.size()) {
                return (false);
            }
            Iterator i = realMap.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry e = (Map.Entry) i.next();
                Object key = e.getKey();
                Object value = e.getValue();
                if (value == null) {
                    if (!(mo.get(key) == null && mo.containsKey(key))) {
                        return (false);
                    }
                } else {
                    if (!value.equals(mo.get(key))) {
                        return (false);
                    }
                }
            }
            return (true);
		}

		@Override
		public int hashCode() {
			int h = 0;
	        Iterator i = realMap.entrySet().iterator();
	        while (i.hasNext()) {
	            h += i.next().hashCode();
	        }
	        return h;
		}
    }
    
    private class SynchronizedMap implements Map{
    	
		@Override
		public void clear() {
			synchronized(realMap){
				realMap.clear();
			}
		}

		@Override
		public boolean containsKey(Object key) {
			synchronized(realMap){
				return realMap.containsKey(key);
			}
		}

		@Override
		public boolean containsValue(Object value) {
			synchronized(realMap){
				return realMap.containsValue(value);
			}
		}

		@Override
		public Object get(Object key) {
			synchronized(realMap){
				return realMap.get(key);
			}
		}

		@Override
		public boolean isEmpty() {
			synchronized(realMap){
				return realMap.isEmpty();
			}
		}

		@Override
		public Object put(Object key, Object value) {
			synchronized(realMap){
				return realMap.put(key,value);
			}
		}

		@Override
		public void putAll(Map in) {
			synchronized(realMap){
				realMap.putAll(in);
			}
		}

		@Override
		public Object remove(Object key) {
			synchronized(realMap){
				return realMap.remove(key);
			}
		}

		@Override
		public int size() {
			synchronized(realMap){
				return realMap.size();
			}
		}

		@Override
		public Collection values() {
			return new Values(false);
		}
		
		@Override
		public Set keySet() {
			return new KeySet(false);
		}
		
		@Override
		public Set entrySet() {
			return new EntrySet(false);
		}
		
		

		@Override
		public String toString() {
			return new EntrySet(false).toString();
		}

		@Override
		public boolean equals(Object other) {
			synchronized (realMap) {
				Map mo=(Map)other;
                if (mo.size() != realMap.size()) {
                    return (false);
                }
                Iterator i = realMap.entrySet().iterator();
                while (i.hasNext()) {
                    Map.Entry e = (Map.Entry) i.next();
                    Object key = e.getKey();
                    Object value = e.getValue();
                    if (value == null) {
                        if (!(mo.get(key) == null && mo.containsKey(key))) {
                            return (false);
                        }
                    } else {
                        if (!value.equals(mo.get(key))) {
                            return (false);
                        }
                    }
                }
                return (true);
            }
		}

		@Override
		public int hashCode() {
			int h = 0;
	        Iterator i = realMap.entrySet().iterator();
	        while (i.hasNext()) {
	            h += i.next().hashCode();
	        }
	        return h;
		}
    }
}

