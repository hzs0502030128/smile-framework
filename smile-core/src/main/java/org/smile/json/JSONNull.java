package org.smile.json;

public class JSONNull extends JSONBasic<Object>{
	public JSONNull() {
		super(null);
	}
	
	@Override
	public JSONType getJsonType() {
		return JSONType.NULL;
	}
}
