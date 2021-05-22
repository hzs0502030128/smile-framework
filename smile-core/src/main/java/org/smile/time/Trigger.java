package org.smile.time;

import java.util.Date;


public interface Trigger {
	/***
	 * 是否触发
	 * @param c
	 * @return
	 */
	public boolean willFireOn(long times);
	/**
	 * 上次触发时间
	 * @return
	 */
	public Date getPreviousFireTime();
	/**
	 * 本次触发时间
	 * @return
	 */
	public long getFireTime();
	/**
	 * 下次触发时间
	 * @return
	 */
	public Date getNextFireTime();
	/**
	 * 停止
	 */
	public void pause();
	
	/***
	 * 工作
	 */
	public void working();
	/**
	 * 获取之后的可触发的时间点
	 * @param afterTime
	 * @return
	 */
	public  Date getTimeAfter(Date afterTime);
	
}
