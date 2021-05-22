package org.smile.core.beans;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.smile.bean.Student;
import org.smile.beans.FieldDeclare;
import org.smile.beans.MapBean;
import org.smile.beans.MapBeanClass;
import org.smile.beans.converter.BeanException;
import org.smile.beans.handler.MapBeanPropertyHandler;
import org.smile.json.JSON;
import org.smile.reflect.Generic;


public class TestBeanProperty {
	@Test
	public void test2() throws BeanException{
		MapBeanClass clazz=new MapBeanClass();
		clazz.declareFiled("name", new FieldDeclare(String.class));
		clazz.declareFiled("student", new FieldDeclare(Student.class));
		Generic g=new Generic(1);
		g.setIndex(0, Integer.class);
		clazz.declareFiled("ids", new FieldDeclare(List.class,g));
		clazz.declareFiled("map", new FieldDeclare(Map.class, new Generic(new Class[]{String.class,Student.class})));
		clazz.declareFiled("list", new FieldDeclare(List.class, new Generic(new Class[]{Student.class})));
		MapBean bean= clazz.newInstance();
		MapBeanPropertyHandler h=new MapBeanPropertyHandler(false);
		System.out.println(bean.get("name"));
		System.out.println(JSON.toJSONString(bean.get("student")));
		h.setExpFieldValue(bean, "name", "不折");
		h.setExpFieldValue(bean, "student.name", "不折e");
		h.setExpFieldValue(bean, "ids.0", 10);
		h.setExpFieldValue(bean, "ids.2", 13);
		h.setExpFieldValue(bean, "ids.1", 11);
		h.setExpFieldValue(bean, "map.1.name", "小白子啊233");
		h.setExpFieldValue(bean, "list.1.name", "小白子啊");
		h.setExpFieldValue(bean, "array.0.name", "小白子啊");
		System.out.println(bean.get("name"));
		System.out.println(bean.get("ids"));
		System.out.println(JSON.toJSONString(bean.get("map")));
		String jsonlist=JSON.toJSONString(bean.get("list"));
		System.out.println(jsonlist);
		List<Student> ss=JSON.parseJSONArray(jsonlist,Student.class);
		System.out.println(bean);
	}
}
