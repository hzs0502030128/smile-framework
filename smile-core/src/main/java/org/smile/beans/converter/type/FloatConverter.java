package org.smile.beans.converter.type;


public class FloatConverter extends NumberConverter<Float>{

	@Override
	protected Float valueOf(Number number) {
		return number.floatValue();
	}
	
	@Override
	public Class getType() {
		return Float.class;
	}
	
}
