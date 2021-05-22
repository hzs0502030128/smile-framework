package org.smile.json;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;

import org.smile.json.format.SerializeConfig;
import org.smile.json.parser.JSONParseException;
import org.smile.json.parser.JSONTokener;

/**
 * 
 * @author 胡真山
 *
 */
public class JSONArray extends ArrayList<Object> implements JSONAware{
	/**
	 * 空的json对象
	 */
	public JSONArray(){}
	/**
	 * 指定长度的空JSON
	 * @param size
	 */
	public JSONArray(int size){
		super(size);
	}
	/**
	 * 从一个json字符串构造一个jsonArray对象
	 * @param jsonStr JSON字符串
	 * @throws JSONParseException 对字符串不能转成json对象时抛出异常
	 */
	public JSONArray(String jsonStr) throws JSONParseException{
		this(new JSONTokener(jsonStr));
	}
	/**
	 * 从一个jsontokener实例化一个jsonarray对象
	 * @param x
	 * @throws JSONParseException
	 */
	public JSONArray(JSONTokener x) throws JSONParseException {
        if (x.nextClean() != '[') {
            throw x.syntaxError("A JSONArray text must start with '['");
        }
        if (x.nextClean() != ']') {
            x.back();
            for (;;) {
                if (x.nextClean() == ',') {
                    x.back();
                    this.add(null);
                } else {
                    x.back();
                    this.add(x.nextValue());
                }
                switch (x.nextClean()) {
                case ';':
                case ',':
                    if (x.nextClean() == ']') {
                        return;
                    }
                    x.back();
                    break;
                case ']':
                    return;
                default:
                    throw x.syntaxError("Expected a ',' or ']'");
                }
            }
        }
    }
	/**
	 * 从集合中初始化一个JsonArray
	 * @param jsonObjs
	 */
	public JSONArray(Collection<?> jsonObjs){
		this.addAll(jsonObjs);
	}
	
	public String toJSONString(){
		return JSONValue.serializer.serializeCollection(this,SerializeConfig.NULL_NOT_VIEW);
	}
	/**
	 * 写入到一个writer中
	 * @param writer
	 * @throws IOException
	 */
	public void write(Writer writer) throws IOException{
		write(this,writer);
	}
	/**
	 * 写入到一个writer中
	 * @param writer
	 * @throws IOException
	 */
	public static void write(Object json,Writer writer) throws IOException{
		writer.write(JSONValue.serializer.serializeObjct(json,SerializeConfig.NULL_NOT_VIEW));
	}
	/**
	 * 重写toString方法
	 */
	public String toString() {
		return toJSONString();
	}
	
	public static String toJSONString(Collection collection){
		return JSONValue.toJSONString(collection,SerializeConfig.NULL_NOT_VIEW);
	}
	
	public static String toJSONString(Object[] array){
		return JSONValue.toJSONString(array,SerializeConfig.NULL_NOT_VIEW);
	}
	
	@Override
	public boolean isNullJson() {
		return false;
	}
	@Override
	public JSONType getJsonType() {
		return JSONType.ARRAY;
	}
	
	@Override
	public Object value() {
		return this;
	}
	
	@Override
	public void serializeWriter(JSONWriter writer) {
		JSONValue.serializer.writeCollection(writer, this);
	}
}
