package org.smile.core;

import junit.framework.TestCase;

import org.junit.Test;
import org.smile.util.Md5;
import org.smile.util.MessageDigests;

public class TestMessageDigests {
	@Test
	public void testMd5(){
		String md51=MessageDigests.md5("128", "utf-8");
		String md52=Md5.encrypt("128");
		TestCase.assertEquals(md51, md52);
	}
}
