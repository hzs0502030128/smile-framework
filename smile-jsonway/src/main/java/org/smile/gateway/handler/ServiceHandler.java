package org.smile.gateway.handler;

import javax.servlet.ServletContext;
/**
 * JSONGateway服务类提供接口
 * @author 胡真山
 *
 */
public interface ServiceHandler {
	
	public Object getBean(String serviceName,ServletContext context) throws Exception;
	
}
