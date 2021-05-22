package org.smile.remote;

import java.rmi.Remote;

import org.smile.log.LoggerHandler;

public class RMI implements LoggerHandler {
	/**
	 * 服务器端口
	 */
	protected int port = 1099;
	/**
	 * 服务器地址
	 */
	protected String ip = "127.0.0.1";

	/**
	 * 以接口转为服务地址
	 * 
	 * @param service
	 * @return
	 */
	protected <T extends Remote> String getRmiUrl(Class<T> service) {
		String url = "rmi://" + ip + ":" + port + "/" + service.getName().replaceAll("\\.", "/");
		logger.debug(url);
		return url;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
