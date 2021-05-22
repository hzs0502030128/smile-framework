package org.smile.ioc.test.bean;

import org.smile.commons.ann.Config;
import org.smile.commons.ann.Resource;
import org.smile.commons.ann.Value;
@Config(prefix = "bean",value = "smile2")
public class TestBean {
	@Resource
	private TestService service;
	@Value
	private String name;
	
	public void test() {
		System.out.println(service.test()+name);
	}
}
