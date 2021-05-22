package org.smile.util;

import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

public class TestBase64 extends TestCase{
	public void testEncode() throws UnsupportedEncodingException {
		String c2=Base64.encode("中国1233".getBytes("UTF-8"));
		String c1=Base64.encodeUTF("中国12333");
		System.out.println(new String(Base64.decode(c1)));
		System.out.println(c1);
		String s1=Base64.decodeUTF(c2);
		System.out.println(s1);
	}
	
	
	public void testDecode() throws UnsupportedEncodingException {
		char[] arr=Base64.encodeToChar("中国0999".getBytes("UTF-8"));
		byte[] arr2=Base64.encodeToByte("中国0999".getBytes());
		byte[] arr3=Base64.decode(arr);
		byte[] arr4=Base64.decode(arr2);
		System.out.println(new String(arr3));
		System.out.println(new String(arr4));
	}
}
