package org.smile.collection;

import org.junit.Test;

public class TestLoop {
	@Test
	public void test(){
		for(int i:new Loop(1, 9, 2)){
			System.out.println(i);
		}
	}
}
