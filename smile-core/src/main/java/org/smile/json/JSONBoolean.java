package org.smile.json;

public class JSONBoolean extends JSONBasic<Boolean>{

	public JSONBoolean(Boolean t) {
		super(t);
	}

	@Override
	public JSONType getJsonType() {
		return JSONType.BOOLEAN;
	}
	
}
