package org.smile.validate;

import org.smile.Smile;
import org.smile.log.LoggerHandler;
import org.smile.resource.MessageResource;

/***
 * 数据验证常量配置
 * 
 * 可以在smile.properties中配置
 * 
 * validate.single  是否是对字段单个验证不通过返回
 * validate.message.file  可选   验证提示信息国际化文件名称  默认为 validate
 * validate.message.textKey 可选  验证文件中配置验证提示文件的key的前缀   默认为 validate.text.
 * 
 * 
 * @author 胡真山
 * @Date 2016年1月28日
 */
public class ValidateConstants implements LoggerHandler{
	/**内置验证提示语*/
	public static ValidateText text;
	/**验证信息提示语国际化资源文件key的前缀*/
	public static String validateTextKey;
	/**
	 * 是否是单个验证不通过返回
	 */
	public static boolean single=true;
	/**
	 * 提示信息资源文件 
	 */
	public static MessageResource message;
	
	/**
	 * 初始化validate常量配置信息
	 */
	static{
		try {
			//从smile.properties中读取是否单个验证信息
			single=Smile.config.getBoolean("validate.single",true);
			//验证提示信息文件
			String validateResourceName=Smile.config.getProperty("validate.message.file", "validate");
			message=new MessageResource(validateResourceName);
			//验证提示信息字段国际化前缀
			validateTextKey=Smile.config.getProperty("validate.message.textKey", "validate.text.");
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
