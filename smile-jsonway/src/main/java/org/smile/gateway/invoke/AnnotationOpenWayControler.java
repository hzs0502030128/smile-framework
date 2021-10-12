package org.smile.gateway.invoke;

import java.lang.reflect.Method;

import org.smile.gateway.ann.GatewayPoint;
/**
 * 以注解决定是否开放入口
 * @author 胡真山
 *
 */
public class AnnotationOpenWayControler extends BaseOpenWayControler{
	@Override
	public boolean isOpenWay(Class serviceClass, Method method) {
		GatewayPoint gateway=method.getAnnotation(GatewayPoint.class);
		if(gateway!=null){
			return true;
		}
		return false;
	}
	
}
