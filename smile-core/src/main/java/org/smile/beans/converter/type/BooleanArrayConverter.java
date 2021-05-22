package org.smile.beans.converter.type;

public class BooleanArrayConverter extends ArrayConverter<Boolean>{

	@Override
	protected Boolean[] defaultValue(int len) {
		return new Boolean[len];
	}

	@Override
	public Class<Boolean[]> getType() {
		return Boolean[].class;
	}

	@Override
	protected Class<Boolean> getOneType() {
		return Boolean.class;
	}

	
	
}
