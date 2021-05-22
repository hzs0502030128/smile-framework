package org.smile.collection;

import java.util.Enumeration;
import java.util.Iterator;
/**
 * 只是做一个桥接
 * @author 胡真山
 *
 * @param <E>
 */
public class IteratorEnumeration<E> implements Enumeration<E>{
	
	private Iterator<E> iterator;
	
	public IteratorEnumeration(Iterator<E> iterator){
		this.iterator=iterator;
	}
	
	public IteratorEnumeration(Iterable<E> iterable){
		this.iterator=iterable.iterator();
	}

	@Override
	public boolean hasMoreElements() {
		return iterator.hasNext();
	}

	@Override
	public E nextElement() {
		return iterator.next();
	}

}
