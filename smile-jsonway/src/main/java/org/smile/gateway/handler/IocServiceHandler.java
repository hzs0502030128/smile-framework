package org.smile.gateway.handler;

import javax.servlet.ServletContext;

import org.smile.ioc.WebXmlIocContext;
/**
 *使用ioc容器中配置类实例
 * @author 胡真山
 */
public class IocServiceHandler implements ServiceHandler {

	public Object getBean(String serviceName,ServletContext context) throws Exception {
		return WebXmlIocContext.getInstance().getBean(serviceName);
	}

}
