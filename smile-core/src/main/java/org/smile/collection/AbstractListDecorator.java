package org.smile.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
/**
 * 对一个list进行外部包装
 * @author 胡真山
 *
 * @param <E>
 */
public abstract class AbstractListDecorator<E> implements List<E>{

	protected List<E> realList;
	
	protected AbstractListDecorator(List<E> list){
		this.realList=list;
	}
	
	@Override
	public int size() {
		return realList.size();
	}

	@Override
	public boolean isEmpty() {
		return realList.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return realList.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return realList.iterator();
	}

	@Override
	public Object[] toArray() {
		return realList.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return realList.toArray(a);
	}

	@Override
	public boolean add(E e) {
		return realList.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return realList.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return realList.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return realList.addAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return realList.retainAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return realList.removeAll(c);
	}

	@Override
	public void clear() {
		realList.clear();
	}
	
	@Override
	public E get(int index) {
		return realList.get(index);
	}

	@Override
	public E set(int index, E element) {
		return realList.set(index, element);
	}

	@Override
	public void add(int index, E element) {
		realList.add(index,element);
	}

	@Override
	public E remove(int index) {
		return realList.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return realList.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return realList.lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		return realList.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return realList.listIterator(index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return realList.subList(fromIndex, toIndex);
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		return realList.addAll(index,c);
	}

	
}
