package org.smile.time;

import java.util.Date;

public abstract class AbstractTrigger implements Trigger{
	/**
	 * 触发的时间
	 */
	protected long fireTime;
	/**
	 * 下一次触发的时间
	 */
	protected Date nextFireTime;
	/***
	 * 上次触发时间
	 */
	protected Date previousFireTime = null;
	/**
	 * 是否停止
	 */
	protected boolean pause=false;
	
	@Override
	public long getFireTime() {
		return fireTime;
	}
	
	@Override
	public Date getNextFireTime() {
		return nextFireTime;
	}
	
	@Override
	public void pause(){
		this.pause=true;
	}

	@Override
	public void working() {
		this.pause=false;
	}
	
	@Override
	public Date getPreviousFireTime() {
		return this.previousFireTime;
	}
	
	public void setNextFireTime(Date nextFireTime) {
		this.nextFireTime = nextFireTime;
	}
	
	public void setPreviousFireTime(Date previousFireTime) {
		this.previousFireTime = previousFireTime;
	}

	
}
