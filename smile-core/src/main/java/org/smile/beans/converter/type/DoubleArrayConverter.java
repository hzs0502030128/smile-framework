package org.smile.beans.converter.type;

public class DoubleArrayConverter extends ArrayConverter<Double>{

	@Override
	protected Double[] defaultValue(int len) {
		return new Double[len];
	}

	@Override
	public Class<Double[]> getType() {
		return Double[].class;
	}

	@Override
	protected Class<Double> getOneType() {
		return Double.class;
	}

	
	
}
