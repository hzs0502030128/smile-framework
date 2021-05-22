package org.smile.orm.dao;


public interface DaoTargetHandler {
	/**获取target对象*/
	public Object getTarget(String targetClass,Executor executor);
}
