package org.smile.datetime;

import org.junit.Test;
import org.smile.util.DateUtils;

public class TestDateTime {
	@Test
	public void test(){
		DateTime date= new DateTime("2020-09-06 12:23");
		System.out.println(date.toString("TZL E yyyy-MM-dd a HH:mm:ss"));
		System.out.println(date.toString("TZL E YYYY ww D"));
		System.out.println(date);
		System.out.println(DateUtils.formatDate(date.convertToDate(), "YYww"));
		System.out.println(new DateTime(DateTime.JD_2001).toString("yyyyMMdd hh:mm:ss"));
	}
}
