package org.smile.util;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Map;

import org.smile.Smile;
import org.smile.collection.ThreadLocalMap;
import org.smile.commons.SmileRunException;
import org.smile.commons.StringBand;
import org.smile.commons.Strings;

/**
 * 日期工具类 具日期转换等方法
 * 
 * @author 胡真山
 *
 */
public class DateUtils {
	/**把format缓存在线程对象中*/
	private final static Map<String,DateFormat> FORMATS=new ThreadLocalMap<String,DateFormat>();
	/** 一天的毫秒数 */
	public final static int ONE_DAY_MILLS = 24 * 60 * 60 * 1000;
	/** 一天的秒数 */
	public final static int ONE_DAY_SECONDS = 24 * 60 * 60;
	/** 一个小时的毫秒数 */
	public final static int HOUR_MILLS = 60 * 60 * 1000;
	/** 一分钟的毫秒数 */
	public final static int MINUTE_MILLS = 60 * 1000;
	/** 默认只有日期的格式 */
	protected final static DateFormat dayDf = getFormat("yyyy-MM-dd");
	/** 年月日时分秒 */
	protected final static DateFormat defaultDf=getFormat("yyyy-MM-dd HH:mm:ss");
	/** 年月日时分秒毫秒 */
	protected final static DateFormat defaultSSSDf = getFormat("yyyy-MM-dd HH:mm:ss,SSS");
	/** yyyyMMdd的格式化 */
	protected final static DateFormat yyyyMMdd = getFormat("yyyyMMdd");
	/** 此工具适用的转换格式 */
	private final static Map<String, DateFormat> ACCEPT_DATE_FORMATS = new LinkedHashMap<String, DateFormat>();
	
	// 支持转换的日期格式
	static {
		ACCEPT_DATE_FORMATS.put("yyyy-MM-dd HH:mm:ss", defaultDf);
		ACCEPT_DATE_FORMATS.put("yyyy-MM-dd", dayDf);
		ACCEPT_DATE_FORMATS.put("yyyy/MM/dd", getFormat("yyyy/MM/dd"));
		ACCEPT_DATE_FORMATS.put("yyyyMMdd", yyyyMMdd);
		ACCEPT_DATE_FORMATS.put("yy-MM-dd", getFormat("yy-MM-dd"));
		ACCEPT_DATE_FORMATS.put("yy/MM/dd", getFormat("yy/MM/dd"));
		ACCEPT_DATE_FORMATS.put("MM/dd/yyyy HH:mm:ss", getFormat("MM/dd/yyyy HH:mm:ss"));
		ACCEPT_DATE_FORMATS.put("MM/dd/yyyy", getFormat("MM/dd/yyyy"));
	}

	/**
	 * 得到一个DateFormat 对象 以一个字符串格式
	 * 
	 * @param format
	 * @return
	 */
	public static DateFormat getFormat(String format) {
		DateFormat df = FORMATS.get(format);
		if (df == null) {
			df=new SimpleDateFormat(format);
			//一年第一周最少要有几天
			df.getCalendar().setMinimalDaysInFirstWeek(Smile.config.getInteger(Smile.MINIMAL_DAYS_INFIRST_WEEK, 4));
			//一个星期的第一天是星期几
			df.getCalendar().setFirstDayOfWeek(Smile.config.getInteger(Smile.FIRST_DAY_OF_WEEK, Calendar.MONDAY));
			FORMATS.put(format, df);
		}
		return df;
	}

	/**
	 * 日期与字符串之间的转换方法 是字符串就转成日期 是日期就转成字符串
	 * 
	 * @param value
	 *            一个要转换的值 可以是日期格式的字符串 或 日期
	 * @return
	 */
	public static Object convert(Object value) {
		if (value instanceof String) {
			return parseDate((String) value);
		} else if (value instanceof Date) {
			// 服务器向浏览器输出时，进行Date to String的类型转换
			Date date = (Date) value;
			return defaultDf.format(date);
		}
		throw new SmileRunException("not support convert " + value);
	}

	/**
	 * 转换了日期类型
	 * 
	 * @param value
	 *            可以是数字 字符串
	 * @return
	 */
	public static Date convertToDate(Object value) {
		if (value instanceof Date) {
			return (Date) value;
		} else if (value instanceof String) {
			// 浏览器向服务器提交时，进行String to Date的转换
			String dateString = (String) value;
			if (Strings.BLANK.equals(dateString)) {
				return null;
			}
			// 获取日期的字符串
			for (DateFormat format : ACCEPT_DATE_FORMATS.values()) {
				try {
					synchronized (format) {
						// 遍历日期支持格式，进行转换
						return format.parse(dateString);
					}
				} catch (ParseException e) {
					continue;
				}
			}
		} else if (value instanceof Number) {
			Number n = (Number) value;
			Date date = new Date(n.longValue());
			return date;
		}
		throw new SmileRunException("can convert " + value + " to date");
	}

	/**
	 * 日期转换
	 * 
	 * @param value
	 *            要转换的值 可以是日期类型 和 字符串 类型
	 * @param format
	 *            格式
	 * @return
	 * @throws Exception
	 */
	public static Object convert(Object value, String format) {
		try {
			if (value instanceof String) {
				// 浏览器向服务器提交时，进行String to Date的转换
				String dateString = (String) value;
				DateFormat df = getFormat(format);
				// 获取日期的字符串
				return df.parse(dateString);
			} else if (value instanceof Date) {
				// 服务器向浏览器输出时，进行Date to String的类型转换
				Date date = (Date) value;
				DateFormat df = getFormat(format);
				return df.format(date);
			}
		} catch (ParseException e) {
			throw new SmileRunException("日期转换出错，请检查格式是否正确" + e);
		}
		throw new SmileRunException("not support convert " + value + " format " + format);
	}

	/**
	 * 格式化一个日期
	 * 
	 * @param date
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public static String formatDate(Date date, String text) {
		DateFormat df = getFormat(text);
		return df.format(date);
	}

	/**
	 * 格式化日期格式的字符串
	 * 
	 * @param date
	 * @param text
	 * @return
	 */
	public static String formatDate(String date, String text) {
		Date d = parseDate(date);
		return formatDate(d, text);
	}

	/**
	 * 当前日期的一个相隔的日期
	 * 
	 * @param day
	 *            相隔的天数 如：-1 当前日期前一天 的日期 ，5当前日期的5天后的日期
	 * @return
	 */
	public static Date getDateBetweenNow(int day) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.add(Calendar.DATE, day);
		return c.getTime();
	}

	/**
	 * 把一个日期转换成一个适用于做为结束日期查询的日期 其实就是把一个日期转换成'yyyy-MM-dd 23:59:59" 这种日期
	 * 
	 * @param d
	 * @return
	 */
	public static Date formatToBeginDate(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	/**
	 * 把一个日期转换成一个适用于做为起始日期查询的日期 其实就是把一个日期转换成'yyyy-MM-dd 00:00:00" 这种日期
	 * 
	 * @param d
	 * @return
	 */
	public static Date formatToEndDate(Date d) {
		String str = (String) convert(d, "yyyy-MM-dd");
		return (Date) convert(str + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 处理oracle时间类型
	 * 
	 * @param value
	 * @return
	 */
	public static Timestamp getOracleTimestamp(Object value) {
		try {
			Class clz = value.getClass();
			Method m = clz.getMethod("timestampValue");
			return (Timestamp) m.invoke(value, new Object[] {});
		} catch (Exception e) {
			throw new SmileRunException("get orale time error", e);
		}
	}

	/**
	 * 
	 * @param mss 要转换的毫秒数
	 * @return 该毫秒数转换为 *天*小时*分* 后的格式
	 */
	public static String formatDuring(long mss) {
		long days = mss / (1000 * 60 * 60 * 24);
		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		return days + "天" + hours + "小时";
	}

	/**
	 * 将秒数换算成x天x时x分x秒
	 * 
	 * @param totalSecond
	 * @return
	 */
	public static String formatSecond(long totalSecond) {
		int mi = 60;
		int hh = mi * 60;
		int dd = hh * 24;

		long day = totalSecond / dd;
		long hour = (totalSecond - day * dd) / hh;
		long minute = (totalSecond - day * dd - hour * hh) / mi;
		long second = totalSecond - day * dd - hour * hh - minute * mi;
		StringBand formatStr = new StringBand();
		if (day > 0) {
			formatStr.append(day).append("天");
		}
		if (hour > 0) {
			formatStr.append(hour).append("时");
		}
		if (minute > 0) {
			formatStr.append(minute).append("分");
		}
		formatStr.append(second).append("秒");
		return formatStr.toString();
	}

	/**
	 * 将秒数换算成x天x时x分x秒
	 * 
	 * @param millis
	 * @return
	 */
	public static String formatMillis(long millis) {
		int mi = 60000;
		int hh = mi * 60;
		int dd = hh * 24;

		long day = millis / dd;
		long hour = (millis - day * dd) / hh;
		long minute = (millis - day * dd - hour * hh) / mi;
		double second = (millis - day * dd - hour * hh - minute * mi) / 1000D;
		StringBand formatStr = new StringBand();
		if (day > 0) {
			formatStr.append(day).append("天");
		}
		if (hour > 0) {
			formatStr.append(hour).append("时");
		}
		if (minute > 0) {
			formatStr.append(minute).append("分");
		}
		formatStr.append(second).append("秒");
		return formatStr.toString();
	}

	/**
	 * 是不是在同一天 年月日 相同
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameDay(Date date1, Date date2) {
		if ((date1 == null) || (date2 == null)) {
			throw new NullPointerException("The date must not be null");
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return isSameDay(cal1, cal2);
	}

	/**
	 * 是否是同一天
	 * 
	 * @param cal1
	 * @param cal2
	 * @return
	 */
	public static boolean isSameDay(Calendar cal1, Calendar cal2) {
		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) 
				&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) 
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 是不是在同一天 年月日 相同
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameDay(long date1, long date2) {
		Date newDate1 = new Date(date1);
		Date newDate2 = new Date(date2);
		return isSameDay(newDate1, newDate2);
	}

	/**
	 * 是不是在同一天 只是秒数的日期
	 * 
	 * @param date1
	 *            秒数的日期
	 * @param date2
	 *            秒数的日期
	 * @return
	 */
	public static boolean isSameDay(int date1, int date2) {
		Date newDate1 = new Date(date1 * 1000L);
		Date newDate2 = new Date(date2 * 1000L);
		return isSameDay(newDate1, newDate2);
	}

	/**
	 * 是不是在同一个小时 不一定时同一天
	 * 
	 * @param date1
	 * @param date2
	 * @return 两个时间是不是同一个小时数
	 */
	public static boolean isSameHourValue(long date1, long date2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		c1.setTimeInMillis(date1);
		c2.setTimeInMillis(date2);

		return c1.get(Calendar.HOUR_OF_DAY) == c2.get(Calendar.HOUR_OF_DAY);

	}

	/**
	 * 比较date1是否比date2多了millsTime这么长的时间(date1>=date2)
	 * 
	 * @param date1
	 *            新的时间
	 * @param date2
	 *            老的时间
	 * @param millsTime
	 *            期望多出来的时间长度
	 */
	public static boolean compareMoreThan(Date date1, Date date2, long millsTime) {
		if ((date2.getTime() + millsTime) >= date1.getTime()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 格式化 yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String formatOnlyDate(Date date) {
		return dayDf.format(date);
	}

	/***
	 * 格式化日期
	 * 
	 * @param time
	 * @param format
	 * @return
	 */
	public static String formatDate(long time, String format) {
		return formatDate(new Date(time), format);
	}

	/**
	 * 相差的秒数
	 * 
	 * @param oldTime
	 * @param newTime
	 * @return
	 */
	public static int compareSecond(long oldTime, long newTime) {
		long between = (newTime - oldTime) / 1000;
		return (int) between;
	}

	/**
	 * 相差的分数
	 * 
	 * @param oldTime
	 * @param newTime
	 * @return
	 */
	public static long compareMinute(long oldTime, long newTime) {
		long between = (newTime - oldTime) / 1000 / 60;
		return between;
	}

	/**
	 * 两天差多久
	 * 
	 * @param oldTime
	 * @param newTime
	 * @return X天X小时X分钟X秒的字符串
	 */
	public static String compareDate(long oldTime, long newTime) {

		long range = (newTime - oldTime);

		long days = range / 1000 / 60 / 60 / 24;

		range = (range - days * 1000 * 60 * 60 * 24);

		long hours = range / 1000 / 60 / 60;

		range = (range - hours * 1000 * 60 * 60);

		long minutes = range / 1000 / 60;

		range = (range - minutes * 1000 * 60);

		long seconds = range / 1000;

		return days + "天" + hours + "小时" + minutes + "分钟" + seconds + "秒";
	}

	/**
	 * 零点时间
	 * 
	 * @param time
	 *            时间戳
	 * @return
	 */
	public static long getZeroPoint(long time) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTimeInMillis();
	}

	/** 当前时间 的前N天 */
	public static long getBeforeDay(int day) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -day);
		return c.getTimeInMillis();
	}

	/**
	 * 当前时间的前几年的时间
	 * 
	 * @param year
	 * @return
	 */
	public static Date getBeforeYear(int year) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, -year);
		return c.getTime();
	}

	/**
	 * 当前时间的前几年的时间
	 * 
	 * @param year
	 * @return
	 */
	public static Date getBeforeYear(Date date, int year) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, -year);
		return c.getTime();
	}

	/**
	 * 当前时间后N天
	 * 
	 * @param day
	 * @return
	 */
	public static Date getAfterDay(int day) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, day);
		return c.getTime();
	}

	/** 一个时间的前N天 */
	public static Date getBeforeDay(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, -day);
		return c.getTime();
	}

	/** 一个时间的后N天 */
	public static long getAfterDay(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, day);
		return c.getTimeInMillis();
	}

	/**
	 * 比较两个日期的大小，只比较到日
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareDateOfMonth(Date date1, Date date2) {
		return dayDf.format(date1).compareTo(dayDf.format(date2));
	}

	/**
	 * 比较从指定时间点当天的0点开始， 结束时间点是否在给定的天数内
	 * 
	 * @param startTime
	 *            开始时间点(毫秒)
	 * @param endTime
	 *            结束时间点(毫秒) * @param dayLimit 天数
	 * @return
	 */
	public static boolean isInTheTimeOn0(long startTime, long endTime, int dayLimit) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(startTime);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		long time = (endTime - c.getTimeInMillis()) / 1000;
		int days = (int) Math.floor(time / (24 * 60 * 60));
		if (days < dayLimit) {
			return true;
		}
		return false;
	}

	/**
	 * 比较开始时间与结束时间的时间间隔是否在给定间隔天数内
	 * 
	 * @param startTime
	 * @param endTime
	 * @param dayLimit
	 *            间隔（天）
	 * @return
	 */
	public static boolean isInTheTime(long startTime, long endTime, int dayLimit) {
		if ((startTime + dayLimit * 24 * 60 * 60 * 1000l) > endTime) {
			return true;
		}
		return false;
	}

	/**
	 * 得到今天凌晨0点的时刻
	 */
	public static Date getZeroPoint() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.set(year, month, day, 0, 0, 00);
		return calendar.getTime();
	}

	/**
	 * 以今天为基准，第N天的凌晨点，如beforeday=-1，昨天凌晨点
	 * 
	 * @param beforeday
	 * @return
	 */
	public static Date getZeroPoint(int beforeday) {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.set(year, month, day, 0, 0, 0);
		calendar.add(Calendar.DATE, beforeday);
		return calendar.getTime();
	}

	/**
	 * 判断两个日期是否属于年内同一个星期,中国时间计算周
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameWeekOfYear(Date date1, Date date2) {
		Calendar c = Calendar.getInstance();
		c.setTime(date1);
		c.add(Calendar.DATE, -1);
		int year1 = c.get(Calendar.YEAR);
		int week1 = c.get(Calendar.WEEK_OF_YEAR);
		c.setTime(date2);
		c.add(Calendar.DATE, -1);
		int year2 = c.get(Calendar.YEAR);
		int week2 = c.get(Calendar.WEEK_OF_YEAR);
		if (year1 == year2 && week1 == week2) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断两个日期是否属于同一个月份
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameMonthOfYear(Date date1, Date date2) {
		Calendar c = Calendar.getInstance();
		c.setTime(date1);
		int year1 = c.get(Calendar.YEAR);
		int month1 = c.get(Calendar.MONTH);
		c.setTime(date2);
		int year2 = c.get(Calendar.YEAR);
		int month2 = c.get(Calendar.MONTH);
		if (year1 == year2 && month1 == month2) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 今天是不是星期一
	 * 
	 * @return
	 */
	public static boolean isTodayMonday() {
		Calendar c = Calendar.getInstance();
		int weekOfDay = c.get(Calendar.DAY_OF_WEEK);
		if (weekOfDay == Calendar.MONDAY) {
			return true;
		}
		return false;
	}

	/**
	 * 今天 是不是星期中的哪一天
	 * 
	 * @param calendarDayOfWeek
	 * @return
	 */
	public static boolean isTodayOfWeek(int calendarDayOfWeek) {
		Calendar c = Calendar.getInstance();
		int weekOfDay = c.get(Calendar.DAY_OF_WEEK);
		if (weekOfDay == calendarDayOfWeek) {
			return true;
		}
		return false;
	}


	/**
	 * 判断两个时间点相隔天数 bdate>smdate : bdate是新的时间，smdate是旧的时间 例如今天 smdate为1:00
	 * bigdate为昨天23:00 返回天数1
	 * 
	 * @param bigdate
	 *            新的时间
	 * @param smdate
	 *            旧的时间
	 * @return 天数
	 */
	public static int getDiffDay(String bigdate, String smdate) {
		Date date1 = parseDate(bigdate);
		Date date2 = parseDate(smdate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		long time1 = cal.getTimeInMillis();
		cal.setTime(date2);
		long time2 = cal.getTimeInMillis();
		long betweenTimes = (time1 - time2);
		if (betweenTimes % ONE_DAY_MILLS == 0) {
			return (int) (betweenTimes / ONE_DAY_MILLS);
		} else {
			return (int) (betweenTimes / ONE_DAY_MILLS + 1);
		}
	}

	/**
	 * 是从开始时间 的第几天 是同一天为是1  第二天为2
	 * 
	 * @param startday
	 *            起算时间
	 * @param nowday
	 *            当前时间
	 * @return 同一天为 1 是同一天为是 第二天为2
	 */
	public static int getStartDays(Date startday, Date nowday) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startday);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		long oldTimes = cal.getTimeInMillis();
		cal.setTime(nowday);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		long nowtimes = cal.getTimeInMillis();
		return (int) ((nowtimes - oldTimes) / ONE_DAY_MILLS) + 1;

	}

	/**
	 * 指定的时间是否在一个时间断
	 * 
	 * @param date
	 *            时间
	 * @param startHour
	 *            开始小时数
	 * @param endHour
	 *            结束小时数
	 * @param isSameDay
	 *            是否需要是同一天
	 * @return
	 */
	public static boolean isBetweenTheHours(Date date, int startHour, int endHour, boolean isSameDay) {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		c.setTime(date);
		if (isSameDay) {
			if (year != c.get(Calendar.YEAR)) {
				return false;
			} else if (month != c.get(Calendar.MONTH)) {
				return false;
			} else if (day != c.get(Calendar.DAY_OF_MONTH)) {
				return false;
			}
		}
		int nowHour = c.get(Calendar.HOUR_OF_DAY);

		if (nowHour >= startHour && nowHour < endHour) {
			return true;
		}
		return false;
	}

	/**
	 * 当前小时数的毫秒数 取小时的整
	 * 
	 * @param hour
	 * @return
	 */
	public static long getCurHourMills(int hour) {
		Calendar cl = Calendar.getInstance();
		cl.set(Calendar.HOUR_OF_DAY, hour);
		cl.set(Calendar.MINUTE, 0);
		cl.set(Calendar.SECOND, 0);
		return cl.getTimeInMillis();
	}

	/**
	 * 得到指定月的天数(中国式月份 )
	 * 
	 * @param month
	 *            月份 1 月 转入 1
	 * @return
	 */
	public static int getMonthDayCount(int month) {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.MONTH, month - 1);
		// 把日期设置为当月第一天
		a.set(Calendar.DATE, 1);
		// 日期回滚一天，也就是最后一天,这里只是天回退 不会带到月份回退
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	/***
	 * 解析日期字符串为日期类型
	 * 
	 * @param date
	 * @return
	 */
	public static Date parseDate(String date) {
		// 获取日期的字符串
		for (DateFormat format : ACCEPT_DATE_FORMATS.values()) {
			try {
				synchronized (format) {
					return format.parse(date);
				}
			} catch (ParseException e) {
				continue;
			}
		}
		return null;
	}
	/**
	 * 解析字符串成一个日期类型
	 * @param date
	 * @param format
	 * @return
	 */
	public static Date parseDate(String date,String format){
		if(StringUtils.isEmptyAfterTrim(date)){
			return null;
		}
		try {
			return getFormat(format).parse(date);
		} catch (ParseException e) {
			throw new SmileRunException(date,e);
		}
	}
	/**
	 * 解析年周
	 * @param year 年份
	 * @param weekOfYear 所在年的周数
	 * @param weekDay 解析到周几
	 * @return
	 */
	public static Date parseYearWeek(int year,int weekOfYear,int weekDay){
		Calendar ca = new GregorianCalendar();
		if(year<100){
			year=ca.get(Calendar.YEAR)/100*100+year;
		}
		ca.set(Calendar.YEAR, year);
		ca.set(Calendar.WEEK_OF_YEAR, weekOfYear);
		ca.set(Calendar.DAY_OF_WEEK,weekDay);
		return ca.getTime();
	}
	/**
	 * 解析年周日期字符串 
	 * @param dc 年周格式日期字符串
	 * @param weekDay
	 * @return
	 */
	public static Date parseYearWeek(String dc,int weekDay){
		int year=Integer.valueOf(StringUtils.substr(dc, 0, dc.length()-2));
		int weekOfYear=Integer.valueOf(StringUtils.right(dc, 2));
		return parseYearWeek(year, weekOfYear, weekDay);
	}

	/***
	 * 一年的第几周
	 * 
	 * @param date
	 *            日期
	 * @param minfirstweek
	 *            第一周最少需要多少天
	 * @return 周数
	 */
	public static int getWeekOfYear(Date date, int minfirstweek) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.setMinimalDaysInFirstWeek(minfirstweek);
		return c.get(Calendar.WEEK_OF_YEAR);
	}

	/***
	 * 时间是一个小时中的秒数
	 * 
	 * @param date
	 * @return
	 */
	public static short getSecondOfHour(Date date) {
		Calendar c = Calendar.getInstance();
		if (date != null) {
			c.setTime(date);
		}
		int mm = c.get(Calendar.MINUTE);
		int ss = c.get(Calendar.SECOND);
		return (short) (mm * 60 + ss);
	}

	/**
	 * 默认的格式化 日期 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String defaultFormat(Date date) {
		synchronized (defaultDf) {
			return defaultDf.format(date);
		}
	}

	/**
	 * 当前日期的默认格式 化
	 * 
	 * @return
	 */
	public static String currentDefaultDate() {
		return defaultFormat(new Date());
	}

	/**
	 * 当月的第一天
	 * 
	 * @return
	 */
	public static Date getMonthFirstDay() {
		Calendar a = Calendar.getInstance();
		// 把日期设置为当月第一天
		a.set(Calendar.DATE, 1);
		return a.getTime();
	}

	/**
	 * 当月的第最后一天
	 * 
	 * @return
	 */
	public static Date getMonthLastDay() {
		Calendar a = Calendar.getInstance();
		// 把日期设置为当月第一天
		a.set(Calendar.DATE, 1);
		// 日期回滚一天，这只是回滚日 不会对月连动
		a.roll(Calendar.DATE, -1);
		return a.getTime();
	}

	/**
	 * 
	 * @param monthDiff
	 *            于当前日期差的月份值
	 * @return
	 */
	public static Date getMonthFirstDay(int monthDiff) {
		Calendar a = Calendar.getInstance();
		// 把日期设置为当月第一天
		a.set(Calendar.DATE, 1);
		a.add(Calendar.MONTH, monthDiff);
		return a.getTime();
	}

	/**
	 * 当月的第最后一天
	 * 
	 * @return
	 */
	public static Date getMonthLastDay(Date date) {
		Calendar a = Calendar.getInstance();
		a.setTime(date);
		// 把日期设置为当月第一天
		a.set(Calendar.DATE, 1);
		// 日期回滚一天，这只是回滚日 不会对月连动
		a.roll(Calendar.DATE, -1);
		return a.getTime();
	}

	/**
	 * 当前的最大一天
	 * 例如 31 或 30 29 28
	 * @param date
	 * @return
	 */
	public static int getMonthMaxDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 把日期设置为当月第一天
		cal.set(Calendar.DATE, 1);
		// 日期回滚一天，这只是回滚日 不会对月连动
		cal.roll(Calendar.DATE, -1);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 	当天开始的时间戳 0:0:0.000
	 * 
	 * @param times
	 * @return
	 */
	public static long getDateStartTimes(long times) {
		Calendar ca = Calendar.getInstance();
		ca.setTimeInMillis(times);
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		ca.set(Calendar.MILLISECOND, 0);
		return ca.getTimeInMillis();
	}
	
	/**
	 * 	重置到当天开始时间
	 * @param ca
	 */
	public static void resetToDayStart(Calendar ca) {
		ca.set(Calendar.HOUR_OF_DAY,0);
		ca.set(Calendar.MINUTE,0);
		ca.set(Calendar.SECOND,0);
		ca.set(Calendar.MILLISECOND,1);
	}

	/**
	 * 获取当天过去了的时间
	 * 
	 * @param times
	 * @return
	 */
	public static int getOneDayPassTime(long times) {
		Calendar ca = new GregorianCalendar();
		ca.setTimeInMillis(times);
		int hour = ca.get(Calendar.HOUR_OF_DAY);
		int minute = ca.get(Calendar.MINUTE);
		int secend = ca.get(Calendar.SECOND);
		int millsecend = ca.get(Calendar.MILLISECOND);
		return hour * HOUR_MILLS + minute * MINUTE_MILLS + secend * 1000 + millsecend;
	}
	/**
	 * 获取日期的年份
	 * @param date
	 * @return
	 */
	public static int getYear(Date date){
		Calendar ca = new GregorianCalendar();
		ca.setTime(date);
		return ca.get(Calendar.YEAR);
	}
	/**
	 * 获取日期的年份
	 * @return
	 */
	public static int getYear(){
		Calendar ca = new GregorianCalendar();
		return ca.get(Calendar.YEAR);
	}
	/**
	 * 获取月份
	 * 第一个月是1月
	 * @return
	 */
	public static int getMonth(){
		Calendar ca = new GregorianCalendar();
		return ca.get(Calendar.MONTH)+1;
	}
	/**
	 * 获取月份
	 * 第一个月是1月
	 * @param date 
	 * @return
	 */
	public static int getMonth(Date date){
		Calendar ca = new GregorianCalendar();
		ca.setTime(date);
		return ca.get(Calendar.MONTH)+1;
	}
	
	/**
	 * 系统时期
	 * @return
	 */
	public static Date now(){
		return new Date();
	}
}
