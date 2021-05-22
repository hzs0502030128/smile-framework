package org.smile.collection;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.smile.collection.Node.NodeIterator;
import org.smile.commons.StringBand;
import org.smile.util.StringUtils;
/**
 * 使用一个数组做为key的hashmap  
 * 键具有树型结构  
 * @author 胡真山
 *
 * @param <K>
 * @param <V>
 */
public class ArrayHashMap<K,V> extends AbstractMap<K[], V>{
	/**保存map的长度*/
	private int size=0;
	/**根结点，不保存数据*/
	private  Node<K,V> rootNode =new ArrayKeyNode(null);
	/**
	 * 过滤敏感词
	 * @param content
	 * @return
	 */
	@Override
	public V get(Object key) {
		K[] array=(K[])key;
		Node<K,V> node =getNode(array);
		if(node!=null&&node.endFlag){
			return node.value;
		}
		return null;
	}
	/**
	 * 获取一个结点
	 * @param array
	 * @return
	 */
	public Node<K,V> getNode(K[] array){
		Node<K,V> node = rootNode;// 设置查找节点为根节点
		int idx = 0;
		while (idx < array.length) {
			node = findNode(node, array[idx]);
			if (node == null) {
				return null;
			} else if(idx==array.length-1){
				return node;
			}
			idx++;
		}
		return null;
	}
	
	@Override
	public V remove(Object key){
		K[] array=(K[])key;
		Node<K,V> node = getNode(array);
		if(node!=null&&node.endFlag){
			V value=node.value;
			node.removeValue();
			return value;
		}
		return null;
	}
	/**
	 * 添加坏子节点
	 * 
	 * @param node
	 * @param cs
	 * @param index
	 */
	private V insertNode(Node<K,V> node, K[] array,V value) {
		boolean isnew=false;
		Node<K,V> parent=node;
		for(int index=0;index<array.length;index++){
			node = findNode(parent, array[index]);
			if (node == null) {
				node = new ArrayKeyNode(array[index]);
				parent.nodes.put(node.key, node);
				node.parent=parent;
				isnew=true;
			}else{
				isnew=!node.endFlag;
			}
			parent=node;
		}
		// 标志是尾部
		node.endFlag = true;
		V old=node.value;
		node.value=value;
		node.fullKey=array;
		if(isnew){
			size++;
			return null;
		}
		return old;
		
	}

	/**
	 * 是否有坏字
	 * @param key
	 * @return true有坏字 false 没有
	 */
	public boolean hasKey(K[] key) {
		Node<K,V> node = getNode(key);
		if(node!=null&&node.endFlag){
			return true;
		}
		return false;
	}

	@Override
	public boolean containsKey(Object key) {
		return hasKey((K[])key);
	}
	
	@Override
	public void clear() {
		rootNode.nodes.clear();
	}
	
	protected Node<K,V> findNode(Node<K,V> node, K key) {
		return node.nodes.get(key);
	}

	/**
	 * 移去一个节点
	 * 字结点也会被移除
	 * @param elements
	 */
	public void removeNode(K[] array){
		Node<K,V> node = getNode(array);
		if(node!=null){
			node.remove();
		}
	}
	/**
	 * 是否存在一个节点
	 * @param array
	 * @return
	 */
	public boolean hasNode(K[] array){
		Node<K,V> node =getNode(array);
		return node!=null;
	}

	@Override
	public V put(K[] key, V value) {
		if (key.length > 0){
			insertNode(rootNode, key,value);
		}
		return value;
	}

	@Override
	public Set<Entry<K[], V>> entrySet() {
		return new AbstractSet<Entry<K[], V>>() {
			@Override
			public Iterator<Entry<K[], V>> iterator() {
				NodeIterator iterator=rootNode.iterator();
				return iterator;
			}
			@Override
			public int size() {
				return size;
			}
		};	
	}
	
	@Override
	public int size() {
		return size;
	}
	
	class ArrayKeyNode extends Node<K,V>{
		ArrayKeyNode(K key){
			super(key);
		}
		@Override
		protected void onRemoveValue() {
			size--;
		}
	}
}

abstract class Node<K,V> implements Entry<K[], V>{
	/**当前结点的键*/
	protected K key;
	/**结束标记*/
	protected boolean endFlag=false;
	/**从根结点开始完整的键*/
	protected K[] fullKey;
	/**当前结点上保存的值*/
	protected V value;
	/**父结点*/
	protected Node<K,V> parent;
	/**子结点*/
	protected Map<K, Node<K,V>> nodes = new HashMap<K,Node<K,V>>();
	
	public Node(){}
	
	public Node(K key) {
		this.key = key;
	}
	
	@Override
	public K[] getKey() {
		return fullKey;
	}

	@Override
	public V getValue() {
		return value;
	}

	@Override
	public V setValue(V value) {
		return this.value=value;
	}
	/**
	 * 在结点内容被移除时调用
	 */
	protected abstract void onRemoveValue();
	
	/**
	 * 移除结点整个结点移除
	 */
	public void remove(){
		if(!this.nodes.isEmpty()){
			NodeIterator iterator=new NodeIterator(this);
			while(iterator.hasNext()){
				onRemoveValue();
			}
		}
		if(this.endFlag){
			onRemoveValue();
		}
		parent.nodes.remove(key);
	}
	/**只移除当前结点的值*/
	public void removeValue(){
		this.endFlag=false;
		this.value=null;
		onRemoveValue();
	}
	
	public String fullKeyToString(){
		return "["+StringUtils.join(fullKey, ',')+"]";
	}
	
	public NodeIterator iterator(){
		return new NodeIterator(this);
	}

	@Override
	public String toString() {
		StringBand str=new StringBand();
		str.append(fullKeyToString()).append(":").append(value);
		return str.toString();
	}
	/**
	 * 用于迭代时封装内容
	 * @author 胡真山
	 *
	 */
	private class _Node_{
		Node<K,V> node;
		_Node_ parent;
		Iterator<_Node_> childs;
		//是否已经迭代过了
		boolean iterated;
		
		_Node_(Node<K,V> node){
			this.node=node;
			if(CollectionUtils.notEmpty(node.nodes)){
				this.childs=new Iterator<_Node_>() {
					Iterator<Map.Entry<K,Node<K,V>>> iterator=_Node_.this.node.nodes.entrySet().iterator();
					@Override
					public boolean hasNext() {
						return iterator.hasNext();
					}

					@Override
					public _Node_ next() {
						_Node_ n= new _Node_(iterator.next().getValue());
						n.parent=_Node_.this;
						return n;
					}

					@Override
					public void remove() {
						throw new RuntimeException("not supper remove");
					}
				};
			}
		}
		/**
		 * 结点的下一个结点
		 * @return
		 */
		_Node_ next(){
			if(childs!=null&&childs.hasNext()){
				return childs.next().next();
			}else if(!iterated){
				iterated=true;
				return this;
			}else if(parent!=null){
				return parent.next();
			}
			return null;
		}
	}
	
	
	class NodeIterator implements Iterator<Node<K,V>>{
		_Node_ inode;
		_Node_ next;
		NodeIterator(Node<K,V> node){
			this.inode=new _Node_(node);
		}
		/**
		 * 对迭代是保存数据的结点
		 */
		@Override
		public boolean hasNext() {
			next=inode.next();
			while(next!=null&&!next.node.endFlag){
				inode=next;
				next=inode.next();
			}
			return next!=null;
		}

		@Override
		public void remove() {
			if(inode.node.endFlag){
				inode.node.removeValue();
			}
		}

		@Override
		public Node<K,V> next() {
			_Node_ n=null;
			if(next!=null){
				n=next; 
				inode=next;
				return n.node;
			}else if(hasNext()){
				n= next;
				inode=next;
				return n.node;
			}
			return null;
		}
	}
}
