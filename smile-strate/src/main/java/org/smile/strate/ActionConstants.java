package org.smile.strate;


/**
 * action 信息
 * @author 胡真山
 * @Date 2016年1月17日
 */
public class ActionConstants {
	/**action URL 扩展名*/
	public static String extension="do";
	/**是否支持动态方法调用*/
	public static boolean allowDynamicMethod=false;
	/**动态方法参数格式*/
	public static String dynamicMethodFlag="action:";
	/**用于设置错误信息的变量名*/
	public static String errorkey="error";
	/**用于设置提示信息的变量名*/
	public static String messagekey="message";
	/**在action中保存json返回值的属性*/
	public static String jsonResultProperty = "json";
	/**在action中保存image返回值的属性*/
	public static String imageResultProperty = "image";
	/**在action中保存chart返回值的属性*/
	public static String chartResultProperty = "chart";
	/**前端传送方法参数名称,支持从request获取方法名*/
	public static String actionMethodParam="method";
	
}
