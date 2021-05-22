package org.smile.ioc.test.bean;

import org.smile.commons.ann.Bean;
import org.smile.commons.ann.Resource;
import org.smile.ioc.ann.Configuration;

@Configuration
public class BeanConfigs {
	@Resource
	TestService service;
	@Bean
	public TestBean testBean1(TestService testService) {
		testService.test();
		return new TestBean();
	}
	
	@Bean
	public TestBean testBean2(TestService testService) {
		testService.test();
		return new TestBean();
	}
	@Bean
	public BeanConfigs2 beanConfig2() {
		return new BeanConfigs2();
	}
}
