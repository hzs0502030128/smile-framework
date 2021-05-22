package org.smile.orm.parameter;

import java.lang.reflect.Method;
/**
 * 获取方法的参数名称
 * @author 胡真山
 *
 */
public interface ParamNameGetter {
	/**获取方法的参数名*/
	String[] getParamName(Method method);
}
