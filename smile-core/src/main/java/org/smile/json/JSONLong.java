package org.smile.json;

public class JSONLong extends JSONBasic<Long>{

	public JSONLong(Long t) {
		super(t);
	}
	
	@Override
	public JSONType getJsonType() {
		return JSONType.LONG;
	}

}
