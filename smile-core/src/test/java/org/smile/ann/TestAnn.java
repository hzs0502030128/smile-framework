package org.smile.ann;

import java.lang.reflect.Method;

import org.junit.Test;
import org.smile.annotation.AnnotationUtils;
import org.smile.bean.SuperBean;
import org.smile.commons.ann.Skip;
import org.smile.reflect.MethodUtils;
@Ann1(attr = "attr123",name="李白")
public class TestAnn{
	@Test
	public void testAlis() {
		Ann1 ann=AnnotationUtils.getAnnotation(TestAnn.class, Ann1.class);
		System.out.println(ann.attr()+" "+ann.name()+" "+ann.text() );
		Ann2 ann2=AnnotationUtils.getAnnotation(TestAnn.class, Ann2.class);
		System.out.println(ann2.name()+" "+ann2.text());
		Ann3 ann3=AnnotationUtils.getAnnotation(TestAnn.class, Ann3.class);
		System.out.println(ann3.name()+" "+ann3.value()+" "+ann3.text());
	}
	@Test
	public void test2() {
		Ann1 ann=AnnotationUtils.findAnnotation(TestAnn.class, Ann1.class);
		System.out.println(ann.attr()+" "+ann.name()+" "+ann.text() );
		Ann2 ann2=AnnotationUtils.findAnnotation(TestAnn.class, Ann2.class);
		System.out.println(ann2.name()+" "+ann2.text());
		Ann3 ann3=AnnotationUtils.findAnnotation(TestAnn.class, Ann3.class);
		System.out.println(ann3.name()+" "+ann3.value()+" "+ann3.text());
	}
	@Test
	public void testAnn() {
		Method method=MethodUtils.getMethod(SuperBean.class, "testBrig");
		Skip skip=AnnotationUtils.getAnnotation(method, Skip.class);
		System.out.println(skip);
	}
}