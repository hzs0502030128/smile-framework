package org.smile.time;

import java.text.ParseException;
import java.util.Date;

import org.smile.util.DateUtils;

import junit.framework.TestCase;

public class TestTimeExpression extends TestCase {
	public void test() {
		TimeExpression exp = new TimeExpression("[*][8-11][3,5,12][16:00-20:00][2/3600]");
		System.out.println(exp.isAllYear());
		System.out.println(exp.isValidDate(System.currentTimeMillis()));
		Date next = new Date();
		System.out.println(DateUtils.defaultFormat(next));
		System.out.println(DateUtils.defaultFormat(next));
		try {
			CronExpression cron = new CronExpression("1/3 0/2 * * * ? *");
			Date date = new Date();
			System.out.println(DateUtils.formatDate(date, "yyyy-MM-dd HH:mm:ss"));
			date = cron.getNextValidTimeAfter(new Date());
			System.out.println(DateUtils.formatDate(date, "yyyy-MM-dd HH:mm:ss"));
			date = cron.getNextValidTimeAfter(date);
			System.out.println(DateUtils.formatDate(date, "yyyy-MM-dd HH:mm:ss"));
			date = cron.getNextValidTimeAfter(date);
			System.out.println(DateUtils.formatDate(date, "yyyy-MM-dd HH:mm:ss"));
			Trigger trigger = new CronExpressionTrigger("* 0/2 11-17 * * ? *");
			if (trigger.willFireOn(System.currentTimeMillis())) {
				System.out.println("fire");
			} else {
				System.out.println("not fire");
			}
			Date d = trigger.getTimeAfter(new Date(System.currentTimeMillis()));
			if (d != null) {
				System.out.println(DateUtils.formatDate(d, "yyyy-MM-dd HH:mm:ss"));
			} else {
				System.out.println("null");
			}
			System.out.println(exp.getLeaveTime(System.currentTimeMillis()));
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void testTimeExpression() {
		FireOnExpression exp = new TimeExpression("[0/4][8-11][9-15][16:00-20:57][2/4]");
		long time = System.currentTimeMillis();
		if (exp.isValidTime(time)) {
			System.out.println("fire:" + exp.getNextValidTimeAfter(time));
		}
		System.out.println(exp.getNextValidTimeAfter(time));
	}

	public void testCronExpression() throws ParseException {
		FireOnExpression exp = new CronExpression("1/4 1-3 * * * ? *");
		long time = System.currentTimeMillis();
		if (exp.isValidTime(time)) {
			System.out.println("fire:" );
		}
		System.out.println(exp.getNextValidTimeAfter(time));
	}
}
