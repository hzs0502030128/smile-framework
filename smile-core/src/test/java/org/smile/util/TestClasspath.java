package org.smile.util;

import java.util.Set;

import org.junit.Test;

public class TestClasspath {
	@Test
	public void test() {
		Set<String> classPath=ClassPathUtils.getClassPathDirAndJar();
		System.out.println(classPath);
	}
}
