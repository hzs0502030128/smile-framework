package org.smile.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.smile.datetime.DateTime;
import org.smile.datetime.JulianDate;
import org.smile.util.DateUtils;

public class TestDate extends TestCase{
	public void testDateParse(){
		Date date=DateUtils.parseDate("20201220", "yyyyMMdd");
		assertEquals(DateUtils.formatOnlyDate(date), "2020-12-20");
	}
	
	public void testDateTimeFormat() {
		DateTime dt=new DateTime(2020,10,12);
		System.out.println(015);
		System.out.println(dt.format("yyyyMMd HH:mm:ss.SSS a"));
		System.out.println(dt.format("TZS E yyyy年M月dd日 a hh时mm分ss秒SSS DL 'W'ww"));
	}
	
	public void testDate(){
		Date date=DateUtils.parseDate("20201220", "yyyyMMdd");
		int max=DateUtils.getMonthMaxDay(date);
		assertEquals(31, max);
		assertEquals(29, DateUtils.getMonthMaxDay(DateUtils.parseDate("20200220", "yyyyMMdd")));
		assertEquals(DateUtils.getYear(date), 2020);
		assertEquals(DateUtils.getMonth(date), 12);
	}
	
	public void testJulianDate() {
		DateTime date=new DateTime(2020,8,1);
		System.out.println(date);
		date.setWeekDefinition(DateTime.MONDAY, 4);
		date.setWeekNumber(2);
		assertEquals("2020-08-10",date.setFormat("yyyy-MM-dd").toString());
		date.setWeekOfMonth(1,6);
		assertEquals("2020-08-08", date.toString());
	}
	
	public void testFormat(){
		Date d=DateUtils.parseDate("2039","YYww");
		String ds=DateUtils.formatDate(d,"yyyy-MM-dd");
		DateTime td=new DateTime(2020, 9, 20);
		System.out.println(td.format("YYww"));
		System.out.println(ds);
	}
	
	public void testParseYW(){
		Date d=DateUtils.parseYearWeek("2010", Calendar.MONDAY);
		assertEquals("2020-03-02", DateUtils.formatOnlyDate(d));
	}
}
