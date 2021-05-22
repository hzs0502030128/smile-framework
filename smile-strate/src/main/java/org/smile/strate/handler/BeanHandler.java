package org.smile.strate.handler;

import javax.servlet.ServletContext;

import org.smile.strate.action.Action;
/**
 * Action实例化处理类
 * @author strive
 *
 */
public interface BeanHandler {
	/**
	 * 返回一个action的实例 
	 * @param name action的名称
	 * @param context servlet容器
	 * @return 一个action的实例
	 * @throws StrateBeanHandlerException 实例化失败
	 */
	public Action getActionBean(String name, ServletContext context) throws StrateBeanHandlerException;
}
