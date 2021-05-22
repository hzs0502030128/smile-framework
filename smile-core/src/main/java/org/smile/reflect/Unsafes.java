package org.smile.reflect;

import java.lang.reflect.Field;

import org.smile.commons.SmileRunException;

import sun.misc.Unsafe;

public class Unsafes {
	/**
	 * 获取jdk中的Unsafe对象 由于jdk是不请允许非jdk包的码调用
	 * Unsafe.getUnsafe() 方法 会抛出异常 在此通过反射在调用
	 * @return
	 */
	public static Unsafe getUnsafe(){
		try{
			 Field field = Unsafe.class.getDeclaredField("theUnsafe");  
	         field.setAccessible(true);  
	         return (Unsafe)field.get(null); 
		}catch(Exception e){
			throw new SmileRunException("java.lang.SecurityException: Unsafe",e);
		}
	}
}
