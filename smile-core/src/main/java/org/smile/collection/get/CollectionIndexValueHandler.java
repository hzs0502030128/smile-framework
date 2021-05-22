package org.smile.collection.get;

import java.util.Collection;
import java.util.List;

public class CollectionIndexValueHandler<T> extends IndexValueHandler<Collection<T>, T> {

	@Override
	public T getValueAt(Collection<T> collection, int index) {
		if(collection instanceof List){
			return ((List<T>) collection).get(index);
		}else {
			int i=0;
			for(T t:collection){
				if(i==index){
					return t;
				}
				i++;
			}
			return null;
		}
	}

	@Override
	public int getSize(Collection<T> collection) {
		return collection.size();
	}

}
