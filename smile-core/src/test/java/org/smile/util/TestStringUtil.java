package org.smile.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestStringUtil {
	@Test
	public void test(){
		String s=StringUtils.replaceFlag("hello {0} 你好 ","胡真山");
		assertEquals("hello 胡真山 你好 ", s);
	}
	
	@Test
	public void testAfter() {
		String string="qwer1033ee10334410ddgg103";
		assertEquals("3", StringUtils.substringAfterLast(string, "10",string.length()-1));
		assertEquals("33ee10334410ddgg103", StringUtils.substringAfter(string, "10"));
	}
	@Test
	public void testConcat() {
		assertEquals("qc12", StringUtils.concat("qc",null,"12"));
	}
	@Test
	public void testSplit() {
		String args[] =StringUtils.splitc("aa", 'a',false);
		System.out.println(args.length);
		System.out.println(StringUtils.join(args,','));
		
		String[] aa=StringUtils.splitc(",a,,b,", ',');
		System.out.println(aa.length);
		System.out.println(StringUtils.join(aa,'/'));
	}
	@Test
	public void testOmit() {
		String str="1234567890";
		String res=StringUtils.omit(str, 4);
		assertEquals("1234[省略6字]", res);
	}
	@Test
	public void testLimit() {
		String str="1234567890";
		String res=StringUtils.limit(str, 2,5);
		assertEquals("34567", res);
		res=StringUtils.limit(str, 2,20);
		assertEquals("34567890", res);
		
	}
	
}
