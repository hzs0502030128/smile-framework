package org.smile.bean;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.smile.beans.BeanInfo;
import org.smile.beans.BeanUtils;
import org.smile.beans.converter.BeanException;
import org.smile.collection.CollectionUtils;

public class TestBeanInfo {
	@Test
	public void testRead(){
		BeanInfo info=BeanInfo.getInstance(Student.class);
		System.out.println(info.getDeclareReadPd().keySet());
		System.out.println(info.getPdMap().keySet());
	}
	@Test
	public void testBeanUtils() throws BeanException{
		List list=new LinkedList();
		list.add(CollectionUtils.hashMap("id", 1L));
		List<Long> ids=(List<Long>) BeanUtils.getExpValue(list, "*.id");
		System.out.println(ids);
	}
}
