package org.smile.beans.converter.type;

public class IntArrayConverter extends ArrayConverter<Integer>{
	
	private static IntArrayConverter instance=new IntArrayConverter();
	
	public static IntArrayConverter getInstance(){
		return instance;
	}
	

	@Override
	protected Integer[] defaultValue(int len) {
		return new Integer[len];
	}

	@Override
	public Class<Integer[]> getType() {
		return Integer[].class;
	}

	@Override
	protected Class<Integer> getOneType() {
		return Integer.class;
	}

	
	
}
