package org.smile.collection.get;

public class BooleanIndexValueHandler extends IndexValueHandler<boolean[], Boolean>{

	@Override
	public Boolean getValueAt(boolean[] collection, int index) {
		return collection[index];
	}

	@Override
	public int getSize(boolean[] collection) {
		return collection.length;
	}

	

}
