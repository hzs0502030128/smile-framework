package org.smile.beans.converter.type;


public class ByteConverter extends NumberConverter<Byte>{

	@Override
	protected Byte valueOf(Number number) {
		return number.byteValue();
	}
	
	@Override
	public Class getType() {
		return Byte.class;
	}
	
}
