package org.smile.gateway.invoke;

import java.lang.reflect.Method;
/**
 * 开启通道控制者
 * @author 胡真山
 *
 */
public interface OpenWayControler {
	/**
	 * 是否开户通过
	 * @param serviceClass 提供服务的类
	 * @param method 提供服务的方法
	 * @return 是否开启
	 */
	public boolean isOpenWay(Class serviceClass,Method method);
	
	public Method[] initOpenMethods(Class serviceClass);
	
}
