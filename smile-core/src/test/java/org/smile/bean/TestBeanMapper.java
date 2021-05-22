package org.smile.bean;

import java.util.Date;
import java.util.HashMap;

import org.junit.Test;
import org.smile.beans.BeanWrapper;
import org.smile.beans.CglibBeanWrapper;
import org.smile.json.JSONObject;

public class TestBeanMapper {
	@Test
	public void test(){
		BeanWrapper<Student> studentMapper=new CglibBeanWrapper<Student>(Student.class);
		Student student=studentMapper.build(new HashMap());
		Class clazz=student.getClass();
		student.setAddress("中国");
		student.setName("胡真山");
		student.setAge(120);
		System.out.println(JSONObject.toJSONString(student));
		
	}
	
	@Test
	public void testInterface(){
		BeanWrapper<StudentSup> studentMapper=new CglibBeanWrapper<StudentSup>(StudentSup.class);
		StudentSup student=studentMapper.build(new HashMap());
		Class clazz=student.getClass();
		student.setName("胡真山");
		student.setAge(120);
		System.out.println(student.getName());
		System.out.println(JSONObject.toJSONString(student));
		
	}
	
	@Test
	public void testBean(){
		BeanWrapper<Student> studentMapper=new CglibBeanWrapper<Student>(Student.class);
		studentMapper.fields("address", "adr").fields("birthday", "code");
		MyBean bean=new MyBean();
		bean.setName("胡真山");
		bean.setCode("2019-09-09");
		Date date=new Date();
		Student student=studentMapper.build(bean);
		for(int i=0;i<10;i++){
			Class clazz=student.getClass();
			student.getName();
			student.setAddress("中国");
			student.setName("胡真山");
			student.setBirthday(date);
			student.setAge(120);
		}
		System.out.println(JSONObject.toJSONString(student));
	}
}
