package org.smile.gateway.invoke;

import java.lang.reflect.Method;
/**
 * 最基本的控制器实现
 * @author 胡真山
 *
 */
public class BaseOpenWayControler implements OpenWayControler{
	@Override
	public boolean isOpenWay(Class serviceClass, Method method){
		return method.getDeclaringClass().equals(serviceClass);
	}

	@Override
	public Method[] initOpenMethods(Class serviceClass) {
		return serviceClass.getMethods();
	}
}
