package org.smile.time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.smile.collection.CollectionUtils;
import org.smile.commons.Strings;
import org.smile.util.DateUtils;
import org.smile.util.RegExp;
import org.smile.util.StringUtils;

/**
 * 时间表达式
 * 
 */
public class TimeExpression implements FireOnExpression {
	private static final RegExp VALUE_SPLIT=new RegExp("[\\]\\[]+");
	Expression[] expressions;

	// [*][*][*][1:40-2:50](可选[0/100] 表示100秒执行一次)
	// new Expression("[*,yyyy,yyyy-yyyy][*,mm,mm-mm][*,dd,dd-dd][*,h1-h2]");
	// 时间表达式格式 [年][月][日][时间段];[年][月][日][时间段];....
	// 用";"隔开并列的表达式, 每个"[]"里都可以用"*"表示不对该日期段进行限制,用","表示并列,用"-"表示从哪儿到哪儿
	// 例[*][5,6,8-9][1,3,5,7][1-2:30,3:00-5:00]
	// 表示5月6月,8月至9月,每月的1号3号5号,7号,在这些限定日期内的每日的1:00-2:30,3:00-5:00
	// 例[*][*][1,3,5,7][*];[*][*][2,4,6,8][10-20:30]表示
	static public String getHelp() {
		String t = "时间表达式格式 为  [年][月][日][时间段];[年][月][日][时间段];....\r\n";
		t = t + "用\";\"隔开并列的表达式, 每个\"[]\"里都可以用\"*\"表示不对该日期段进行限制,用\",\"表示并列,用\"-\"表示从哪儿到哪儿\r\n";
		t = t + "例 [*][5,6,8-11][1,3,5,7][1-2:30,3:00-5:00]\r\n";
		t = t + "表示每5月,6月,8月至11月,每月的1号3号5号,7号,在这些限定日期内的每日的1:00-2:30,3:00-5:00\r\n";
		t = t + "例[*][*][1,3,5,7][*];[*][*][2,4,6,8][10-20:30]\r\n";
		t = t + "表示1号3号5号7号的任意时间和2号4号6号8号的10:00-20:30\r\n";
		return t;
	}
	/**
	 * 时间表达式
	 * @param experssions
	 */
	public TimeExpression(String experssions) {
		experssions = experssions.trim();
		if (experssions.endsWith(";")) {
			experssions = experssions.substring(0, experssions.length() - 1);
		}
		String[] exarray = experssions.split(";");
		Expression[] expressionArray = new Expression[exarray.length];
		for (int i = 0; i < exarray.length; i++) {
			expressionArray[i] = new Expression(exarray[i]);
		}
		expressions = expressionArray;
	}

	/**
	 * 判断一个时间是不是在当前表达式之内
	 * @param times 要判断的时间
	 * @return
	 */
	@Override
	public boolean isValidTime(long times) {
		for (Expression item : expressions) {
			if (item.isValidateTime(times)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 只判断到天数
	 * @param time
	 * @return
	 */
	public boolean isValidDate(long time) {
		for (Expression item : expressions) {
			if (item.isValidateDate(time)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 是否是时间段内  判断到秒
	 * @param time
	 * @return
	 */
	public Expression getExpression(long time){
		for (Expression item : expressions) {
			if (item.isValidateTime(time)) {
				return item;
			}
		}
		return null;
	}
	
	/**
	 * 是否是所有的年份
	 * @return
	 */
	public boolean isAllYear() {
		for (Expression item : expressions) {
			if (item.isAllYear()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取此时间后的下一次合符表达式的时间
	 * @param times 从此时间这后开始查找
	 * @return
	 */
	@Override
	public Date getNextValidTimeAfter(long times){
		Calendar ca=Calendar.getInstance();
		ca.setTimeInMillis(times);
		long next=-1;
		for (Expression item : expressions) {
			long n=item.getNextValidateTime(ca);
			if(n>0){
				if(next==-1){
					next=n;
				}else{
					next=next<n?next:n;
				}
			}
		}
		if(next>0){
			return new Date(next);
		}
		return null;
	}
	/**
	 * 获取刷新时间点
	 * @param time
	 * @return
	 */
	public String getStart(long time) {
		List<String> l = new ArrayList<String>();
		for (Expression item : expressions) {
			List<String> horseExp = item.getHourExp(time);
			if (horseExp.contains("*")) {
				return "即时";
			}
			for (String string : horseExp) {
				if (!l.contains(string)) {
					l.add(string);
				}
			}
		}
		Collections.sort(l);
		if (l.size() > 0) {
			return l.get(0);
		} else {
			return "当日不刷新";
		}
	}
	/**
	 * 获取当前时间表达式剩余执行时间
	 * @param time 毫秒数
	 * @return
	 */
	public int getLeaveTime(long time){
		for (Expression item : expressions) {
			int leaveTime=item.getLeaveTime(time);
			if(leaveTime<0||leaveTime>0){
				return leaveTime;
			}
		}
		return 0;
	}
	
	/**时间表达式*/
	public class Expression {
		String experssion;
		/**年*/
		Set<Value> years;
		/**月*/
		Set<Value> months;
		/**天*/
		Set<Value> days;
		/**时期*/
		Set<Value> weeks;
		/**时间段*/
		Set<String> hours;
		/**秒*/
		Set<Value> seconds;
		
		Set<Value> onlyWeeks=new TreeSet<TimeExpression.Value>();
		
		Set<Value> onlyDays=new TreeSet<TimeExpression.Value>();

		/**
		 * [年][月][日][时间段]
		 * 
		 * [年][月][日][时间段][秒频次]
		 * 
		 * 	两种类型
		 * @param experssion
		 */
		Expression(String experssion) {
			String[] t =VALUE_SPLIT.split(experssion,true);
			if (t.length <4 || t.length>5) {
				throw new IllegalStateException("不合法的表达式:"+experssion+"必须是[年][月][日][时间]格式");
			}
			years = parseToValueSet(t[0]);
			months =parseToValueSet(t[1]);
			days =parseToValueSet(t[2]);
			hours =CollectionUtils.linkedHashSet(StringUtils.splitc(t[3],','));
			if (years.size() == 0 || months.size() == 0 || days.size() == 0 || hours.size() == 0) {
				throw new IllegalStateException("不合法的表达式:"+experssion);
			}
			if(t.length>4){
				seconds=parseToValueSet(t[4]);
			}
			//分开week 和  day
			for(Value v:days){
				if(v.isWeek()){
					onlyWeeks.add(v);
				}else{
					onlyDays.add(v);
				}
			}
		}

		/**
		 * 解析一个表达式段
		 * @param valueExp
		 * @return
		 */
		private Set<Value> parseToValueSet(String valueExp){
			Set<Value> values=new TreeSet<Value>();
			String[] strs=StringUtils.splitc(valueExp,',');
			for(String s:strs){
				Value v=Value.valueOf(s);
				values.add(v);
			}
			return values;
		}
		
		/**
		 * 是否在当前表达式的时间段内
		 * @param time
		 * @return
		 */
		public boolean isValidateTime(long time) {
			if (isValidateDate(time)) {
				int hourtime = DateUtils.getOneDayPassTime(time);
				if (isInHours(hours, hourtime)) {
					if(seconds==null){
						return true;
					}
					return isInValues(seconds,(hourtime/1000)%3600);
				}
			}
			return false;
		}

		/**
		 * 判断值是否一值范围内
		 * 用于判断年 月 日
		 */
		private boolean isInValues(Set<Value> values, int value) {
			for (Value item : values) {
				if (item.isAll()) {
					return true;
				}else if(item.isInValue(value)) {
					return true;
				}
			}
			return false;
		}
		/**
		 * 获取当前日表达式执行的时间点
		 * @param time
		 * @return
		 */
		public List<String> getHourExp(long time) {
			List<String> l = new ArrayList<String>();
			if (isValidateDate(time)) {
				GregorianCalendar ca = new GregorianCalendar();
				int hour = ca.get(Calendar.HOUR_OF_DAY);
				int minute = ca.get(Calendar.MINUTE);
				int secend = ca.get(Calendar.SECOND);
				int millsecend = ca.get(Calendar.MILLISECOND);
				int hourtime = hour * 60 * 60 * 1000 + minute * 60 * 1000 + secend * 1000 + millsecend;
				for (String item : hours) {
					if (item.equals("*")) {
						l.clear();
						l.add("*");
						return l;
					}
					if (item.indexOf("-") != -1) {// 数字段的
						String[] seg = item.split("-");
						String[] start = seg[0].split(":");
						String[] end = seg[1].split(":");
						int starthour = Integer.parseInt(start[0]) * 60 * 60 * 1000;
						if (start.length > 1) {
							starthour = starthour + Integer.parseInt(start[1]) * 60 * 1000;
						}
						int endhour = Integer.parseInt(end[0]) * 60 * 60 * 1000;
						if (end.length > 1) {
							endhour = endhour + Integer.parseInt(end[1]) * 60 * 1000;
						}
						if (hourtime <= starthour) {
							l.add(seg[0]);
						}
					}
				}
			}
			return l;
		}

		/**
		 * 获取当前日表达式剩余执行时间
		 * @param time 剩余的毫秒数
		 * @return
		 */
		public int getLeaveTime(long time) {
			int hourtime = DateUtils.getOneDayPassTime(time);
			for (String item : hours) {
				if("*".equals(item)){
					return -1;
				}
				if (item.indexOf("-") != -1) {// 数字段的
					String[] seg = item.split("-");
					int startTime = parseTimeToMillis(seg[0]);
					int endTime = parseTimeToMillis(seg[1]);
					if (startTime<=hourtime&&hourtime<=endTime) {
						 return endTime-hourtime;
					}
				}
			}
			return 0;
		}

		/**
		 * 当前日期内是否执行 只判断到天
		 * @param time 要判断的时间
		 * @return
		 */
		public boolean isValidateDate(long time) {
			Calendar ca = new GregorianCalendar();
			ca.setTimeInMillis(time);
			int year = ca.get(Calendar.YEAR);
			if (!isInValues(years, year)) {
				return false;
			}
			int month = ca.get(Calendar.MONTH) + 1;
			if (!isInValues(months, month)) {
				return false;
			}
			
			
			int week = ca.get(Calendar.DAY_OF_WEEK) - 1;
			if (week == 0) {
				week = 7;
			}
			int day = ca.get(Calendar.DAY_OF_MONTH);
			for(Value item:days){
				if(item.isWeek()){
					if(item.isInValue(week)){
						return true;
					}
				}else{
					if(item.isInValue(day)){
						return true;
					}
				}
			}
			return false;
		}

		/**
		 * 是否在时间段时,只计算到分钟
		 * @param array
		 * @param checkhourtime
		 * @return
		 */
		protected boolean isInHours(Set<String> array, int checkhourtime) {
			for (String item : array) {
				if (item.equals("*")) {
					return true;
				}
				if (item.indexOf("-") != -1) {// 数字段的
					String[] seg = item.split("-");
					int startTime=parseTimeToMillis(seg[0]);
					int endTime = parseTimeToMillis(seg[1]);
					if (checkhourtime >= startTime && checkhourtime <= endTime) {
						return true;
					}
				}else{
					throw new IllegalStateException("不合法的表达式时间段："+item);
				}
			}
			return false;
		}
		/**
		 * 把时间转成毫秒
		 * @return
		 */
		private int parseTimeToMillis(String time){
			String[] timeSplit=StringUtils.splitc(time, ':');
			//小时
			int starthour = Integer.parseInt(timeSplit[0]) * 60 * 60 * 1000;
			//分
			if (timeSplit.length > 1) {
				starthour = starthour + Integer.parseInt(timeSplit[1]) * 60 * 1000;
			}
			//秒
			if(timeSplit.length>2){
				starthour = starthour + Integer.parseInt(timeSplit[2]) * 1000;
			}
			return starthour;
		}
				
		/**
		 * 以一个日期获取一个开始时间
		 * @param currentTimes
		 * @return
		 */
		public long getStartTimes(long currentTimes){
			int hourtime =DateUtils.getOneDayPassTime(currentTimes);
			for (String item : this.hours) {
				if (item.equals("*")) {
					return 0;
				}
				if (item.indexOf("-") != -1) {// 数字段的
					String[] seg = item.split("-");
					int starthour = parseTimeToMillis(seg[0]);
					int endhour = parseTimeToMillis(seg[1]);
					if (hourtime >= starthour && hourtime <= endhour) {
						return currentTimes-(hourtime-starthour);
					}
				}
			}
			return -1;
		}
		
		protected long getSecondsValidTime(long dayStart,long times){
			int passDay=DateUtils.getOneDayPassTime(times);
			int sc= getSecend((passDay/1000)%3600);
			if(sc>0){
				return dayStart+(passDay/3600000*3600+sc)*1000;
			}
			return -1;
		}
		
		protected long getNextValidateTimeOfHours(long times) {
			long dayStart=DateUtils.getDateStartTimes(times);
			for (String item : this.hours) {
				if (item.equals("*")) {
					if(seconds==null){
						return times+1000;
					}else{
						long sc=getSecondsValidTime(dayStart, times+1000);
						if(sc>0){
							return sc;
						}
					}
				}else if (item.indexOf("-") != -1) {// 数字段的
					String[] seg = item.split("-");
					int starthour = parseTimeToMillis(seg[0]);
					long start=dayStart+starthour;
					if(start>times){
						if(seconds==null){
							return start;
						}else{
							long sc=getSecondsValidTime(dayStart, start);
							if(sc>0){
								return sc;
							}
						}
					}else{
						long end=dayStart+parseTimeToMillis(seg[1]);
						if(seconds==null){
							if(end>=times+1000){
								return times+1000;
							}
						}else if(end>=times){
							long sc=getSecondsValidTime(dayStart, times+1000);
							if(sc>0&&sc<end){
								return sc;
							}
						}
					}
				}
			}
			return -1;
		}
		/**
		 * 下一个在表达式中的时间戳
		 */
		protected long getNextValidateTime(Calendar current){
			if(isValidateDate(current.getTimeInMillis())){
				//当天开始时间
				long result=getNextValidateTimeOfHours(current.getTimeInMillis());
				if(result>0) {
					return result;
				}else {//当天已经打不到合适的时间段,需要增加一天
					current.add(Calendar.DAY_OF_MONTH, 1);
					DateUtils.resetToDayStart(current);
					return getNextValidateTime(current);
				}
			}
			Date date=getNextFireDay(current);
			if(date!=null){
				//当天开始时间
				long times=date.getTime();
				long result=getNextValidateTimeOfHours(times);
				if(result>0) {
					return result;
				}
			}
			return -1;
		}
		
		private int getSecend(long dayTime){
			if(seconds!=null){
				return getNextMinValue(seconds, (int)dayTime);
			}else{
				return (int)dayTime;
			}
		}
		/**
		 * 是不是所有的年都满足
		 * @return
		 */
		private boolean isAllYear() {
			for (Value year : years) {
				if (year.isAll()) {
					return true;
				}
			}
			return false;
		}
		/**
		 * 获取年份
		 * @param date
		 * @return
		 */
		protected int getNextFireYear(Calendar ca){
			int cYear=ca.get(Calendar.YEAR);
			return getNextMinValue(years, cYear);
		}
		
		/**
		 * 从一组值里查找  如获取不到返回-1
		 * @param valueArray
		 * @param cValue
		 * @return
		 */
		private int getNextMinValue(Set<Value> valueArray,int cValue){
			int minValue=-1;
			for(Value value : valueArray){
				if(value.isAll()){
					return cValue;
				}else if(value.single){
					if(cValue<=value.start){
						minValue=minValue==-1?value.start:Math.min(minValue, value.start);
					}
				}else if(value.frequency){
					int n=getNextFrequency(value, cValue);
					minValue= minValue==-1?n:Math.min(minValue,n);
				}else{// 数字段的
					if(cValue<value.start){
						minValue=minValue==-1?value.start:Math.min(minValue, value.start);
					}else if(value.start<=cValue&&cValue<=value.end){
						minValue=minValue==-1?cValue:Math.min(minValue,cValue);
					}
				}
			}
			return minValue;
		}
		
		private int getNextFrequency(Value value,int cValue){
			if(value.isInfrequency(cValue)){
				return cValue;
			}
			int v= cValue/value.end*value.end+value.start;
			if(v>cValue){
				return v;
			}else{
				return v+value.end;
			}
		}
		
		protected int getNextFireMonth(Calendar ca){
			int cMonth=ca.get(Calendar.MONTH)+1;
			return getNextMinValue(months, cMonth);
		}
		
		/**
		 * 固定了月份
		 * @param ca
		 * @return
		 */
		protected Date getNextFireDayOnFixedMonth(Calendar ca){
			int week = ca.get(Calendar.DAY_OF_WEEK) - 1;
			if (week == 0) {
				week = 7;
			}
			int day = ca.get(Calendar.DAY_OF_MONTH);
			int resultDay=getNextMinValue(onlyDays, day);
			if(resultDay==day){//是当天日期
				return ca.getTime();
			}
			//不是当天需重置时分秒
			DateUtils.resetToDayStart(ca);
			if(onlyWeeks.size()==0){
				if(resultDay<0) {//日期天数查找不到又没有星期的时候
					return null;
				}
				ca.add(Calendar.DAY_OF_MONTH, resultDay-day);
				return ca.getTime();
			}
			int resultWeek=getNextMinValue(onlyWeeks, week);
			if(resultWeek<0){
				int maxDay=DateUtils.getMonthMaxDay(ca.getTime());
				//下个星期
				int nextWeek=getNextMinValue(onlyWeeks, 0);
				int nextWeekDay=nextWeek+7-week+day;
				if(nextWeekDay<=maxDay){
					if(resultDay>0){
						resultDay=nextWeekDay<resultDay?nextWeekDay:resultDay;
					}else{
						resultDay=nextWeekDay;
					}
				}
			}else{
				int nextWeekDay=resultWeek-week+day;
				if(resultDay>0){
					resultDay=nextWeekDay<resultDay?nextWeekDay:resultDay;
				}else{
					resultDay=nextWeekDay;
				}
			}
			if(resultDay>0){
				ca.set(Calendar.DAY_OF_MONTH, resultDay);
				return ca.getTime();
			}
			return null;
		}
		/**
		 * 	固定了年份
		 * @param current
		 * @return
		 */
		private Date getNextFireDayOneFixedYear(Calendar current){
			int month=getNextFireMonth(current);
			if(month>0){
				int m=current.get(Calendar.MONTH)+1;
				if(m==month){//是当前月
					Date date= getNextFireDayOnFixedMonth(current);
					if(date==null) {//
						current.add(Calendar.MONTH, 1);
						current.set(Calendar.DAY_OF_MONTH, 1);
						return getNextFireDayOneFixedYear(current);
					}
					return date;
				}else{
					current.set(Calendar.MONTH, month-1);
					current.set(Calendar.DAY_OF_MONTH, 1);
					return getNextFireDayOnFixedMonth(current);
				}
			}
			return null;
		}
		/***
		 * 已一个日期获取此时间下次的触发时间
		 * @param date
		 * @return
		 */
		protected Date getNextFireDay(Calendar result){
			int year=getNextFireYear(result);
			if(year>0){
				if(year==result.get(Calendar.YEAR)){//当前年份
					return getNextFireDayOneFixedYear(result);
				}else{//以后的年份
					result.set(Calendar.YEAR, year);
					result.set(Calendar.MONTH, 0);
					result.set(Calendar.DAY_OF_YEAR, 1);
					result.set(Calendar.HOUR, 0);
					result.set(Calendar.MINUTE, 0);
					result.set(Calendar.SECOND, 0);
					return getNextFireDayOneFixedYear(result);
				}
			}
			return null;
		}
	}

	/**
	 * 从给定的日期开始下一下符合表达式的是哪一天
	 * 从 date+1天开始匹配
	 * @param date 给定的日期
	 * @return
	 */
	public Date getNextDay(Date date) {
		Calendar ca=Calendar.getInstance();
		ca.setTime(date);
		Date result=null;
		for(Expression exp:expressions){
			if(result==null){
				result=exp.getNextFireDay(ca);
			}else{
				Date d=exp.getNextFireDay(ca);
				if(d!=null){
					if(d.before(result)){
						result=d;
					}
				}
			}
		}
		return result;
	}
	

	public Expression[] getExpressions() {
		return expressions;
	}
	
	/**
	 *	 一个配置的值 
	 * 	可以是单个值  区间  *
	 * @author 胡真山
	 *
	 */
	static class Value implements Comparable<Value>{
		/**开始值*/
		private int start;
		/**结束值*/
		private int end;
		/**是否是单个值*/
		private boolean single=true;
		/**是否是表示星期几*/
		private boolean week=false;
		/**是否表示所有*/
		private boolean all=false;
		/**是否是频次 配置时 0/100 表示100秒执行一次 */
		private boolean frequency=false;
		
		@Override
		public String toString() {
			if(all) {
				return Strings.STAR;
			}
			if(frequency) {
				return start+"/"+end;
			}
			if(single) {
				return (week?"W":"")+start;
			}else {
				return (week?"W":"")+start+"-"+(week?"W":"")+end;
			}
		}


		@Override
		public int compareTo(Value o) {
			if(all){
				return -1;
			}
			if(single){
				return start-o.start;
			}else{
				int c=start-o.start;
				if(c==0){
					return end-o.end;
				}else{
					return c;
				}
			}
		}
		
		
		
		@Override
		public boolean equals(Object obj) {
			if(this==obj){
				return true;
			}
			if(obj instanceof Value){
				Value o=(Value)obj;
				if(week==o.week){
					return valueEq(o);
				}else{
					return false;
				}
			}
			return false;
		}

		private boolean valueEq(Value o){
			if(single=o.single){
				if(single){
					return start==o.start;
				}else{
					return start==o.start&&end==o.end;
				}
			}else{
				return false;
			}
		}


		protected void parse(String value){
			if("*".equals(value)){
				this.all=true;
			}else if(value.indexOf('/')>0){
				String[] vs=StringUtils.splitc(value, '/');
				this.start=Integer.parseInt(vs[0]);
				this.end=Integer.parseInt(vs[1]);
				frequency=true;
				single=false;
			}else{
				String[] vs=StringUtils.splitc(value, '-');
				if(vs[0].startsWith("W")||vs[0].startsWith("w")){
					this.week=true;
					vs[0]=vs[0].substring(1);
				}
				this.start=Integer.parseInt(vs[0]);
				if(vs.length>1){
					if(vs[1].startsWith("W")||vs[1].startsWith("w")){
						vs[1]=vs[1].substring(1);
					}
					this.end=Integer.parseInt(vs[1]);
					single=false;
				}
			}
		}
		
		static Value valueOf(String value){
			Value v=new Value();
			v.parse(value);
			return v;
		}

		public int getStart() {
			return start;
		}

		public int getEnd() {
			return end;
		}

		public boolean isSingle() {
			return single;
		}

		public boolean isWeek() {
			return week;
		}

		public boolean isAll() {
			return all;
		}
		
		public boolean isInValue(int value){
			if(single){
				return start==value;
			}else if(frequency){
				return isInfrequency(value);
			}
			return start<=value&&value<=end;
		}
		/**
		 * 是否命中频次
		 * @param value
		 * @return
		 */
		protected boolean isInfrequency(int value){
			if(value%end==start){
				return true;
			}else{
				return false;
			}
		}
	}
}
