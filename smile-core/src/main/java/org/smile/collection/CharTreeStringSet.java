package org.smile.collection;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * 一个以字符树存放的String Set
 * 这是一个以字符组成的树来表示多个字符串的数据结构
 * 
 * @author 胡真山
 *
 */
public class CharTreeStringSet extends AbstractSet<String>{
	
	protected StringMap<String> rootMap;
	
	public CharTreeStringSet(){
		this.rootMap=new StringMap<String>();
	}
	/**
	 * 
	 * @param ignoreCase 是否忽略大小写 true  则是不区分大小写的
	 */
	public CharTreeStringSet(boolean ignoreCase){
		this.rootMap=new StringMap<String>(ignoreCase);
	}
	
	@Override
	public Iterator<String> iterator() {
		return rootMap.values().iterator();
	}

	@Override
	public int size() {
		return rootMap.size;
	}

	@Override
	public boolean isEmpty() {
		return rootMap.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return rootMap.containsKey(o);
	}

	@Override
	public boolean add(String e) {
		rootMap.put(e, e);
		return true;
	}

	@Override
	public boolean remove(Object o) {
		String v=rootMap.remove(o);
		return v!=null;
	}
	
	/***
	 * 删除以此字符串开头的所有字符串
	 * @param node
	 */
	public void removeStartWith(String node){
		rootMap.removeNode(node);
	}
	
	/**
	 * 过滤掉一个字符串中的子字符串
	 * 这些子字符串存在于这个set中
	 * @param content
	 * @return
	 */
	public String replaceContent(String content) {
		return replaceContent(content, '*');
	}
	
	/***
	 * 文本中是否存在set中的任意一个字符串
	 * @param content
	 * @return
	 */
	public boolean includeAnyString(String content){
		return rootMap.hasOneKey(content);
	}
	/**
	 * 过滤敏感词
	 * @param content
	 * @return
	 */
	public String replaceContent(String content,char replace) {
		char[] chars = content.toCharArray();
		Node<Character,String> node = rootMap.rootNode;// 设置查找节点为根节点
		StringBuilder buffer = new StringBuilder(content.length());// 输出字串
		List<Character> badList = new ArrayList<Character>();// 坏字集合
		int idx = 0;
		while (idx < chars.length) {
			node = rootMap.findNode(node, chars[idx]);
			if (node == null) {
				// 坏字节点中断
				node = rootMap.rootNode;// 设置节点为根节点，重新查找坏字
				idx = idx - badList.size();// 恢复索引值（当前索引 - 坏字集合长度）
				if (badList.size() > 0) {// 清空坏字集合
					badList.clear();
				}
				buffer.append(chars[idx]);// 添加输出字串
			} else if (node.endFlag) {//末尾
				badList.add(chars[idx]);
				for (int i = 0; i < badList.size(); i++) {// 过滤
					buffer.append(replace);
				}
				if (node.nodes.isEmpty())
				{
					node = rootMap.rootNode;// 设置为根节点
				}
				badList.clear();// 清空坏字集合
			} else {
				// 未构成坏字末尾
				badList.add(chars[idx]);// 添加至坏字集合
				if (idx == chars.length - 1) {
					for (int i = 0; i < badList.size(); i++) {
						buffer.append(badList.get(i));
					}
				}
			}
			idx++;
		}
		return buffer.toString();
	}
	
}
