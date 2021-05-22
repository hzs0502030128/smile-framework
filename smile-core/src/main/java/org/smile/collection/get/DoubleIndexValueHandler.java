package org.smile.collection.get;

public class DoubleIndexValueHandler extends IndexValueHandler<double[], Double>{

	@Override
	public Double getValueAt(double[] collection, int index) {
		return collection[index];
	}

	@Override
	public int getSize(double[] collection) {
		return collection.length;
	}

	

}
