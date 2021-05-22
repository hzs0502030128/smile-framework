package org.smile.plugin;

import java.util.Map;

import org.smile.util.RegExp;

public class ClassRegExpInterceptor extends SimpleRegExpInterceptor{
	/**用来设置哪些类是需要代理*/
	protected RegExp classReg;
	
	@Override
	public Object plugin(Object target) {
		if(target!=null){
			Class clazz=target.getClass();
			if(needProxy(clazz)){
				return super.plugin(target);
			}
		}
		return target;
	}
	/**
	 * 是否要对些类型进行代理
	 * @param targetClass  目标类型
	 * @return
	 */
	protected boolean needProxy(Class targetClass){
		if(classReg==null){
			return true;
		}
		return classReg.test(targetClass.getName());
	}
	/**
	 * 设置类正则
	 * @param classString
	 */
	public void setClassReg(String classString) {
		this.classReg=Plugin.createMethodReg(classString);
	}
	/***
	 * 注册方法拦截器
	 * @param methodInterceptors
	 */
	public void setMethodInterceptors(Map<String,MethodInterceptor> methodInterceptors){
		for(Map.Entry<String,MethodInterceptor> entry:methodInterceptors.entrySet()){
			registMethodInterceptor(entry.getKey(), entry.getValue());
		}
	}
}
