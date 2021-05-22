package org.smile.json.adapter;

import java.util.Collection;
import java.util.List;
import java.util.Map;


public interface JsonAdpater{
	
	/**
	 * 解析对象型json字符串为Map
	 * @param json
	 * @return
	 */
	public Map parseJSONObject(String jsonObject);
	/**
	 * 解析数组型json字符串为List
	 * @param json
	 * @return
	 */
	public List parseJSONArray(String jsonArray);
	/**
	 * 对象转成json字符串
	 * @param json
	 * @return
	 */
	public String toJSONString(Object json);
	/**
	 * 转成json字符串
	 * @param json
	 * @return
	 */
	public String toJSONString(Collection json);
	/**
	 * 解析json
	 * @param json
	 * @return
	 */
	public Object parseJSON(String json); 
	/**
	 * 解析json成一个对象
	 * @param json
	 * @param javaType
	 * @return
	 */
	public <T> T parseJSONObject(String json,Class<T> javaType);
	/**
	 * 解析成对象列表
	 * @param json
	 * @param javaType
	 * @return
	 */
	public <T> List<T> parseJSONArray(String json,Class<T> javaType);
}
