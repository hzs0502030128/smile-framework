package org.smile.beans.converter.type;


public class LongConverter extends NumberConverter<Long>{
	public static final LongConverter instance=new LongConverter();
	@Override
	protected Long valueOf(Number number) {
		return number.longValue();
	}
	
	@Override
	public Class getType() {
		return Long.class;
	}
}
