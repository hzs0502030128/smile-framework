package org.smile.json;

public class JSONDouble extends JSONBasic<Double>{

	public JSONDouble(Double t) {
		super(t);
	}

	@Override
	public JSONType getJsonType() {
		return JSONType.DOUBLE;
	}
}
