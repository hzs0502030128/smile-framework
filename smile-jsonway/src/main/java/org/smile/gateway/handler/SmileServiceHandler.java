package org.smile.gateway.handler;

import javax.servlet.ServletContext;
/**
 * smile提供了一个sericehandler的实现，
 * 在src目录下提供一个jsongateway_beans.xml的配置文件
 * 文件内容如下例子
 * <?xml version="1.0" encoding="UTF-8"?>
 *	<beans>
 *		<bean name="TestService" class="test.TestService"/>
 *	</beans>
 * @author 胡真山
 *
 */
public class SmileServiceHandler implements ServiceHandler {

	public Object getBean(String serviceName,ServletContext context) throws Exception {
		return BaseBeanContext.getBean(serviceName);
	}

}
