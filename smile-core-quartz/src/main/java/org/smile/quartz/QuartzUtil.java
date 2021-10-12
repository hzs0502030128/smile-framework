package org.smile.quartz;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.smile.beans.converter.BeanException;
import org.smile.config.BeanConfig;
import org.smile.config.BeanCreator;
import org.smile.config.BeansConfig;
import org.smile.log.LoggerHandler;
import org.smile.util.XmlUtils;

public class QuartzUtil implements LoggerHandler{
	
	private Scheduler sched = null;
	
	public QuartzUtil(){
		 sched =getDefaultScheduler();
	}
	/**
	 * 获得scheduler
	 * 
	 * @return Scheduler
	 * @throws
	 */
	private  Scheduler getDefaultScheduler() {
		StdSchedulerFactory schefactory = new StdSchedulerFactory();
		Scheduler schedul = null;
		try {
			schedul = schefactory.getScheduler();
			if (schedul.isShutdown()) {
				schedul.start(); // 若关闭,启动
			}
		} catch (SchedulerException e) {
			logger.error(e);
		}
		return schedul;
	}
	/**
	 * 关闭定时器容器
	 */
	public void shutdown() {
		try {
			if (sched != null && !this.sched.isShutdown()) {
				sched.shutdown();
			}
			sched = null;
		} catch (Exception e) {
			logger.debug("Quartz Scheduler failed to shutdown cleanly: " + e.toString());
			logger.error(e.getMessage(), e);
			this.sched = null;
		}
		logger.debug("Quartz Scheduler successful shutdown.");
	}
	/**
	 * 创建定时任务
	 * 
	 * @param jobName
	 *            任务名称
	 * @param expression
	 *            任务执行时间表达式
	 * @param classs
	 *            调用job类
	 */
	public JobDetail createJob(String jobName, String expression, Class classs) {
		// 任务名称，任务组名称，任务实现类
		JobDetail job = new JobDetail(jobName, Scheduler.DEFAULT_GROUP, classs);
		try {
			// 删除作业
			if (sched.getJobDetail(jobName, Scheduler.DEFAULT_GROUP) != null) {
				sched.deleteJob(jobName, Scheduler.DEFAULT_GROUP);
			}
			CronTrigger ctrigger = new CronTrigger(jobName + "Trigger", null);
			CronExpression cronExpression = new CronExpression(expression);
			
			ctrigger.setCronExpression(cronExpression);
			// 注册作业
			sched.scheduleJob(job, ctrigger);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return job;
	}
	/**
	 * 启动
	 * @throws SchedulerException
	 */
	public void start() throws SchedulerException{
		if (!sched.isShutdown()) {
			sched.start();
		}
	}
	
	/**
	 * 启动任务从一个配置文件中
	 * @param is
	 * @throws BeanException
	 * @throws SchedulerException
	 */
	public List<TaskJob> startFromXml(InputStream is) throws BeanException, SchedulerException {
		BeansConfig beans=XmlUtils.parserXml(BeansConfig.class, is);
		List<TaskJob> runinfos=new LinkedList<TaskJob>();
		if(null != beans && beans.getBean() != null){
			for(BeanConfig b:beans.getBean()){
				TaskJob task=(TaskJob)new BeanCreator(b).getBean();
				createJob(task.getClass().getName(),task.getExpression(), task.getClass());
				runinfos.add(task);
			}
			start();
		}
		return runinfos;
	}
	
	
	private static final QuartzUtil instance=new QuartzUtil();
	
	public static QuartzUtil getInstance(){
		return instance;
	}
	
}