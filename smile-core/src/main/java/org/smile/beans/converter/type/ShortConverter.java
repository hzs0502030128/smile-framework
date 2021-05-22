package org.smile.beans.converter.type;


public class ShortConverter extends NumberConverter<Short>{

	@Override
	protected Short valueOf(Number number) {
		return number.shortValue();
	}

	@Override
	public Class getType() {
		return Short.class;
	}
	
}
