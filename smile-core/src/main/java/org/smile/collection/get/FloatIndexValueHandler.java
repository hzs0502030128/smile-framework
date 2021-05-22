package org.smile.collection.get;

public class FloatIndexValueHandler extends IndexValueHandler<float[], Float>{

	@Override
	public Float getValueAt(float[] collection, int index) {
		return collection[index];
	}

	@Override
	public int getSize(float[] collection) {
		return collection.length;
	}

	

}
