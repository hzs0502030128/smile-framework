package org.smile.collection;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.smile.collection.Node.NodeIterator;
/**
 * 这是一个以树形字符实现的字符串的集合set
 * @author 胡真山
 *
 */
public class StringMap<V> extends AbstractMap<String,V>{
	/**根结点*/
	protected volatile Node<Character,V> rootNode = new StringMapNode('R');
	/**用于键值转换，如忽略大小写功能*/
	private KeyHandler keyHandler;
	/**长度*/
	protected volatile int size=0;
	/**
	 * 是否忽略大小写
	 * @param ignoreCase
	 */
	public StringMap(boolean ignoreCase){
		if(ignoreCase){
			keyHandler=new NocaseKeyHandler();
		}else{
			keyHandler=new KeyHandler();
		}
	}
	/**
	 * 区分大小写
	 */
	public StringMap(){
		this(false);
	}
	
	@Override
	public V put(String e,V value) {
		return insertNode(e,value);
	}
	
	/**
	 * 获取一个结点
	 * @param array
	 * @return
	 */
	public Node<Character,V> getNode(String str){
		Node<Character,V> node = rootNode;// 设置查找节点为根节点
		int idx = 0;
		char[] array=str.toCharArray();
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
	public boolean containsKey(Object o) {
		Node<Character,V> node=getNode((String)o);
		if(node!=null){
			return node.endFlag;
		}
		return false;
	}
	
	/**
	 * 移除整个结节 包括子结点
	 * @param str
	 * @return
	 */
	public boolean removeNode(String str){
		Node<Character,V> node=getNode(str);
		if(node!=null){
			node.remove();
			return true;
		}
		return false;
	}
	
	public boolean remove(String o) {
		String str=(String)o;
		Node<Character,V> node = getNode(str);
		if(node!=null&&node.endFlag){
			node.removeValue();
			return true;
		}
		return false;
	}

	@Override
	public void clear() {
		this.rootNode.nodes.clear();
	}

	/**
	 * 插入一个字符串
	 */
	public V insertNode(String nodeStr,V value) {
		if (nodeStr.length() > 0){
			return insertNode(rootNode, nodeStr,value);
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
	private V insertNode(Node<Character,V> node, String key,V value) {
		Node<Character,V> parent=node;
		boolean isnew=false;
		Character[] cs=ArrayUtils.valuesOf(key.toCharArray());
		for(int index=0;index<cs.length;index++){
			node= findNode(parent, cs[index]);
			//标记是否是新增加的结点
			if (node == null) {
				node = new StringMapNode(cs[index]);
				parent.nodes.put(keyHandler.convert(cs[index]), node);
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
		node.fullKey=cs;
		if(isnew){//是新增加的结点 长度+1
			size++;
			return null;
		}
		return old;
	}

	/**
	 * 是否有坏字
	 * @param content
	 */
	public boolean hasOneKey(String content) {
		char[] chars = content.toCharArray();
		Node<Character,V> node = rootNode;
		List<Character> word = new LinkedList<Character>();
		int idx = 0;
		while (idx < chars.length) {
			node = findNode(node, chars[idx]);
			if (node == null) {
				node = rootNode;
				idx = idx - word.size();
				word.clear();
			} else if (node.endFlag) {
				return true;
			} else {
				word.add(chars[idx]);
			}
			idx++;
		}
		word = null;
		return false;
	}

	protected Node<Character,V> findNode(Node<Character,V> node, Character c) {
		return node.nodes.get(keyHandler.convert(c));
	}

	class StringMapNode extends Node<Character, V>{
		
		StringMapNode(Character key){
			super(key);
		}
		@Override
		protected void onRemoveValue() {
			size--;
		}
	}
	
	protected class KeyHandler{
		public Character convert(Character key){
			return key;
		}
	}
	
	protected class NocaseKeyHandler extends KeyHandler{
		public Character convert(Character key){
			return Character.toLowerCase(key);
		}
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public V remove(Object key) {
		String str=String.valueOf(key);
		Node<Character,V> node = getNode(str);
		if(node!=null&&node.endFlag){
			node.removeValue();
			return node.value;
		}
		return null;
	}
	
	@Override
	public Set<java.util.Map.Entry<String, V>> entrySet() {
		return new AbstractSet<Entry<String, V>>() {
			@Override
			public Iterator<Entry<String, V>> iterator() {
				NodeIterator iterator=rootNode.iterator();
				return iterator;
			}
			@Override
			public int size() {
				return size;
			}
		};	
	}
}
