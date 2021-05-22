package org.smile.json;

/**
 * @author 胡真山
 */
public interface JSONAware{
	/**@return JSON text*/
	String toJSONString();
	/**json的值*/
	Object value();
	/**是否是null*/
	boolean isNullJson();
	/**json的类型*/
	JSONType getJsonType();
	/**序列化到一个writer中*/
	void serializeWriter(JSONWriter writer);
}
