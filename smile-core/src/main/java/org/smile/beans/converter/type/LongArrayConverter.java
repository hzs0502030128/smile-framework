package org.smile.beans.converter.type;

public class LongArrayConverter extends ArrayConverter<Long>{

	@Override
	protected Long[] defaultValue(int len) {
		return new Long[len];
	}

	@Override
	public Class<Long[]> getType() {
		return Long[].class;
	}

	@Override
	protected Class<Long> getOneType() {
		return Long.class;
	}

	
	
}
