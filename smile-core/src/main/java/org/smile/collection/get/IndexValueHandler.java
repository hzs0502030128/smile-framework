package org.smile.collection.get;



public abstract class IndexValueHandler<C,T> {
	
	public abstract T getValueAt(C collection,int index);
	
	public abstract int getSize(C collection);
}
