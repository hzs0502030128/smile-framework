package org.smile.collection.get;

import java.util.List;

public class ListIndexValueHandler<T> extends IndexValueHandler<List<T>,T> {
	@Override
	public T getValueAt(List<T> collection, int index) {
		return collection.get(index);
	}

	@Override
	public int getSize(List<T> collection) {
		return collection.size();
	}
	

}
