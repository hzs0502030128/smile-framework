package org.smile.json;

import org.junit.Test;
import org.smile.json.format.IgnoreableConfig;


public class TestJsonIgore {
	@Test
	public void test(){
		TestJsonIgoreBean bean=new TestJsonIgoreBean();
		bean.name="2223";
		bean.age=55;
		IgnoreableConfig fc=new IgnoreableConfig();
		System.out.println(new JSONFiledSerializer().serialize(bean,fc));
	}
	
	@Test
	public void testPd(){
		TestJsonIgoreBean bean=new TestJsonIgoreBean();
		bean.name="2223";
		bean.age=55;
		IgnoreableConfig fc=new IgnoreableConfig();
		System.out.println(new JSONSerializer().serialize(bean,fc));
	}
	
}
