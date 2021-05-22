package org.smile.json;

public class JSONInteger extends JSONBasic<Integer>{

	public JSONInteger(Integer t) {
		super(t);
	}
	
	@Override
	public JSONType getJsonType() {
		return JSONType.INTEGER;
	}

}
