package org.smile.strate.form;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.smile.beans.converter.TypeConverter;
import org.smile.commons.ExceptionUtils;
import org.smile.log.LoggerHandler;
/**
 * 封闭一个类的自定义转换器配置
 * @author 胡真山
 * @Date 2016年1月26日
 */
public class ActionConvertConfig implements LoggerHandler{
	/**
	 * 当前action的类型
	 */
	private Class actionClass;
	/**
	 * 转换器类型注册
	 */
	private Map<Class,TypeConverter> typeConverters;
	
	public ActionConvertConfig(Class actionClass){
		this.actionClass=actionClass;
	}
	
	public void initConverters(Properties p){
		typeConverters=new HashMap<Class, TypeConverter>(p.size());
		for(Map.Entry<Object, Object> entry:p.entrySet()){
			try{
				Class clazz=Class.forName((String)entry.getKey());
				TypeConverter type=(TypeConverter)(Class.forName((String)entry.getValue()).newInstance());
				typeConverters.put(clazz,type);
			}catch(Exception e){
				logger.error(ExceptionUtils.getExceptionMsg(e));
			}
		}
	}
	
	public TypeConverter getTypeConverter(Class clazz){
		return typeConverters.get(clazz);
	}

	public Class getActionClass() {
		return actionClass;
	}
	
	
}
