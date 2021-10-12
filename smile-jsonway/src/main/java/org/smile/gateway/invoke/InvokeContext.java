package org.smile.gateway.invoke;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InvokeContext {
	/**
	 * 缓存服务类的方法 
	 */
	protected Map<String,ServiceMethods> serviceMap=new ConcurrentHashMap<String,ServiceMethods>();
	/**控制是否发启方法为gateway方法*/
	protected OpenWayControler controler;

	
	public OpenWayControler getControler() {
		return controler;
	}
	/***
	 * 入口控制器设置 
	 * @param controler
	 */
	public void setControler(OpenWayControler controler) {
		this.controler = controler;
	}

	public ServiceMethods getServiceMethods(String serviceName,Class serviceClass){
		ServiceMethods serviceMethods=serviceMap.get(serviceName);
		if(serviceMethods==null){
			//初始化service方法 
			synchronized (serviceMap) {
				serviceMethods= serviceMap.get(serviceClass);
				if(serviceMethods==null){
					serviceMethods=new ServiceMethods(serviceClass);
					serviceMethods.init(controler);
					serviceMap.put(serviceName, serviceMethods);
				}
			}
		}
		return serviceMethods;
	}
}
