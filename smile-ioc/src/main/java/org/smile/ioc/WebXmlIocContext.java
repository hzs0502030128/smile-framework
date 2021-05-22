package org.smile.ioc;


public class WebXmlIocContext extends ClassPathIocContext{
	
	private static WebXmlIocContext instance=new WebXmlIocContext();
	
	public static WebXmlIocContext getInstance(){
		return instance;
	}
}
