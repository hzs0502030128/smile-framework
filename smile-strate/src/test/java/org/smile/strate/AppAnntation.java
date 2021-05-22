package org.smile.strate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.junit.Test;
import org.smile.reflect.MethodUtils;
import org.springframework.web.servlet.mvc.Controller;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppAnntation  extends TestCase
{
	
	
    @Test
    public void testRequestMapping()
    {
       Method m=MethodUtils.getAnyMethod(AppAnntation.class,"method");
       Annotation[] ans=m.getAnnotations();
       for(Annotation a:ans) {
    	   System.out.println(a);
       }
    }
}
