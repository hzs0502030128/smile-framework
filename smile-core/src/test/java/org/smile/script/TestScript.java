package org.smile.script;

import org.junit.Test;
import org.smile.bean.Student;
import org.smile.collection.CollectionUtils;
import org.smile.json.JSON;
import org.smile.util.RegExp;

public class TestScript {
	
	@Test
	public void test2(){
		ScriptExecutor se=ScriptType.JS.createExecutor();
		Student s=new Student();
		s.setAge(12);
//		se.setResultVar("ww");
		Object v=se.execute(" var result=0 ;for(i=0;i<=10;i++){result+=i;} obj.age=result+obj.age;result=obj; ww= 444;result;",CollectionUtils.hashMap("obj", s));
		System.out.println(v.getClass());
		System.out.println(JSON.toJSONString(v));
	}
	@Test
	public void test3(){
		ScriptExecutor se=ScriptType.SMIMLE_ELXL.createExecutor();
		Student s=new Student();
		s.setAge(12);
//		se.setResultVar("ww");
		Object v=se.execute("result=0 ;range=range(1,10);foreach(range,result=result+it);obj.age=result;obj",CollectionUtils.hashMap("obj", s));
		System.out.println(v.getClass());
		System.out.println(JSON.toJSONString(v));
	}
	@Test
	public void test4(){
		RegExp reg=new RegExp("^name(_[a-z]+)?$");
		System.out.println(reg.test("name_as"));
		System.out.println(reg.test("name_aZ"));
		System.out.println(reg.test("name"));
		System.out.println(reg.test("name_"));
		
		RegExp splitExp=new RegExp(";[\r\n]+");
		String[] ss=splitExp.split("1111;\n111");
		
	}
	
	
	@Test
	public void tests(){
		System.out.println("ABC".getBytes().length);
		System.out.println("胡真山".getBytes().length);
	}
	
}
