package org.smile.collection.get;


public class LongIndexValueHandler extends IndexValueHandler<long[], Long>{

	@Override
	public Long getValueAt(long[] collection, int index) {
		return collection[index];
	}

	@Override
	public int getSize(long[] collection) {
		return collection.length;
	}
}
