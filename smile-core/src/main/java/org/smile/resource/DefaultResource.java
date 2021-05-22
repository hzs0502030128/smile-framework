package org.smile.resource;
/**
 * 这是一个默认的资源文件加载
 * 会加载 classpath 下的  MessageResource 国际化资源文件
 * @author 胡真山
 * @Date 2016年1月20日
 */
public class DefaultResource extends MessageResource{
	
	private static DefaultResource instance=new DefaultResource();
	
	public static DefaultResource getInstance(){
		return instance;
	}
}
