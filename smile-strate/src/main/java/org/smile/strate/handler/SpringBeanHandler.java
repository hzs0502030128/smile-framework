package org.smile.strate.handler;

import javax.servlet.ServletContext;

import org.smile.strate.action.Action;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
/**
 * 从spring容器中实例化一个action
 * @author strive
 *
 */
public class SpringBeanHandler extends StrateBeanHandler {
	public Action getActionBean(String name,ServletContext context) throws StrateBeanHandlerException {
		WebApplicationContext webappCtx = WebApplicationContextUtils.getWebApplicationContext(context);
		Action action;
		if(name.indexOf(".")>0){
			try {
				action=(Action)webappCtx.getBean(Class.forName(name));
			}catch (Exception e) {
				throw new StrateBeanHandlerException("创建action实例失败",e);
			}
		}else{
			action=(Action)webappCtx.getBean(name);
		}
		if(action==null){
			action=super.getActionBean(name, context);
		}
		return action;
	}

}
