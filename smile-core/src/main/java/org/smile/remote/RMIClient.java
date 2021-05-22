package org.smile.remote;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * 远程调用客户端
 * @author 胡真山
 *
 */
public class RMIClient extends RMI{
	/**
	 * 获取服务端的远程接口
	 * @param serviceInterface 远程接口类
	 * @return
	 * @throws RemoteException
	 */
	public  <T extends Remote> T lookup(Class<T> serviceInterface) throws RemoteException{
		String url=getRmiUrl(serviceInterface);
		try {
			return (T) Naming.lookup(url);
		} catch (MalformedURLException e) {
			throw new RemoteException("服务地址异常"+url,e);
		}catch (NotBoundException e) {
			throw new RemoteException("不存在的远程服务"+url,e);
		}
	}
}
