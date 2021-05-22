package org.smile.db.function;

import java.util.Set;

import org.smile.log.LoggerHandler;

/***
 * 函数处理接口
 * @author 胡真山
 *
 */
public interface FunctionHandler extends LoggerHandler{
	/**对函数进行转换的方法*/
	public void convertFuction(SqlFunction function);
	/**返回支持处理的函数名称*/
	public Set<String> viewSupport();
}
