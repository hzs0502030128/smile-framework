package org.smile.core.groovy;

import org.junit.Test;
import org.smile.reflect.Invoker;
import org.smile.script.groovy.GroovyClass;
import org.smile.script.groovy.GroovyLoader;

public class TestClassloader {
	@Test
	public void test(){
		GroovyLoader loader=new GroovyLoader();
		loader.loadClasspath("D:/smileworkspace/smile-framework/smile-core/groovy");
		GroovyClass clazz=loader.getGroovyClass("test.bean.TestGroovyBean");
		Invoker invoker=clazz.getInvoker("testmethod", String.class);
		Object target=clazz.newInstance();
		Object obj=invoker.call(target, new Object[]{"胡真山"});
		System.out.println(obj);
	}
}
