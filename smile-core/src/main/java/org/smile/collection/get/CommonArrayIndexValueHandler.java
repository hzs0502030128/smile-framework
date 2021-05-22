package org.smile.collection.get;

import java.lang.reflect.Array;

public class CommonArrayIndexValueHandler<C,T> extends IndexValueHandler<C, T>{

	@Override
	public T getValueAt(C collection, int index) {
		return (T)Array.get(collection, index);
	}

	@Override
	public int getSize(C collection) {
		return Array.getLength(collection);
	}
}
