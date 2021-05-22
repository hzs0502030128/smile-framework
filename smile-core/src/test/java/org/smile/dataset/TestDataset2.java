package org.smile.dataset;

import org.junit.Test;

public class TestDataset2 {
	@Test
	public void test(){
		Object obj=FormulaUtils.topOccurs(new Integer[]{2,3,4,2,3,1,34,12,4,4});
		System.out.println(obj);
	}
}
