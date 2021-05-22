package org.smile.beans.converter.type;

public class StringArrayConverter extends ArrayConverter<String>{
	
	private static StringArrayConverter instance=new StringArrayConverter();
	
	public StringArrayConverter getInstance(){
		return instance;
	}

	@Override
	protected String[] defaultValue(int len) {
		return new String[len];
	}

	@Override
	public Class<String[]> getType() {
		return String[].class;
	}

	@Override
	protected Class<String> getOneType() {
		return String.class;
	}
}
