package org.smile.gateway.json;

import java.lang.reflect.Type;

/**
 * 处理Json数据的转换  
 * @author 胡真山
 *
 */
public interface JsonHandler {
	/**
	 * 解析参数信息为java对象
	 * @param array
	 * @return
	 * @throws Exception
	 */
	public Object[] parserJsonToArray(String array) throws Exception;
	/**
	 * 把java对象转成json字符串
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public String toJson(Object obj) throws Exception;
	/**
	 * 转化为方法参数适用的类型
	 * @param type
	 * @param value 
	 * @return
	 */
	public Object toMethodParam(Type type,Object value);
}
