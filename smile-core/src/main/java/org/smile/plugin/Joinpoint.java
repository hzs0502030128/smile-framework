package org.smile.plugin;

public interface Joinpoint {
	/***
	 * 执行动作
	 * @return
	 * @throws Exception
	 */
	public <T> T proceed() throws Throwable ;
	
	/**
	 * 执行此动作的对象
	 * @return
	 */
	public <T> T getTarget();
}
