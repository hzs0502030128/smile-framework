package org.smile.strate.handler;

import javax.servlet.ServletContext;

import org.smile.beans.converter.BeanException;
import org.smile.ioc.WebXmlIocContext;
import org.smile.strate.action.Action;
/***
 * 从ioc容器中获取action实例 
 * @author 胡真山
 */
public class IocBeanHandler extends StrateBeanHandler {

	@Override
	public Action getActionBean(String name, ServletContext context) throws StrateBeanHandlerException {
		try {
			return (Action)WebXmlIocContext.getInstance().getBean(name);
		} catch (BeanException e) {
			if(name.indexOf(".")>0){
				return super.getActionBean(name, context);
			}
			throw new StrateBeanHandlerException("get bean named "+name+" in spring context",e);
		}
	}

}
