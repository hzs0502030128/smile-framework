
package org.smile.json;

import java.io.IOException;
import java.io.Writer;

import org.smile.commons.Strings;
import org.smile.json.format.SerializeConfig;
import org.smile.json.parser.JSONParseException;
import org.smile.util.RegExp;

/**
 * json操作类
 * @author 胡真山
 */
public class JSONValue {
	/**封装了true的json对象*/
	public static final JSONBoolean TRUE=new JSONBoolean(true);
	/**一个false的json对象*/
	public static final JSONBoolean FALSE=new JSONBoolean(false);
	/**一个空的json对象*/
	public static final JSONNull NULL=new JSONNull();
	/**空字符串的json对象*/
	public static final JSONString BLANK=new JSONString(Strings.BLANK);
	/**可转换为integer类型正则表达式*/
	protected static RegExp INTEGER=new RegExp("^[0-9]+$");
	/**可转换为double类型正则表达式*/
	protected static RegExp DOUBLE=new RegExp("^[0-9]+\\.[0-9]*([eE][\\+-]?[0-9]+)?$");
	/**可转换为boolean类型正则表达式*/
	protected static RegExp BOOLEAN=new RegExp("^(true)|(false)$");
	/**用于json序列化*/
	protected static JSONSerializer serializer=new JSONSerializer();
	
	/**
	 * 转一个obejct成一个json格式
	 * @param value
	 * @return
	 */
	public static String toJSONString(Object value){
		return serializer.serialize(value,SerializeConfig.NULL_NOT_VIEW);
	}
	/**
	 * 转一个obejct成一个json格式
	 * @param value
	 * @return
	 */
	public static String toJSONString(Object value,SerializeConfig config) {
		return serializer.serialize(value, config);
	}
	/**
	 *解析时 转换字符串为对象
	 * @param string
	 * @return
	 */
	public static Object stringToValue(String string) {
        return serializer.parseString(string);
    }
	/**
	 * 写一个对象以JSON格式到一个writer中
	 * @param bean 源对象
	 * @param writer
	 * @throws IOException
	 */
	protected static void write(Object bean,Writer writer) throws IOException{
		writer.write(serializer.serialize(bean,SerializeConfig.NULL_NOT_VIEW));
	}
	/**
	 * 打印到控制台
	 * @param obj
	 */
	public static void printToConsle(Object obj){
		System.out.println(toJSONString(obj,SerializeConfig.NULL_NOT_VIEW));
	}
	/**
	 * 把字符串解析成json对象
	 * @param json
	 * @return
	 * @throws JSONParseException
	 */
	public static JSONAware parse(String json) throws JSONParseException {
		return serializer.parse(json);
	}
	
}
