package org.smile.gateway.handler;

import javax.servlet.ServletContext;

import org.smile.spring.SpringBeanLocator;
/**
 * 从spring提供service类的一种接口实现
 * @author 胡真山
 *
 */
public class SpringServiceHandler implements ServiceHandler {

	public Object getBean(String serviceName,ServletContext context) {
		return SpringBeanLocator.getInstance().getBean(serviceName);
	}

}
