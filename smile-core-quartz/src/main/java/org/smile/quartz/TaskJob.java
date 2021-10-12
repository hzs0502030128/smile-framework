package org.smile.quartz;

import org.quartz.Job;
/**
 * 用于配置xml中的定时任务加载
 * 定时任务的实现类需要继承此接口
 * @author 胡真山
 *
 */
public interface TaskJob extends Job {
	/**
	 * 获取运行表达式
	 * @return
	 */
	public String getExpression();
	/**
	 * 运行表达式
	 * @param expression
	 */
	public void setExpression(String expression);

}
