package org.smile.datetime.format;

import java.text.DateFormatSymbols;
import java.util.TimeZone;

import org.smile.datetime.DateTimeStamp;
import org.smile.datetime.DateTime;
import org.smile.util.LocaleUtils;

public class ISO8601JdtFormatter extends AbstractFormatter {

	public ISO8601JdtFormatter() {
		preparePatterns(
				new String[] {
						"yyyy",		// 0  + year
						"MM",		// 1  + month
						"dd",		// 2  + day of month
						"D",		// 3  - day of week
						"ML",		// 4  - month long name
						"MS",		// 5  - month short name
						"DL",		// 6  - day of week long name
						"DS",		// 7  - day of week short name
						"HH",		// 8  + hour
						"mm",		// 9  + minute
						"ss",		// 10 + seconds
						"SSS",		// 11 + milliseconds
						"DDD",		// 12 -	day of year
						"ww",		// 13 - week of year
						"WWW",		// 14 - week of year with 'W' prefix
						"W",		// 15 - week of month
						"E", 		// 16 - era
						"TZL",		// 17 - timezone long name
						"TZS",		// 18 - timezone short name
						"YYYY",		// 19    -     自然周年份
						"YY",		// 20    -     自然周年份二位
						"yy",       // 21    -     年份二位
						"a",        // 22    -     上午下午
						"hh",       // 23    -   十二小时制小时
						"M"		// 24    -      月份可变1-2位
				}
		);
	}

	@Override
	protected String convertPattern(int patternIndex, DateTime jdt) {
		DateFormatSymbols dfs = LocaleUtils.getDateFormatSymbols(jdt.getLocale());
		switch (patternIndex) {
			case 0:
				return printPad4(jdt.getYear());
			case 1:
				return print2(jdt.getMonth());
			case 2:
				return print2(jdt.getDay());
			case 3:
				return Integer.toString(jdt.getDayOfWeek());
			case 4:
				return dfs.getMonths()[jdt.getMonth() - 1];
			case 5:
				return dfs.getShortMonths()[jdt.getMonth() - 1];
			case 6:
				return dfs.getWeekdays()[(jdt.getDayOfWeek() % 7) + 1];
			case 7:
				return dfs.getShortWeekdays()[(jdt.getDayOfWeek() % 7) + 1];
			case 8:
				return print2(jdt.getHour());
			case 9:
				return print2(jdt.getMinute());
			case 10:
				return print2(jdt.getSecond());
			case 11:
				return print3(jdt.getMillisecond());
			case 12:
				return print3(jdt.getDayOfYear());
			case 13:
				return print2(jdt.getWeekOfYear());
			case 14:
				return 'W' + print2(jdt.getWeekOfYear());
			case 15:
				return Integer.toString(jdt.getWeekOfMonth());
			case 16:
				return jdt.getEra() == 1 ? dfs.getEras()[1] : dfs.getEras()[0];
			case 17:
				return jdt.getTimeZone().getDisplayName(
						jdt.isInDaylightTime(),
						TimeZone.LONG,
						jdt.getLocale());
			case 18:
				return jdt.getTimeZone().getDisplayName(
						jdt.isInDaylightTime(),
						TimeZone.SHORT,
						jdt.getLocale());
			case 19:
				return printPad4(jdt.getYearOfWeek());
			case 20:
				return print2(jdt.getYearOfWeek()%100);
			case 21:
				return print2(jdt.getYear()%100);
			case 22:
				return dfs.getAmPmStrings()[jdt.getHour()/12];
			case 23:
				return print2(jdt.getHour()>=12?jdt.getHour()-12:jdt.getHour());
			case 24:
				int m=jdt.getMonth();
				return String.valueOf(m);
			default:
				return new String(patterns[patternIndex]);
		}
	}

	@Override
	protected void parseValue(int patternIndex, String value, DateTimeStamp destination) {
		int v = Integer.parseInt(value);
		switch (patternIndex) {
			case 0:		destination.year = v; break;
			case 1:		destination.month = v; break;
			case 2:		destination.day = v; break;
			case 8:		destination.hour = v; break;
			case 9:		destination.minute = v; break;
			case 10:	destination.second = v; break;
			case 11:	destination.millisecond = v; break;
			default:
				throw new IllegalArgumentException("Invalid template: " + new String(patterns[patternIndex]));
		}
	}
}