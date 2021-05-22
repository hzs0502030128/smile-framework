package org.smile.beans.converter.type;

public class ByteArrayConverter extends ArrayConverter<Byte>{

	@Override
	protected Byte[] defaultValue(int len) {
		return new Byte[len];
	}

	@Override
	public Class<Byte[]> getType() {
		return Byte[].class;
	}

	@Override
	protected Class<Byte> getOneType() {
		return Byte.class;
	}

	
	
}
