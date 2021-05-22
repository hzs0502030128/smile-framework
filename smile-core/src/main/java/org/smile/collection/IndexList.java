package org.smile.collection;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
/**
 * 这是一个可以索引设置值的list实现
 * @author 胡真山
 * @Date 2016年1月22日
 * @param <E>
 */
public class IndexList<E> extends AbstractList<E> implements Cloneable,Serializable{
	
	private TreeMap<Integer,E> elements=new TreeMap<Integer,E>();
	
	@Override
	public int size() {
		return elements.size();
	}


	@Override
	public boolean contains(Object o) {
		return elements.containsValue(o);
	}

	@Override
	public Iterator<E> iterator() {
		return elements.values().iterator();
	}

	@Override
	public Object[] toArray() {
		return elements.values().toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return elements.values().toArray(a);
	}

	@Override
	public boolean add(E e) {
		if(isEmpty()){
			elements.put(0, e);
		}else{
			elements.put(elements.lastKey()+1, e);
		}
		return true;
	}

	@Override
	public boolean remove(Object o) {
		Iterator<Map.Entry<Integer, E>> set=elements.entrySet().iterator();
		boolean result=false;
		while(set.hasNext()){
			Entry<Integer, E> entry=set.next();
			if(valueEq(entry, o)){
				elements.remove(entry.getKey());
				result=true;
			}
		}
		return result;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for(Object o:c){
			if(!elements.containsValue(o)){
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		Iterator<Map.Entry<Integer, E>> set=elements.entrySet().iterator();
		boolean result=false;
		while(set.hasNext()){
			Entry<Integer, E> entry=set.next();
			if(c.contains(entry.getValue())){
				elements.remove(entry.getKey());
				result=true;
			}
		}
		return result;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("not implements this method ");
	}

	@Override
	public void clear() {
		this.elements.clear();
	}

	@Override
	public E get(int index) {
		return elements.get(index);
	}

	@Override
	public E set(int index, E element) {
		return elements.put(index, element);
	}

	@Override
	public void add(int index, E element) {
		if(elements.containsKey(index)){
			throw new IndexOutOfBoundsException("index "+index+" is exists ");
		}
		set(index, element);
	}

	@Override
	public E remove(int index) {
		if(!elements.containsKey(index)){
			throw new IndexOutOfBoundsException("index not exists "+index);
		}
		return elements.remove(index);
	}
	
	private boolean valueEq(Map.Entry<Integer, E> entry,Object val){
		if(val==null){
			return entry.getValue()==null;
		}
		return val==entry.getValue()||entry.getValue().equals(val);
	}

	@Override
	public int indexOf(Object o) {
		for(Map.Entry<Integer, E> entry:elements.entrySet()){
			if(valueEq(entry, o)){
				return entry.getKey();
			}
		}
		return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		int index=-1;
		for(Map.Entry<Integer, E> entry:elements.entrySet()){
			if(valueEq(entry, o)){
				index= entry.getKey();
			}
		}
		return index;
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return new ArrayList<E>(elements.subMap(fromIndex, toIndex).values());
	}
	/**
	 * 索引集合
	 * @return
	 */
	public Set<Integer> indexSet(){
		return elements.keySet();
	}
	/**
	 * 最大的索引
	 * @return
	 */
	public Integer maxIndex(){
		return elements.lastKey();
	}
	/**
	 * 是否存在索引
	 * @param index
	 * @return
	 */
	public boolean containsIndex(int index){
		return elements.containsKey(index);
	}

	@Override
	public String toString() {
		StringBuilder str=new StringBuilder();
		str.append("[");
		int i=0;
		for(E e:this){
			str.append(e);
			if(++i<size()){
				str.append(",");
			}
		}
		str.append("]");
		return str.toString();
	}
}
