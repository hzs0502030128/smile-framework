package org.smile.plugin;

import java.util.LinkedHashMap;
import java.util.Map;

import org.smile.util.RegExp;
import org.smile.util.StringUtils;
/**
 * 一个正则表达式代理方法的拦截器基础实现
 * @author 胡真山
 * @Date 2016年1月15日
 */
public class  SimpleRegExpInterceptor implements RegExpInterceptor {
	
	protected Map<RegExp,MethodInterceptor> methodInterceptors=new LinkedHashMap<RegExp,MethodInterceptor>();
	
	@Override
	public Object plugin(Object target) {
		return RegExpPlugin.wrap(target, this);
	}
	/**
	 * 此处为没有正则表达式匹配方法名的方法调用 
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		return invocation.proceed();
	}
	/**
	 * 注册调用代理
	 * @param reg 正则表达式
	 * @param proxy
	 */
	public void registMethodInterceptor(RegExp methodReg,MethodInterceptor proxy){
		methodInterceptors.put(methodReg, proxy);
	}
	/**
	 * 注册调用代理
	 * @param reg 正则表达式
	 * @param proxy
	 */
	public void registMethodInterceptor(String methodReg,MethodInterceptor proxy){
		methodInterceptors.put(new RegExp(StringUtils.configString2Reg(methodReg)), proxy);
	}

	@Override
	public Map<RegExp, MethodInterceptor> getMethodInterceptors() {
		return methodInterceptors;
	}
}
