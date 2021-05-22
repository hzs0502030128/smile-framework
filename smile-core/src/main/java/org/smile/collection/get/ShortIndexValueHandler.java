package org.smile.collection.get;

public class ShortIndexValueHandler extends IndexValueHandler<short[], Short>{

	@Override
	public Short getValueAt(short[] collection, int index) {
		return collection[index];
	}

	@Override
	public int getSize(short[] collection) {
		return collection.length;
	}

	

}
