package org.smile.strate.form;

import org.smile.beans.MapBean;

/***
 * 表单数据填充结果
 * @author 胡真山
 *
 */
public class ActionFormResult {
	/**动态action参数提交*/
	private String dynamicAction;
	/**提交表单封装到方法对象*/
	private MapBean<ActionParamBeanClass> methodParamBean;
	
	
	public String getDynamicAction() {
		return dynamicAction;
	}

	public void setDynamicAction(String dynamicAction) {
		this.dynamicAction = dynamicAction;
	}
	/**
	 * 方法封装参数对象
	 * @return
	 */
	public MapBean<ActionParamBeanClass> getMethodParamBean() {
		return methodParamBean;
	}
	/**
	 * 是否存在方法参数封装对象
	 * @return
	 */
	public boolean hasMethodParamBean(){
		return methodParamBean!=null;
	}
	
	public void setMethodParamBean(MapBean<ActionParamBeanClass> bean) {
		this.methodParamBean = bean;
	}
	/**
	 * 是否存在动态action
	 * @return
	 */
	public boolean hasDynamicAction(){
		return dynamicAction!=null;
	}
	
}
