package org.smile.core.math;

import org.smile.math.MathUtils;
import org.smile.math.NumberUtils;

import junit.framework.TestCase;

public class TestNumberUtils extends TestCase{
	public void testDouble(){
		double s=6.777688888888888;
		System.out.println(MathUtils.ceilMultiple(1.6, 2.4));
		System.out.println(-0.0==+0.0);
	}
}
