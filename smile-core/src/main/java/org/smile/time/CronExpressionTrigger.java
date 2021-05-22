package org.smile.time;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class CronExpressionTrigger extends AbstractTrigger {

	/**
	 * 表达式对象
	 */
	protected CronExpression expression;
	
	/**
	 * 触发开始时间
	 * */
	protected Date startTime = null;
	/**
	 * 触发结果时间
	 */
	protected Date endTime = null;

	public CronExpressionTrigger(String expression) throws ParseException {
		this.expression = new CronExpression(expression);
	}
	
	public CronExpressionTrigger(CronExpression expression){
		this.expression=expression;
	}

	@Override
	public boolean willFireOn(long times) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(times);
		c.set(Calendar.MILLISECOND, 0);
		Date testTime = c.getTime();

		Date fta = getFireTimeAfter(new Date(times - 1000L));

		while (fta.before(testTime)) {
			fta = getFireTimeAfter(fta);
		}
		c.setTime(fta);
		c.set(Calendar.MILLISECOND, 0);
		
		if (c.getTime().equals(testTime)) {
			this.previousFireTime=new Date(this.fireTime);
			this.fireTime=times;
			return true;
		}
		return false;
	}

	@Override
	public Date getTimeAfter(Date afterTime) {
		return expression.getTimeAfter(afterTime);
	}
	
	/**
	 * 开始时间
	 * @return
	 */
	public Date getStartTime() {
		return this.startTime;
	}
	
	/**
	 * 设置开始时间
	 * @param startTime
	 */
	public void setStartTime(Date startTime) {
		if (startTime == null) {
			throw new IllegalArgumentException("Start time cannot be null");
		}

		Date eTime = getEndTime();
		if ((eTime != null) && (startTime != null) && (eTime.before(startTime))) {
			throw new IllegalArgumentException("End time cannot be before start time");
		}

		java.util.Calendar cl = java.util.Calendar.getInstance();
		cl.setTime(startTime);
		cl.set(Calendar.MILLISECOND, 0);

		this.startTime = cl.getTime();
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		Date sTime = getStartTime();
		if ((sTime != null) && (endTime != null) && (sTime.after(endTime))) {
			throw new IllegalArgumentException("End time cannot be before start time");
		}
		this.endTime = endTime;
	}
	
	public Date getFireTimeAfter(Date afterTime) {
		if (afterTime == null) {
			afterTime = new Date();
		}

		if (getStartTime()!=null&&getStartTime().after(afterTime)) {
			afterTime = new Date(getStartTime().getTime() - 1000L);
		}

		if ((getEndTime() != null) && (afterTime.compareTo(getEndTime()) >= 0)) {
			return null;
		}

		Date pot = getTimeAfter(afterTime);
		if ((getEndTime() != null) && (pot != null) && (pot.after(getEndTime()))) {
			return null;
		}

		return pot;
	}

}
