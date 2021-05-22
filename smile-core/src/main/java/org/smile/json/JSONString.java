package org.smile.json;

public class JSONString extends JSONBasic<String>{

	public JSONString(String t) {
		super(t);
	}
	
	@Override
	public String toJSONString() {
		return "\""+JSONValue.serializer.escape(value)+"\"";
	}

	@Override
	public JSONType getJsonType() {
		return JSONType.STRING;
	}

	@Override
	public void serializeWriter(JSONWriter writer) {
		writer.write('\"').write(JSONValue.serializer.escape(value));
		writer.write('\"');
	}
	
}
