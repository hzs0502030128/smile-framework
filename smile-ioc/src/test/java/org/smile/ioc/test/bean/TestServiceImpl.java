package org.smile.ioc.test.bean;

import org.smile.commons.ann.Service;

@Service
public class TestServiceImpl implements TestService{

	@Override
	public String test() {
		return "huzs";
	}

}
