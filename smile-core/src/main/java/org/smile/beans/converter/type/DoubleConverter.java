package org.smile.beans.converter.type;


public class DoubleConverter extends NumberConverter<Double>{
	
	public static DoubleConverter instance=new DoubleConverter();
	@Override
	protected Double valueOf(Number number) {
		return number.doubleValue();
	}
	
	@Override
	public Class getType() {
		return Double.class;
	}
	
}
