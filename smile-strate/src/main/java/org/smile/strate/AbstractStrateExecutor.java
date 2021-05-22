package org.smile.strate;

import javax.servlet.ServletContext;

import org.smile.log.LoggerHandler;
import org.smile.strate.action.Action;
import org.smile.strate.handler.BeanHandler;
import org.smile.strate.handler.StrateBeanHandlerException;

public abstract class AbstractStrateExecutor implements StrateExecutor,LoggerHandler{
	/**
	 * action实例化的处理类
	 */
	protected BeanHandler actionBeanHandler;
	
	/***
	 * 获取action实例
	 * @param actionElement
	 * @param request
	 * @return
	 * @throws StrateBeanHandlerException
	 */
	@Override
	public Action getActionBean(String actionClass,ServletContext context) throws StrateBeanHandlerException{
		return this.actionBeanHandler.getActionBean(actionClass, context);
	}
	

	@Override
	public BeanHandler getActionBeanHandler() {
		return actionBeanHandler;
	}

	@Override
	public void setActionBeanHandler(BeanHandler beanHandler) {
		this.actionBeanHandler=beanHandler;
	}
	
}
