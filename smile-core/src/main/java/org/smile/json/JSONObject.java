
package org.smile.json;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import org.smile.collection.LinkedHashMap;
import org.smile.collection.ResultMap;
import org.smile.json.format.SerializeConfig;
import org.smile.json.parser.JSONParseException;
import org.smile.json.parser.JSONTokener;
/**
 * JSONObject
 * @author huzhenshan hzs0502030128@163.com
 */
@SuppressWarnings("serial")
public class JSONObject extends LinkedHashMap<String,Object> implements JSONAware,ResultMap{
	
	public JSONObject(){}
	
	@Override
	public String toJSONString(){
		return JSONValue.serializer.serializeMap(this,SerializeConfig.NULL_NOT_VIEW);
	}
	
	public JSONObject(Reader reader) throws JSONParseException {
		this(new JSONTokener(reader));
	}
	
	public JSONObject(String jsonStr) throws JSONParseException {
		this(new JSONTokener(jsonStr));
	}
	/**
	 * 把map中的内容放入到json中
	 * @param map
	 */
	public JSONObject(Map<String,Object> map) {
		putAll(map);
	}
	
	
	public JSONObject(JSONTokener x) throws JSONParseException {
        char c;
        String key;

        if (x.nextClean() != '{') {
            throw x.syntaxError("A JSONObject text must begin with '{'");
        }
        for (;;) {
            c = x.nextClean();
            switch (c) {
            case 0:
                throw x.syntaxError("A JSONObject text must end with '}'");
            case '}':
                return;
            default:
                x.back();
                key = x.nextValue().toString();
            }


            c = x.nextClean();
            if (c == '=') {
                if (x.next() != '>') {
                    x.back();
                }
            } else if (c != ':') {
                throw x.syntaxError("Expected a ':' after a key");
            }
            this.put(key, x.nextValue());


            switch (x.nextClean()) {
            case ';':
            case ',':
                if (x.nextClean() == '}') {
                    return;
                }
                x.back();
                break;
            case '}':
                return;
            default:
                throw x.syntaxError("Expected a ',' or '}'");
            }
        }
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
		writer.write(JSONValue.toJSONString(json,SerializeConfig.NULL_NOT_VIEW));
	}
	/**
	 * 重写toString方法
	 */
	@Override
	public String toString() {
		return toJSONString();
	}
	/**
	 * 转成json字符串
	 * @param value
	 * @return
	 */
	public static String toJSONString(Object value){
		return JSONValue.serializer.serializeObjct(value,SerializeConfig.NULL_NOT_VIEW);
	}

	@Override
	public Object value() {
		return this;
	}

	@Override
	public boolean isNullJson() {
		return false;
	}

	@Override
	public JSONType getJsonType() {
		return JSONType.OBJECT;
	}

	@Override
	public void serializeWriter(JSONWriter writer) {
		JSONValue.serializer.writeMap(writer, this);
		writer.flush();
	}
	
}
