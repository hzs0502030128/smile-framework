package org.smile.collection.get;


public class ArrayIndexValueHandler<T> extends IndexValueHandler<T[], T>{

	@Override
	public T getValueAt(T[] collection, int index) {
		return collection[index];
	}

	@Override
	public int getSize(T[] collection) {
		return collection.length;
	}
}
