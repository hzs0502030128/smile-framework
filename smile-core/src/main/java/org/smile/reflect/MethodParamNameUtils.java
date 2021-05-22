package org.smile.reflect;

import java.lang.reflect.Method;

import org.smile.Smile;
import org.smile.collection.ArrayUtils;
import org.smile.commons.ExceptionUtils;
import org.smile.log.LoggerHandler;
import org.smile.reflect.reader.AsmParamNameReader;
import org.smile.reflect.reader.JavassistParamNameReader;
import org.smile.reflect.reader.ParamNameReader;
import org.smile.reflect.reader.SimpleParamNameReader;

public class MethodParamNameUtils implements LoggerHandler{
	
	private static ParamNameReader reader;
	/**默认*/
	private static ParamNameReader defaultReader=new SimpleParamNameReader();
	
	static{
		//读取配置初始货
		Class readerClass = Smile.config.getValue(Smile.PARAM_NAME_READER_KEY, Class.class);
		if(readerClass!=null){
			reader=tryOneHandler(readerClass);//配置的时候把默认设置为配置的,reader设置为原默认
			if(reader!=null){//配置了进行交换
				ParamNameReader temp=defaultReader;
				defaultReader=reader;
				reader=temp;
			}
		}else{
			//如没有配置自动尝试
			reader=tryOneHandler(AsmParamNameReader.class);
			if(reader==null){
				reader=tryOneHandler(JavassistParamNameReader.class);
			}
		}
	}
	
	public static String[] getParamNames(Method method){
		Class[] params=method.getParameterTypes();
		if(ArrayUtils.notEmpty(params)){
			String[] names=null;
			try{
				names=defaultReader.getParameterNames(method);
			}catch(Exception e){
				logger.warn(ExceptionUtils.getExceptionMsg(e));
			}
			if(names==null&&reader!=null){
				names= reader.getParameterNames(method);
			}
			if(ArrayUtils.notEmpty(names)) {
				return names;
			}
		}
		return new String[]{};
	}
	
	private static ParamNameReader tryOneHandler(Class<? extends ParamNameReader> handlerClass){
		try {
			ParamNameReader reader=handlerClass.newInstance();
			logger.debug("使用"+handlerClass+"字节码工具读取方法参数名");
			return reader;
		} catch (Throwable e) {
			logger.info(handlerClass+"适配失败"+ExceptionUtils.getExceptionMsg(e));
		}
		return null;
	}
}
