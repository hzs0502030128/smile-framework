package org.smile.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
/**
 * 对一个set进行外部包装
 * @author 胡真山
 *
 * @param <E>
 */
public abstract class AbstractSetDecorator<E> implements Set<E>{

	protected Set<E> realSet;
	
	protected AbstractSetDecorator(Set<E> set){
		this.realSet=set;
	}
	
	@Override
	public int size() {
		return realSet.size();
	}

	@Override
	public boolean isEmpty() {
		return realSet.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return realSet.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return realSet.iterator();
	}

	@Override
	public Object[] toArray() {
		return realSet.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return realSet.toArray(a);
	}

	@Override
	public boolean add(E e) {
		return realSet.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return realSet.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return realSet.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return realSet.addAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return realSet.retainAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return realSet.removeAll(c);
	}

	@Override
	public void clear() {
		realSet.clear();
	}

}
