package org.smile.plugin;

import java.util.Map;

import org.smile.util.RegExp;
/**
 * 正则表达式的方式拦截器
 * @author 胡真山
 * @Date 2016年1月15日
 */
public interface RegExpInterceptor extends Interceptor{
	/**正则表达式的方法代理*/
	public Map<RegExp,MethodInterceptor> getMethodInterceptors();
}
