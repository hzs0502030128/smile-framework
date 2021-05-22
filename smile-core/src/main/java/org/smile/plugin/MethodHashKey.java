package org.smile.plugin;

import java.lang.reflect.Method;

import org.smile.util.StringUtils;
/**
 * 只是针对同一个类中的方法进行简单的区分
 * 用于确认是同一个方法的key 
 * @author 胡真山
 * 2015年10月21日
 */
public class MethodHashKey {
	/**记录hash值*/
	private int hashCode;
	
	private Method method;
	/**方法的识别字符串*/
	private String methodKey;
	
	public MethodHashKey(Method m){
		this.method=m;
		StringBuilder sb=new StringBuilder();
		sb.append(m.getName());
		Class[] clazzs=m.getParameterTypes();
		for(Class c:clazzs){
			sb.append(c.hashCode());
			sb.append("-");
		}
		methodKey=sb.toString();
		this.hashCode=methodKey.hashCode();
	}
	@Override
	public boolean equals(Object key) {
		if(key instanceof MethodHashKey){
			return this.methodKey.equals(((MethodHashKey) key).methodKey);
		}
		return false;
	}
	@Override
	public int hashCode() {
		return hashCode;
	}
	
	@Override
	public String toString() {
		Class[] clazz=method.getParameterTypes();
		return method.getName()+"("+StringUtils.join(clazz, ",")+")";
	}
	
	public Method getMethod() {
		return method;
	}
	
	
}
