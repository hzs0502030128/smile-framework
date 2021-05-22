package org.smile.strate.handler;

import javax.servlet.ServletContext;

import org.smile.strate.action.Action;
/**
 * 直接从类名实例化 
 * @author strive
 *
 */
public class StrateBeanHandler implements BeanHandler {

	public Action getActionBean(String name,ServletContext context) throws StrateBeanHandlerException {
		try {
			return (Action)Class.forName(name).newInstance();
		} catch (Exception e) {
			throw new StrateBeanHandlerException("实例化Action出错 class:"+name,e);
		}
	}

}
