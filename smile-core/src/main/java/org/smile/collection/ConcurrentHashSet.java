package org.smile.collection;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.smile.reflect.Unsafes;

/**
 * 使用 ConcurrentHashMap 实现的一个set
 * 
 * @author 胡真山
 *
 * @param <E>
 */
public class ConcurrentHashSet<E> extends AbstractSet<E> implements Cloneable, java.io.Serializable {

	private ConcurrentMap<E, Object> m;

	public ConcurrentHashSet() {
		m = new ConcurrentHashMap<E, Object>();
	}

	public ConcurrentHashSet(int size) {
		m = new ConcurrentHashMap<E, Object>(size);
	}

	public ConcurrentHashSet(Collection<? extends E> c) {
		m = new ConcurrentHashMap<E, Object>();
		addAll(c);
	}

	public ConcurrentHashSet(Set<E> s) {
		m = new ConcurrentHashMap<E, Object>((int) (s.size() * 1.5f));
		addAll(s);
	}

	public ConcurrentHashSet(ConcurrentHashMap<E, Object> m) {
		this.m = m;
	}

	@Override
	public ConcurrentHashSet<E> clone() {
		ConcurrentHashSet<E> clone = null;
		try {
			clone = (ConcurrentHashSet<E>) super.clone();
			clone.setMap(new ConcurrentHashMap(m));
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
		return clone;
	}

	@Override
	public int size() {
		return m.size();
	}

	@Override
	public boolean isEmpty() {
		return m.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return m.containsKey(o);
	}

	@Override
	public boolean add(E e) {
		return m.putIfAbsent(e, Boolean.TRUE) == null;
	}

	@Override
	public boolean remove(Object o) {
		return m.remove(o, Boolean.TRUE);
	}

	@Override
	public void clear() {
		m.clear();
	}

	@Override
	public Iterator<E> iterator() {
		return m.keySet().iterator();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof Set)) return false;
		Collection<?> c = (Collection<?>) o;
		try {
			return containsAll(c) && c.containsAll(this);
		} catch (ClassCastException unused) {
			return false;
		} catch (NullPointerException unused) {
			return false;
		}
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean modified = false;
		for (Iterator<?> i = c.iterator(); i.hasNext();)
			if (remove(i.next())) modified = true;
		return modified;
	}

	private void setMap(ConcurrentHashMap<E, Object> map) {
		UNSAFE.putObjectVolatile(this, mapOffset, map);
	}

	private static final sun.misc.Unsafe UNSAFE;

	private static final long mapOffset;

	static {
		try {
			UNSAFE = Unsafes.getUnsafe();
			mapOffset = UNSAFE.objectFieldOffset(ConcurrentHashSet.class.getDeclaredField("m"));
		} catch (Exception e) {
			throw new Error(e);
		}
	}
}
