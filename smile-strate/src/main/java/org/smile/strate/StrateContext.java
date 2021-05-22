package org.smile.strate;

import javax.servlet.ServletContext;

import org.smile.log.LoggerHandler;
import org.smile.strate.action.ActionContext;
import org.smile.strate.form.StrateConverter;

public class StrateContext implements LoggerHandler{
	
	private static StrateContext instance=new StrateContext();
	
	private boolean running=false;
	
	private StrateContext() {}
	
	public static StrateContext getInstance() {
		return instance;
	}
	/**
	 * 	开启strate初始化
	 * @param context
	 * @throws StrateException
	 */
	public synchronized void start(ServletContext context) throws StrateException {
		if(running) {
			return ;
		}
		ActionContext.init(context);
		StrateConverter.initGlobaConvertConfig();
		logger.info("strate 初始化完成");
		this.running=true;
	}
	
	
	public ServletContext getServletContext() {
		return ActionContext.getServletContext();
	}
}
