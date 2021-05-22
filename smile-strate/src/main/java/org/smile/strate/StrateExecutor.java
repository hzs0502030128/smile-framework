package org.smile.strate;

import java.lang.reflect.Method;

import javax.servlet.ServletContext;

import org.smile.beans.converter.BeanException;
import org.smile.strate.action.Action;
import org.smile.strate.action.ClassElement;
import org.smile.strate.dispatch.DispatchContext;
import org.smile.strate.form.ActionFormResult;
import org.smile.strate.handler.BeanHandler;
import org.smile.strate.handler.StrateBeanHandlerException;

public interface StrateExecutor {
	/**
	 * 获取action实例获取者
	 * @return
	 */
	public BeanHandler getActionBeanHandler();
	/**
	 * 设置action实例获取者
	 * @param beanHandler
	 */
	public void setActionBeanHandler(BeanHandler beanHandler);
	/***
	 * 初始化
	 */
	public void initOnClassElement(ClassElement ce);
	/***
	 * request 中的参数设置到action属性中
	 * 并检查是否需要切换action方法如需要
	 * 反加action方法名
	 * @throws BeanException 
	 */
	public ActionFormResult requestToAction(DispatchContext context) throws Exception;
	/**
	 * 处理一个action
	 * @param action
	 * @param actionElement
	 * @return
	 * @throws StrateException
	 */
	public Object doActionMethod(DispatchContext context, ActionFormResult fillResult) throws StrateException;
	/**
	 * 处理转换的了方法
	 * @param action
	 * @param actionElement
	 * @return
	 * @throws StrateException
	 */
	public  Object doOtherActionMethod(DispatchContext context) throws StrateException ;
	
	/**
	 * 获取action方法 
	 * @param actionClass
	 * @param method
	 * @return
	 */
	public Method getActionMethod(Class actionClass, String method);
	/**
	 *	 获取action的实例
	 * @param actionClass action类的全名
	 * @param context
	 * @return
	 * @throws StrateBeanHandlerException
	 */
	public Action getActionBean(String actionClass, ServletContext context) throws StrateBeanHandlerException;
}
