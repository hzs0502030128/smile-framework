package org.smile.beans.converter.type;


public class IntegerConverter extends NumberConverter<Integer>{
	
	public static IntegerConverter instance=new IntegerConverter();
	
	@Override
	protected Integer valueOf(Number number) {
		return number.intValue();
	}
	
	@Override
	public Class getType() {
		return Integer.class;
	}
	
}
