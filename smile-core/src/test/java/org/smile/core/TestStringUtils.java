package org.smile.core;

import junit.framework.TestCase;

import org.smile.util.StringUtils;

public class TestStringUtils extends TestCase {

	public void testRemove() {
		assertEquals("1245789", StringUtils.remove("123456789", new char[] { '3', '6' }));

	}
	public void testContainsAny() {
		assertEquals(true, StringUtils.containsAny("123456789", new char[] { '1', 'A' }));
	}

	public void testRepeat() {
		assertEquals("12A12A12", StringUtils.repeat("12", "A", 3));
	}

	public void testRight() {
		assertEquals(StringUtils.right("1234567", 4), "4567");
	}

	public void testReverse() {
		assertEquals(StringUtils.reverse("1234567"), "7654321");
		assertEquals(StringUtils.reverse("123456"), "654321");
		assertEquals(StringUtils.reverse("1"), "1");
	}

	public void testJoin() {
		int a[] = { 1, 2, 3 };
		assertEquals("123", StringUtils.join(a, null));
		assertEquals("1,2,3", StringUtils.join(a, ","));
		String[] s = { "10", "20", "30" };
		assertEquals("10,20,30", StringUtils.join(s, ","));
		assertEquals("10,20", StringUtils.join(s, ",", 0, 2));
		assertEquals("1020", StringUtils.join(10, 20));
	}

	public void testStr() {
		String s1 = "www";
		String s2 = "www";
		assertEquals(true, s1 == s2);
	}

	public void testSubstring() {
		String s = "13452612789012";
		String sb = StringUtils.substringBetweenFirstAndLast(s, "1", "2");
		String sb2 = StringUtils.substringBetween(s, "1", "2");
		assertEquals(sb, "345261278901");
		assertEquals(sb2, "345");
		assertEquals(StringUtils.substringBetween(s, "34", "12"), "526");
	}

	public void testIndex() {
		int idx = "1234".indexOf("");
		assertEquals(0, idx);
		String str = "1234567654321";
		String s = StringUtils.substringBefore(str, "2");
		assertEquals(s, "1");
		s = StringUtils.substringBefore(str, "2");
		assertEquals(s, "1");
		s = StringUtils.substringBeforeLast(str, "2");
		assertEquals(s, "12345676543");
		s = StringUtils.substringAfter(str, "2");
		assertEquals(s, "34567654321");
		s = StringUtils.substringAfterLast(str, "2");
		assertEquals(s, "1");
	}

	public void testSplitc() {
		String sp = "<GS>";
		String[] cs = StringUtils.split("12<GS>SB<GS>3SB123", sp);
		assertEquals(3, cs.length);
	}

}
