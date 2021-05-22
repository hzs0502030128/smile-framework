package org.smile.bean;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.smile.beans.BeanProperties;
import org.smile.beans.BeanUtils;
import org.smile.beans.PropertiesGetter;
import org.smile.beans.converter.BeanException;
import org.smile.collection.MapUtils;

public class TestBeanProperties {
	@Test
	public void test() throws BeanException{
		Student s=new Student();
		s.properties=new PropertiesGetter<String, Object>() {
			Map<Integer,Object> map=MapUtils.linkedHashMap(new Integer[]{1,2},new Object[]{"胡",MapUtils.hashMap("name","真")});
			@Override
			public Object getValue(String name) {
				return map.get(Integer.valueOf(name));
			}
		};
		
		BeanProperties p=BeanProperties.NORAL_CAN_NO_PROPERTY;
		Object obj=p.getExpFieldValue(s, "properties.2.name");
		System.out.println(obj);
	}
	@Test
	public void test2() throws BeanException{
		PropertiesGetter properties=new PropertiesGetter<String, Object>() {
			Map<Integer,Object> map=MapUtils.linkedHashMap(new Integer[]{1,2},new Object[]{"胡",MapUtils.hashMap("name","真")});
			@Override
			public Object getValue(String name) {
				return map.get(Integer.valueOf(name));
			}
		};
		
		Map map=new HashMap();
		map.put("properties", properties);
		BeanProperties p=BeanProperties.NORAL_CAN_NO_PROPERTY;
		Object obj=BeanUtils.getExpValue(map, "properties.2.name");
		System.out.println(obj);
	}
	
}
