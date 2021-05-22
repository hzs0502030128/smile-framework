package org.smile.ioc.test.bean;

import org.smile.commons.ann.Bean;
import org.smile.commons.ann.Resource;
import org.smile.ioc.ann.Configuration;
@Configuration
public class BeanConfigs2 {
	@Resource
	TestService service;
	@Resource
	TestBean testBean;
	@Bean
	public TestBean testBean3(TestService testService) {
		testService.test();
		return new TestBean();
	}
	
	@Bean
	public TestBean testBean4(TestService testService) {
		testService.test();
		return new TestBean();
	}
	
	public void testService() {
		System.out.println(this.service.test());
	}
}
