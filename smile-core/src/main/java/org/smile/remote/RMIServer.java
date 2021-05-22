package org.smile.remote;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RMIServer extends RMI{
	
	/**
	 * 创建服务
	 * @throws RemoteException
	 */
	public void createRegistry() throws RemoteException{
		//创建并导出接受指定port请求的本地主机上的Registry实例。              
		LocateRegistry.createRegistry(port);
	}
	
	public void bind(Remote service) throws RemoteException{
		Class[] classes=service.getClass().getInterfaces();
		for(Class clazz:classes){
			if(Remote.class.isAssignableFrom(clazz)){
				String url=getRmiUrl(clazz);
				try {
					Naming.bind(url, (Remote)service);
					logger.debug("绑定远程服务成功:"+url);
				} catch (Exception e) {
					throw new RemoteException("绑定服务失败"+service,e);
				}
			}
		}
	}
	
	
}
