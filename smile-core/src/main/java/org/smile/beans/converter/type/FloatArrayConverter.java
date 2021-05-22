package org.smile.beans.converter.type;

public class FloatArrayConverter extends ArrayConverter<Float>{

	@Override
	protected Float[] defaultValue(int len) {
		return new Float[len];
	}

	@Override
	public Class<Float[]> getType() {
		return Float[].class;
	}

	@Override
	protected Class<Float> getOneType() {
		return Float.class;
	}

	
	
}
