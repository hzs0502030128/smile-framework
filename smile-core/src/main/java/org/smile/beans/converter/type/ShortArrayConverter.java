package org.smile.beans.converter.type;

public class ShortArrayConverter extends ArrayConverter<Short>{

	@Override
	protected Short[] defaultValue(int len) {
		return new Short[len];
	}

	@Override
	public Class<Short[]> getType() {
		return Short[].class;
	}

	@Override
	protected Class<Short> getOneType() {
		return Short.class;
	}

	
	
}
