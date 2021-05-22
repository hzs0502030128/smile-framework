package org.smile.time;

import java.util.Date;

public class TimeExpressionTrigger extends AbstractTrigger{

	protected FireOnExpression expression;
	
	public TimeExpressionTrigger(String expression){
		this.expression=new TimeExpression(expression);
	}
	
	public TimeExpressionTrigger(FireOnExpression expression){
		this.expression=expression;
	}
	
	@Override
	public boolean willFireOn(long times) {
		if(fireTime>0){
			if(times/1000==fireTime/1000){
				return false;
			}
			if(this.expression.isValidTime(times)){
				this.previousFireTime=new Date(fireTime);
				fireTime=times;
				this.nextFireTime=expression.getNextValidTimeAfter(times);
				return true;
			}
		}else{
			if(this.expression.isValidTime(times)){
				fireTime=times;
				this.nextFireTime=expression.getNextValidTimeAfter(times);
				return true;
			}
		}
		return false;
	}

	@Override
	public Date getTimeAfter(Date afterTime) {
		return expression.getNextValidTimeAfter(afterTime.getTime());
	}
}
