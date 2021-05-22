package org.smile.collection.get;

public class IntIndexValueHandler extends IndexValueHandler<int[], Integer>{

	@Override
	public Integer getValueAt(int[] collection, int index) {
		return collection[index];
	}

	@Override
	public int getSize(int[] collection) {
		return collection.length;
	}

	

}
