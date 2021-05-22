package org.smile.collection;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;

class BTreeMap2<K, V> extends AbstractMap<K, V> implements NavigableMap<K, V> {

	private static final int NODE_MAX_CHILD = 4; // max children per B-tree node
													// = M-1
	private static Comparator NULL_COMPARATOR = new Comparator<Comparable>() {
		@Override
		public int compare(Comparable o1, Comparable o2) {
			return o1.compareTo(o2);
		}
	};

	private TreeNode root; // root of the B-tree

	private int treeHeight; // height of the B-tree

	private int size; // number of key-value pairs in the B-tree
	/** 用于排序比较 */
	private Comparator<Object> comparator = null;

	// helper B-tree node data type
	private class TreeNode {

		private int childNum; // number of children

		private Entry<K, V>[] children = new Entry[NODE_MAX_CHILD]; // the array
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
				Entry entry = children[i];
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
		Entry<K, V> getFirstChild() {
			return children[0];
		}

		/**
		 * 最后一个元素
		 * 
		 * @return
		 */
		Entry<K, V> getLastChild() {
			return children[childNum - 1];
		}
	}

	// internal nodes: only use key and next
	// external nodes: only use key and value
	class Entry<K, V> implements Map.Entry<K, V> {
		private K key;
		private V value;
		private TreeNode next; // helper field to iterate over array entries

		public Entry(K key, V value, TreeNode next) {
			this.key = key;
			this.value = value;
			this.next = next;
		}

		@Override
		public String toString() {
			return "{" + key + ":" + (next == null ? value : "*") + "}->" + next;
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
	public BTreeMap2() {
		this.comparator = NULL_COMPARATOR;
		root = new TreeNode(0);
	}
	/**
	 * 
	 * @param comparator 用于排序
	 */
	public BTreeMap2(Comparator comparator){
		this.comparator=comparator;
		this.root=new TreeNode(0);
	}

	// return number of key-value pairs in the B-tree
	public int size() {
		return size;
	}

	// return height of B-tree
	public int height() {
		return treeHeight;
	}

	// search for given key, return associated value; return null if no such key
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
		Entry<K, V>[] children = x.children;
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
					return search(children[j].next, key, ht - 1);
				}
			}
		}
		return null;
	}

	// insert key-value pair
	// add code to check for duplicate keys
	@Override
	public V put(K key, V value) {
		TreeNode u = insert(root, key, value, treeHeight);
		size++;
		if (u == null) {
			return null;
		}
		// need to split root
		TreeNode t = new TreeNode(2);
		t.children[0] = new Entry(root.children[0].key, null, root);
		t.children[1] = new Entry(u.children[0].key, null, u);
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
		Entry<K, V> t = new Entry<K, V>(key, value, null);
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
					TreeNode u = insert(h.children[j++].next, key, value, ht - 1);
					if (u == null) {
						return null;
					}
					t.key = u.children[0].key;
					t.next = u;
					break;
				}
			}
		}
		for (int i = h.childNum; i > j; i--) {
			h.children[i] = h.children[i - 1];
		}
		h.children[j] = t;
		h.childNum++;
		if (h.childNum < NODE_MAX_CHILD) {
			return null;
		} else {
			return split(h);
		}
	}

	// split node in half
	private TreeNode split(TreeNode h) {
		TreeNode t = new TreeNode(NODE_MAX_CHILD / 2);
		h.childNum = NODE_MAX_CHILD / 2;
		for (int j = 0; j < NODE_MAX_CHILD / 2; j++)
			t.children[j] = h.children[NODE_MAX_CHILD / 2 + j];
		return t;
	}

	// for debugging
	public String toString() {
		return toString(root, treeHeight, "") + "\n";
	}

	private String toString(TreeNode h, int ht, String indent) {
		String s = "";
		Entry<K, V>[] children = h.children;
		if (ht == 0) {
			for (int j = 0; j < h.childNum; j++) {
				s += indent + children[j].key + " : " + children[j].value;
				s += "\n";
			}
		} else {
			for (int j = 0; j < h.childNum; j++) {
				if (j > 0) s += indent + "(" + children[j].key + ")\n";
				s += toString(children[j].next, ht - 1, indent + "     ");
			}
		}
		return s;
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

	@Override
	public K firstKey() {
		return null;
	}

	@Override
	public K lastKey() {
		return null;
	}

	@Override
	public java.util.Map.Entry<K, V> lowerEntry(K key) {
		return null;
	}

	@Override
	public K lowerKey(K key) {
		return null;
	}

	@Override
	public java.util.Map.Entry<K, V> floorEntry(K key) {
		return null;
	}

	@Override
	public K floorKey(K key) {
		return null;
	}

	@Override
	public java.util.Map.Entry<K, V> ceilingEntry(K key) {
		return null;
	}

	@Override
	public K ceilingKey(K key) {
		return null;
	}

	@Override
	public java.util.Map.Entry<K, V> higherEntry(K key) {
		return null;
	}

	@Override
	public K higherKey(K key) {
		return null;
	}

	@Override
	public java.util.Map.Entry<K, V> firstEntry() {
		Entry<K, V> e= findFirstExternalNode(root, treeHeight);
		return exportEntry(e);
	}

	/**
	 * 查找第一个外部元素
	 * 
	 * @param node
	 * @param treeHeight
	 * @return
	 */
	public Entry<K, V> findFirstExternalNode(TreeNode node, int treeHeight) {
		if (treeHeight == 0) {
			return node.getFirstChild();
		} else {
			return findFirstExternalNode(node.getFirstChild().next, --treeHeight);
		}
	}

	/**
	 * 查找最后一个外部元素
	 * 
	 * @param node
	 * @param treeHeight
	 * @return
	 */
	public Entry<K, V> findLastExternalNode(TreeNode node, int treeHeight) {
		if (treeHeight == 0) {
			return node.getLastChild();
		} else {
			return findLastExternalNode(node.getLastChild().next, --treeHeight);
		}
	}

	@Override
	public java.util.Map.Entry<K, V> lastEntry() {
		Entry<K,V> e= findLastExternalNode(root, treeHeight);
		return exportEntry(e);
	}

	@Override
	public java.util.Map.Entry<K, V> pollFirstEntry() {
		return null;
	}

	@Override
	public java.util.Map.Entry<K, V> pollLastEntry() {
		return null;
	}

	@Override
	public NavigableMap<K, V> descendingMap() {
		return null;
	}

	@Override
	public NavigableSet<K> navigableKeySet() {
		return null;
	}

	@Override
	public NavigableSet<K> descendingKeySet() {
		return null;
	}

	@Override
	public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
		return null;
	}

	@Override
	public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
		return null;
	}

	@Override
	public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
		return null;
	}

	@Override
	public SortedMap<K, V> subMap(K fromKey, K toKey) {
		return null;
	}

	@Override
	public SortedMap<K, V> headMap(K toKey) {
		return null;
	}

	@Override
	public SortedMap<K, V> tailMap(K fromKey) {
		return null;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return null;
	}

	protected Map.Entry<K, V> exportEntry(Entry<K, V> e) {
		return (e == null) ? null : new AbstractMap.SimpleImmutableEntry(e);
	}

	protected K exportKey(Entry<K, V> e) {
		return (e == null) ? null : e.getKey();
	}
	
	protected V exportValue(Entry<K, V> e) {
		return (e == null) ? null : e.getValue();
	}

}
