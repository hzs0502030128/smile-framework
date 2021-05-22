package org.smile.core;

import org.junit.Test;
import org.smile.template.WrapSimpleStringTemplate;
import org.smile.util.StringUtils;

public class TestClassUtils {
	@Test
	public void test(){
		Class[] interfaces=WrapSimpleStringTemplate.class.getInterfaces();
		System.out.println(StringUtils.join(interfaces));
	}
}
