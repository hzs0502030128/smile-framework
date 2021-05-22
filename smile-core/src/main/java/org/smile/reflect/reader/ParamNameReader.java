package org.smile.reflect.reader;

import java.lang.reflect.Method;
/**
 * 方法参数名称读取
 * @author 胡真山
 *
 */
public interface ParamNameReader {
	/**获取方法的参数名*/
	String[] getParameterNames(Method method); 
	
}
