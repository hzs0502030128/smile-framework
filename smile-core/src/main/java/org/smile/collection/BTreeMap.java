package org.smile.collection;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
/**
 * 
 * @author 胡真山
 *
 * @param <K>
 * @param <V>
 */
class BTreeMap<K, V> extends AbstractMap<K, V> implements SortedMap<K, V>{

	private static final int NODE_MAX_CHILD = 4; // max children per B-tree node
													// = M-1
	private static Comparator NULL_COMPARATOR = new Comparator<Comparable>() {
		@Override
		public int compare(Comparable o1, Comparable o2) {
			return o1.compareTo(o2);
		}
	};

	private TreeNode root; // root of the B-tree
	
	private transient int modCount=0;

	private int treeHeight; // height of the B-tree

	private int size; // number of key-value pairs in the B-tree
	/** 用于排序比较 */
	private Comparator<Object> comparator = null;
	
	private transient EntrySet entrySet;

	// helper B-tree node data type
	private class TreeNode {

		private int childNum; // number of children

		private TreeEntry<K, V>[] children = new TreeEntry[NODE_MAX_CHILD]; // the array
																	// of
		private TreeNode(int k) {
			childNum = k;
		} // create a node with k children

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			for (int i = 0; i < childNum; i++) {
				if (i > 0) {
					sb.append(",");
				}
				TreeEntry<K,V> entry = children[i];
				sb.append(entry.key);

			}
			sb.append("]");
			return sb.toString();
		}

		/**
		 * 第一个元素
		 * 
		 * @return
		 */
		TreeEntry<K, V> getFirstChild() {
			return children[0];
		}

		/**
		 * 最后一个元素
		 * 
		 * @return
		 */
		TreeEntry<K, V> getLastChild() {
			return children[childNum - 1];
		}
	}

	/**
	 * 
	 * @author 胡真山
	 * internal nodes: only use key and node
	 * external nodes: only use key and value
	 * @param <K>
	 * @param <V>
	 */
	class TreeEntry<K, V> implements Map.Entry<K, V> {
		/***/
		private K key;
		/**当前结点上存的值,只有外部结点才有效*/
		private V value;
		/**下级结点,只有内部节点才存在*/
		private TreeNode node; // helper field to iterate over array entries
		/**后继元素*/
		protected TreeEntry<K,V> successor;
		/**前继元素*/
		protected TreeEntry<K,V> predecessor;

		public TreeEntry(K key, V value, TreeNode next) {
			this.key = key;
			this.value = value;
			this.node = next;
		}

		@Override
		public String toString() {
			return "{" + key + ":" + (node == null ? value : "*") +(node==null?"}": "}->" + node);
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V value) {
			V oldValue = this.value;
			this.value = value;
			return oldValue;
		}
	}

	/**
	 * 使用默认排序 
	 * K 需实现 Comparable 接口
	 * constructor
	 */
	public BTreeMap() {
		this.comparator = NULL_COMPARATOR;
		root = new TreeNode(0);
	}
	/**
	 * 
	 * @param comparator 用于排序
	 */
	public BTreeMap(Comparator comparator){
		this.comparator=comparator;
		this.root=new TreeNode(0);
	}

	// return number of key-value pairs in the B-tree
	public int size() {
		return size;
	}
	
	protected V remove(TreeNode node,K childKey,int h){
		V removed=null;
		int childh=h-1;
		int idx=-1;
		for(int i=node.childNum-1;i>=0;i--){
			if (lessEq(node.children[i].key,childKey)) {
				TreeNode childNode=node.children[i].node;
				if(childNode!=null){
					removed=remove(node.children[i].node,childKey, childh);
					if(childNode.childNum==0){//如是没有子结果了此结点移去
						idx=i;
					}
				}
			}
		}
		
		if(removed==null&&h==0){//查找移除的索引
			for(int i=0;i<node.childNum;i++){
				TreeEntry<K,V> entry=node.children[i];
				if(h==0){
					if(eq(childKey, entry.key)){
						idx=i;
						break;
					}
				}
			}
		}
		//移除结点
		if(idx!=-1){
			if(h==0){//外部元素时返回内容并构建链表
				TreeEntry<K, V> removedEntry=node.children[idx];
				removed=removedEntry.value;
				if(removedEntry.successor!=null){
					removedEntry.successor.predecessor=removedEntry.predecessor;
				}
				if(removedEntry.predecessor!=null){
					removedEntry.predecessor.successor=removedEntry.successor;
				}
			}
			//移动元素
			for(int i=node.childNum-1;i>idx;i--){
				node.children[i-1]=node.children[i];
			}
			node.childNum--;
		}
		return removed;
	}
	
	@Override
	public V remove(Object key) {
		V v= remove(root, (K)key, treeHeight);
		if(v!=null){
			modCount++;
		}
		return v;
	}
	
	/**
	 * return height of B-tree
	 * @return
	 */
	public int height() {
		return treeHeight;
	}

	/**
	 * search for given key, return associated value; return null if no such key
	 */
	@Override
	public V get(Object key) {
		return search(root, (K) key, treeHeight);
	}

	/**
	 * 从一个结点开始查找内容
	 * 
	 * @param x
	 * @param key
	 * @param ht
	 * @return
	 */
	private V search(TreeNode x, K key, int ht) {
		TreeEntry<K, V>[] children = x.children;
		// external node
		if (ht == 0) {
			for (int j = 0; j < x.childNum; j++) {
				if (eq(key, children[j].key)) {
					return (V) children[j].value;
				}
			}
		}
		// internal node
		else {
			for (int j = 0; j < x.childNum; j++) {
				if (j + 1 == x.childNum || less(key, children[j + 1].key)) {
					return search(children[j].node, key, ht - 1);
				}
			}
		}
		return null;
	}

	/**
	 * insert key-value pair
	 * add code to check for duplicate keys
	 */
	@Override
	public V put(K key, V value) {
		TreeNode u = insert(root, key, value, treeHeight);
		size++;
		modCount++;
		if (u == null) {
			return null;
		}
		// need to split root
		TreeNode t = new TreeNode(2);
		t.children[0] = new TreeEntry(root.children[0].key, null, root);
		t.children[1] = new TreeEntry(u.children[0].key, null, u);
		root = t;
		treeHeight++;
		return null;
	}

	/**
	 * 插入一个数据
	 * 
	 * @param h
	 * @param key
	 * @param value
	 * @param ht
	 * @return
	 */
	private TreeNode insert(TreeNode h, K key, V value, int ht) {
		int j;
		TreeEntry<K, V> t = new TreeEntry<K, V>(key, value, null);
		// external node
		if (ht == 0) {
			for (j = 0; j < h.childNum; j++) {
				if (eq(key, h.children[j].key)) {
					// 替换值
					h.children[j].value = value;
					return null;
				} else if (less(key, h.children[j].key)) {
					break;
				}
			}
		}
		// internal node
		else {
			for (j = 0; j < h.childNum; j++) {
				if ((j + 1 == h.childNum) || less(key, h.children[j + 1].key)) {
					TreeNode u = insert(h.children[j++].node, key, value, ht - 1);
					if (u == null) {
						return null;
					}
					t.key = u.children[0].key;
					t.node = u;
					break;
				}
			}
		}
		for (int i = h.childNum; i > j; i--) {
			h.children[i] = h.children[i - 1];
		}
		h.children[j] = t;
		h.childNum++;
		if(ht==0&&h.childNum>1){//外部节点时建链表
			buildLinkedInfo(h, t, j);
		}
		if (h.childNum < NODE_MAX_CHILD) {
			return null;
		} else {
			return split(h);
		}
	}
	/**
	 * 构建链表
	 * @param n
	 * @param newLeaf 新插入的叶子元素
	 * @param childIndex 插入的元素在子数组的索引
	 */
	private void buildLinkedInfo(TreeNode n,TreeEntry<K, V> newLeaf,int childIndex){
		if(childIndex>0){
			TreeEntry<K, V> pro=n.children[childIndex-1];
			TreeEntry<K, V> next=pro.successor;
			if(next!=null){
				newLeaf.successor=next;
				next.predecessor=newLeaf;
			}
			newLeaf.predecessor=pro;
			pro.successor=newLeaf;
		}else{
			TreeEntry<K, V> next=n.children[childIndex+1];
			TreeEntry<K, V> pro=next.predecessor;
			if(pro!=null){
				newLeaf.predecessor=pro;
				pro.successor=newLeaf;
			}
			newLeaf.successor=next;
			next.predecessor=newLeaf;
		}
	}

	/**
	 * split node in half
	 * @param h
	 * @return
	 */
	private TreeNode split(TreeNode h) {
		TreeNode t = new TreeNode(NODE_MAX_CHILD / 2);
		h.childNum = NODE_MAX_CHILD / 2;
		for (int j = 0; j < NODE_MAX_CHILD / 2; j++){
			t.children[j] = h.children[NODE_MAX_CHILD / 2 + j];
		}
		return t;
	}

	/**
	 * for debugging
	 */
	public String toString() {
		return toString(root, treeHeight, "") + "\n";
	}

	private String toString(TreeNode h, int ht, String indent) {
		StringBuilder s=new StringBuilder();
		TreeEntry<K, V>[] children = h.children;
		if (ht == 0) {
			for (int j = 0; j < h.childNum; j++) {
				s.append(indent + children[j]);
				s.append("\n");
			}
		} else {
			for (int j = 0; j < h.childNum; j++) {
				if (j > 0) {
					s.append(indent + "(" + children[j].key + ")\n");
				}
				s.append(toString(children[j].node, ht - 1, indent + "     "));
			}
		}
		return s.toString();
	}

	/**
	 * 两个key比较大小
	 * 
	 * @param k1
	 * @param k2
	 * @return
	 */
	private boolean less(Object k1, Object k2) {
		return this.comparator.compare(k1, k2) < 0;
	}
	/**
	 * 小于等于
	 * @param k1
	 * @param k2
	 * @return
	 */
	private boolean lessEq(Object k1, Object k2) {
		return this.comparator.compare(k1, k2) <= 0;
	}


	/**
	 * 两个键比较
	 * 
	 * @param k1
	 * @param k2
	 * @return
	 */
	private boolean eq(Object k1, Object k2) {
		return this.comparator.compare(k1, k2) == 0;
	}

	@Override
	public Comparator<? super K> comparator() {
		return this.comparator;
	}
	/**
	 * 第一个键
	 * @return
	 */
	@Override
	public K firstKey() {
		return exportKey(findFirstExternalNode(root, treeHeight));
	}

	/**
	 * 最后一个key
	 * @return
	 */
	@Override
	public K lastKey() {
		return exportKey(findLastExternalNode(root, treeHeight));
	}

	/**
	 * 小于指定键的键 不存时返回null
	 * @param key
	 * @return
	 */
	public K lowerKey(K key) {
		return exportKey(findLowerExternalNode(root,key, treeHeight));
	}
	/**
	 * 第一个元素
	 * @return
	 */
	public java.util.Map.Entry<K, V> firstEntry() {
		TreeEntry<K, V> e= findFirstExternalNode(root, treeHeight);
		return exportEntry(e);
	}
	/**
	 * 小于等于
	 * @param key
	 * @return
	 */
	public java.util.Map.Entry<K, V> floorEntry(K key) {
		TreeEntry<K, V> e= findFloorExternalNode(root,key, treeHeight);
		return exportEntry(e);
	}
	/**
	 * 大于等于
	 * @param key
	 * @return
	 */
	public java.util.Map.Entry<K, V> ceilingEntry(K key) {
		TreeEntry<K, V> e= findCeilingExternalNode(root,key, treeHeight);
		return exportEntry(e);
	}
	/**
	 * 小于指定键的元素
	 * @param key
	 * @return
	 */
	public java.util.Map.Entry<K, V> lowerEntry(K key) {
		TreeEntry<K, V> e= findLowerExternalNode(root,key, treeHeight);
		return exportEntry(e);
	}
	
	
	public TreeEntry<K, V> findLowerExternalNode(TreeNode node, K key, int height) {
		if(height==0){
			for(int i=node.childNum-1;i>=0;i--){
				if(less(node.children[i].key,key)){
					return node.children[i];
				}
			}
		}else{
			height--;
			for(int i=node.childNum-1;i>=0;i--){
				if(less(node.children[i].key,key)){
					return findLowerExternalNode(node.children[i].node, key,height);
				}
			}
		}
		return null;
	}
	
	public TreeEntry<K, V> findFloorExternalNode(TreeNode node, K key, int height) {
		if(height==0){
			for(int i=node.childNum-1;i>=0;i--){
				if(lessEq(node.children[i].key,key)){
					return node.children[i];
				}
			}
		}else{
			height--;
			for(int i=node.childNum-1;i>=0;i--){
				if(lessEq(node.children[i].key,key)){
					return findFloorExternalNode(node.children[i].node, key,height);
				}
			}
		}
		return null;
	}
	/**
	 * 大于指定的键的元素
	 * @param key 指定的键
	 * @return
	 */
	public java.util.Map.Entry<K, V> higherEntry(K key) {
		TreeEntry<K, V> e= findHigherExternalNode(root, key, treeHeight);
		return exportEntry(e);
	}
	/**
	 * 大于
	 * @param key
	 * @return
	 */
	public K higherKey(K key) {
		TreeEntry<K, V> e= findHigherExternalNode(root, key, treeHeight);
		return exportKey(e);
	}

	public TreeEntry<K, V> findHigherExternalNode(TreeNode node, K key, int height) {
		if(height==0){
			for(int i=0;i<node.childNum;i++){
				if(less(key,node.children[i].key)){
					return node.children[i];
				}
			}
		}else{
			height--;
			for(int i=0;i<node.childNum;i++){
				if((i+1)==node.childNum||lessEq(key,node.children[i+1].key)){
					TreeEntry<K,V> res= findHigherExternalNode(node.children[i].node, key,height);
					if(res!=null){
						return res;
					}
				}
			}
		}
		return null;
	}
	
	public TreeEntry<K, V> findCeilingExternalNode(TreeNode node, K key, int height) {
		if(height==0){
			for(int i=0;i<node.childNum;i++){
				if(lessEq(key,node.children[i].key)){
					return node.children[i];
				}
			}
		}else{
			height--;
			for(int i=0;i<node.childNum;i++){
				if((i+1)==node.childNum||lessEq(key,node.children[i+1].key)){
					TreeEntry<K,V> res= findCeilingExternalNode(node.children[i].node, key,height);
					if(res!=null){
						return res;
					}
				}
			}
		}
		return null;
	}
	/**
	 * 查找第一个外部元素
	 * 
	 * @param node
	 * @param treeHeight
	 * @return
	 */
	public TreeEntry<K, V> findFirstExternalNode(TreeNode node, int treeHeight) {
		if (treeHeight == 0) {
			return node.getFirstChild();
		} else {
			return findFirstExternalNode(node.getFirstChild().node, --treeHeight);
		}
	}

	/**
	 * 查找最后一个外部元素
	 * 
	 * @param node
	 * @param treeHeight
	 * @return
	 */
	public TreeEntry<K, V> findLastExternalNode(TreeNode node, int treeHeight) {
		if (treeHeight == 0) {
			return node.getLastChild();
		} else {
			return findLastExternalNode(node.getLastChild().node, --treeHeight);
		}
	}

	/**
	 * 最后一个
	 * @return
	 */
	public java.util.Map.Entry<K, V> lastEntry() {
		TreeEntry<K,V> e= findLastExternalNode(root, treeHeight);
		return exportEntry(e);
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		Set<java.util.Map.Entry<K, V>> set=this.entrySet;
		return set==null?(this.entrySet=new EntrySet()):set;
	}

	protected Map.Entry<K, V> exportEntry(TreeEntry<K, V> e) {
		return (e == null) ? null : new AbstractMap.SimpleImmutableEntry(e);
	}

	protected K exportKey(TreeEntry<K, V> e) {
		return (e == null) ? null : e.getKey();
	}
	
	protected V exportValue(TreeEntry<K, V> e) {
		return (e == null) ? null : e.getValue();
	}
	
	/**
     * Base class for TreeMap Iterators
     */
    abstract class PrivateEntryIterator<T> implements Iterator<T> {
        protected TreeEntry<K,V> next;
        TreeEntry<K,V> lastReturned;
        int expectedModCount;
        
        PrivateEntryIterator(){}
        
        PrivateEntryIterator(TreeEntry<K,V> first) {
            expectedModCount = modCount;
            lastReturned = null;
            next = first;
        }
        @Override
        public final boolean hasNext() {
            return next != null;
        }

        final TreeEntry<K,V> nextEntry() {
            TreeEntry<K,V> e = next;
            if (e == null){
                throw new NoSuchElementException();
            }
            if (modCount != expectedModCount){
                throw new ConcurrentModificationException();
            }
            next = successor(e);
            lastReturned = e;
            return e;
        }

        private BTreeMap<K, V>.TreeEntry<K, V> successor(BTreeMap<K, V>.TreeEntry<K, V> e) {
			return e.successor;
		}

		final TreeEntry<K,V> prevEntry() {
            TreeEntry<K,V> e = next;
            if (e == null){
                throw new NoSuchElementException();
            }
            if (modCount != expectedModCount){
                throw new ConcurrentModificationException();
            }
            next = predecessor(e);
            lastReturned = e;
            return e;
        }

        private BTreeMap<K, V>.TreeEntry<K, V> predecessor(BTreeMap<K, V>.TreeEntry<K, V> e) {
			return e.predecessor;
		}
        
        @Override
		public void remove() {
            if (lastReturned == null){
                throw new IllegalStateException();
            }
            if (modCount != expectedModCount){
                throw new ConcurrentModificationException();
            }
            BTreeMap.this.remove(lastReturned.key);
            expectedModCount = modCount;
            lastReturned = null;
        }
    }
    
    final class EntryIterator extends PrivateEntryIterator<Map.Entry<K,V>> {
        EntryIterator(TreeEntry<K,V> first) {
            super(first);
        }
        public Map.Entry<K,V> next() {
            return nextEntry();
        }
    }

    final class ValueIterator extends PrivateEntryIterator<V> {
        ValueIterator(TreeEntry<K,V> first) {
            super(first);
        }
        @Override
        public V next() {
            return nextEntry().value;
        }
    }

    final class KeyIterator extends PrivateEntryIterator<K> {
        KeyIterator(TreeEntry<K,V> first) {
            super(first);
        }
        @Override
        public K next() {
            return nextEntry().key;
        }
    }
    
    class EntrySet extends AbstractSet<Map.Entry<K, V>>{

		@Override
		public Iterator<java.util.Map.Entry<K, V>> iterator() {
			return new EntryIterator(findFirstExternalNode(root, treeHeight));
		}

		@Override
		public int size() {
			return size;
		}
    }
    
    class KeySet extends AbstractSet<K>{

		@Override
		public int size() {
			return size;
		}

		@Override
		public Iterator<K> iterator() {
			return new KeyIterator(findFirstExternalNode(root, treeHeight));
		}
    }
    
    class ValueSet extends AbstractSet<V>{

		@Override
		public int size() {
			return size;
		}

		@Override
		public Iterator<V> iterator() {
			return new ValueIterator(findFirstExternalNode(root, treeHeight));
		}
    }
    
    class SortedSubMap extends AbstractMap<K, V> implements SortedMap<K, V>{
    	
    	private BTreeMap<K, V> treeMap;
    	private K start;
    	private K end;
    	private Set  subMapEntrySet;
    	
    	private SortedSubMap(BTreeMap<K,V> map,K start,K end){
    		this.treeMap=map;
    		this.start=start;
    		this.end=end;
    	}
    	
		@Override
		public Comparator<? super K> comparator() {
			return this.treeMap.comparator;
		}

		@Override
		public SortedMap<K, V> subMap(K fromKey, K toKey) {
			K subStart=fromKey;
			if(start!=null&&less(subStart,start)){
				subStart=start;
			}
			K subEnd=toKey;
			if(end!=null&&less(end,subEnd)){
				subEnd=end;
			}
			return treeMap.subMap(subStart, subEnd);
		}

		@Override
		public SortedMap<K, V> headMap(K toKey) {
			K subEnd=toKey;
			if(end!=null&&less(subEnd,end)){
				subEnd=end;
			}
			return treeMap.headMap(subEnd);
		}

		@Override
		public SortedMap<K, V> tailMap(K fromKey) {
			K subStart=fromKey;
			if(start!=null&&less(subStart,start)){
				subStart=start;
			}
			return treeMap.tailMap(subStart);
		}

		@Override
		public K firstKey() {
			if(start==null){
				return exportKey(treeMap.findFirstExternalNode(root, treeHeight));
			}else{
				return exportKey(treeMap.findCeilingExternalNode(root, start, treeHeight));
			}
		}
		
		public Entry<K,V> firstEntry(){
			return start==null?treeMap.findFirstExternalNode(root, treeHeight): findCeilingExternalNode(root,start, treeHeight);
		}
		
		public Entry<K,V> lastEntry(){
			return end==null?treeMap.findLastExternalNode(root,treeHeight): treeMap.findFloorExternalNode(root,end,treeHeight);
		}

		@Override
		public K lastKey() {
			if(end==null){
				return exportKey(treeMap.findLastExternalNode(root, treeHeight));
			}else{
				return exportKey(treeMap.findFloorExternalNode(root, end, treeHeight));
			}
		}

		@Override
		public Set<Map.Entry<K, V>> entrySet() {
			Set set=subMapEntrySet;
			return set==null?(subMapEntrySet=new SubMapEntrySet(firstEntry(),lastEntry())):set;
		}
    	
    }
    
    class SubMapEntrySet extends AbstractSet<TreeEntry<K, V>>{
    	
    	TreeEntry<K, V> start;
    	
    	TreeEntry<K, V> end;
    	
    	SubMapEntrySet(Map.Entry<K, V> start,Map.Entry<K, V> end){
    		this.start=(BTreeMap.TreeEntry) start;
    		this.end=(BTreeMap.TreeEntry) end;
    	}
		@Override
		public Iterator<TreeEntry<K, V>> iterator() {
			return new SubMapIterator(start,end);
		}

		@Override
		public int size() {
			int size=0;
			Iterator<TreeEntry<K,V>> it=iterator();
			while(it.hasNext()){
				size++;
			}
			return size;
		}
		
	}
    
    class SubMapIterator extends PrivateEntryIterator<TreeEntry<K,V>>{
    	TreeEntry<K,V> end;
    	
		SubMapIterator(TreeEntry<K,V> first,TreeEntry<K,V> end) {
			super(first);
			this.end=end;
		}

		@Override
		public TreeEntry<K,V> next() {
			TreeEntry<K,V> res= nextEntry();
			if(res==end){//已经是最后一个了
				this.next=null;
			}
			return res;
		}
    }

	@Override
	public SortedMap<K, V> subMap(K fromKey, K toKey) {
		return new SortedSubMap(this, fromKey, toKey);
	}
	
	@Override
	public SortedMap<K, V> headMap(K toKey) {
		return new SortedSubMap(this, null, toKey);
	}
	
	@Override
	public SortedMap<K, V> tailMap(K fromKey) {
		return new SortedSubMap(this, fromKey, null);
	}
}
