package org.smile.json;

/***
 * 基本类型的json类型封装 (非JSONObject 和 JSONArray)
 * @author 胡真山
 *
 * @param <T>
 */
public class JSONBasic<T> implements JSONAware{
	
	public JSONBasic(T t){
		this.value=t;
	}
	
	protected T value;

	@Override
	public String toJSONString() {
		return String.valueOf(value);
	}

	@Override
	public T value() {
		return value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	@Override
	public boolean isNullJson() {
		return value==null;
	}

	@Override
	public JSONType getJsonType() {
		return JSONType.BASIC;
	}

	@Override
	public int hashCode() {
		if(isNullJson()){
			return JSONType.NULL.hashCode();
		}
		return value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof JSONAware){
			if(isNullJson()){
				return ((JSONAware) obj).isNullJson();
			}else{
				return this.value.equals(((JSONAware) obj).value());
			}
		}
		return false;
	}

	@Override
	public void serializeWriter(JSONWriter writer) {
		writer.write(String.valueOf(value));
	}
	
}
