package org.smile.interceptor;

import org.junit.Test;
import org.smile.bean.Student;
import org.smile.json.JSONObject;
import org.smile.plugin.BaseInterceptor;
import org.smile.plugin.CglibBaseInterceptor;
import org.smile.plugin.Invocation;

public class TestCglib {
	@Test
	public void test1() {
		BaseInterceptor interceptor=new CglibBaseInterceptor() {
			@Override
			protected void doBefore(Invocation invocation) throws Throwable {
				System.out.println(invocation.getArgs());
			}
			
		};
		Student s=new Student();
		s=(Student)interceptor.plugin(s);
		s.setName("胡真山");
		System.out.println(s.getClass().getName());
		System.out.println(JSONObject.toJSONString(s));
	}
}
