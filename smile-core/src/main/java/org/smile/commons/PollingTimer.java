package org.smile.commons;
/**
 * 	轮询计时器
 * @author 胡真山
 * 2015年10月23日
 */
public class PollingTimer {
	
   private long startTime;
   
   private long interval;
   
   private boolean isfirst=true;
   
   public PollingTimer(){
   }
   
   public PollingTimer(long dur){
	   start(dur);
   }
   
   public void changeInerval(long dur){
	   this.interval=dur;
   }
   
   public void start(long dur){
	   this.interval=dur;
	   startTime=System.currentTimeMillis();
	   isfirst=true;
   }
   
   public void reStart(){
	   startTime=System.currentTimeMillis();
	   isfirst=true;
   }
   
   /**
    * 中间间隔多个段的时候，只算一个
    * @param now
    * @return
    */
   public boolean isOk(long now){
	   if(now-startTime>=interval){
		   this.startTime=now;
		   return true;
	   }
	   return false;
   }
   /**
    * 第一次到期
    * @param now
    * @return
    */
   public boolean isFirstOk(long now){
	   if(isfirst&&now-startTime>=interval){
		   isfirst=false;
		   return true;
	   }
	   return false;
   }
   
   /**距下次执行的时间**/
   public long getDistanceNextTime(){
	   return interval-(System.currentTimeMillis()-startTime);
   }
   /**
    * 是否到了周期
    * 一次只增加一个周期
    * @param now
    * @return
    */
   public boolean isIntervalOk(long now){
	   if(now-startTime>=interval){
		   startTime+=interval;
		   return true;
	   }
	   return false;
   }
   /**
    * 周期间隔
    * @return
    */
   public long getInterval() {
		return interval;
   }
}
