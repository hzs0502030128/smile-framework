package org.smile.core.math;

import org.junit.Test;
import org.smile.math.NumberUtils;

public class TestNumber {
	
	 @Test
	 public void test(){
		 String s=NumberUtils.toUnsignedString(100, 4);
		 System.out.println(s);
	 }
}
