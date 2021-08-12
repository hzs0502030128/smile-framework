package org.smile.bean;

import junit.framework.TestCase;
import org.smile.beans.BeanUtils;

public class TestBeanUtils extends TestCase {

    public void testPopulate(){
        Student s1 =new Student();
        s1.setName("胡真山");
        s1.setAge(10);
        Student s2 = new Student();
        BeanUtils.populate(s1,s2);
        assertEquals(s1.getAge(),s2.getAge());
        assertEquals(s1.getName(),s2.getName());
    }
}
