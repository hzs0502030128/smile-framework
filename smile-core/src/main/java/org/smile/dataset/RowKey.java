package org.smile.dataset;

public interface RowKey<T> extends Comparable<RowKey<T>>{
	
	public T key();
}
