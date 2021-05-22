package org.smile.strate.form;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.smile.beans.converter.BasicConverter;
import org.smile.beans.converter.ConvertException;
import org.smile.beans.converter.Converter;
import org.smile.beans.converter.TypeConverter;
import org.smile.commons.ExceptionUtils;
import org.smile.log.LoggerHandler;
import org.smile.reflect.Generic;
import org.smile.strate.Strate;
import org.smile.strate.StrateException;
import org.smile.strate.upload.UploadFileConverter;
import org.smile.strate.upload.UploadInputSteamConverter;
import org.smile.util.ClassPathUtils;
import org.smile.util.Properties;
/**
 *  strate 数据类型转换器
 * @author 胡真山
 * @Date 2016年1月26日
 */
public class StrateConverter implements Converter,LoggerHandler{
	/**全局转换器*/
	private static final BasicConverter GLOBA_CONVERT =  new BasicConverter();

	/**action 配置 自定义转换器*/
	private static Map<Class, ActionConvertConfig> actionConverts = new HashMap<Class, ActionConvertConfig>();

	private static ConcurrentHashMap<Class,StrateConverter> CLASS_CONVERTER=new ConcurrentHashMap<Class,StrateConverter>();
	static{
		//注册默认转换器
		GLOBA_CONVERT.regsiterTypeConverter(new UploadFileConverter());
		GLOBA_CONVERT.regsiterTypeConverter(new UploadInputSteamConverter());
	}
	
	private Class actionClass;
	/**
	 * 构造函数
	 * @param action
	 */
	private StrateConverter(Class action){
		this.actionClass=action;
	}
	/**
	 * 获取转换器的实例
	 * @param actionClass
	 * @return
	 */
	public static final StrateConverter getInstance(Class actionClass){
		StrateConverter convertor=CLASS_CONVERTER.get(actionClass);
		if(convertor!=null){
			return convertor;
		}
		convertor=new StrateConverter(actionClass);
		StrateConverter oldconvertor=CLASS_CONVERTER.putIfAbsent(actionClass, convertor);
		return oldconvertor==null?convertor:oldconvertor;
	}
	/**
	 * 一个action的一种类型的转换器
	 * @param action
	 * @param type
	 * @return
	 */
	public TypeConverter getActionTypeConvert(Class type) {
		ActionConvertConfig config = actionConverts.get(actionClass);
		if (config != null) {
			return config.getTypeConverter(type);
		}
		return null;
	}
	
	public <T> TypeConverter<T> getTypeConverter(Class<T> type) {
		TypeConverter typeConverter=getActionTypeConvert(type);
		if(typeConverter!=null){
			return typeConverter;
		}
		return GLOBA_CONVERT.getTypeConverter(type);
	}
	/**
	 * 初始化全局的转换器配置
	 * @throws StrateException
	 */
	public static void initGlobaConvertConfig() throws StrateException{
		Properties converterProperties;
		try {
			converterProperties = loadGlobaProperties();
		} catch (IOException e1) {
			throw new StrateException(e1);
		}
		if(converterProperties.size()>0){
			for (Map.Entry<Object, Object> entry : converterProperties.entrySet()) {
				try {
					Class clazz = Class.forName((String) entry.getKey());
					TypeConverter typeConverter = (TypeConverter) (Class.forName((String) entry.getValue()).newInstance());
					GLOBA_CONVERT.regsiterTypeConverter(typeConverter);
				} catch (Exception e) {
					logger.error(ExceptionUtils.getExceptionMsg(e));
				}
			}
			logger.info("load globa conerter config "+Strate.globaconversion+" file success ");
		}
	}
	
	public static void addActionConvertConfig(ActionConvertConfig config){
		actionConverts.put(config.getActionClass(), config);
	}
	/**
	 * 配置的转换器初始化
	 * @param clazz
	 * @throws StrateException
	 */
	public static void initActionConvertConfig(Class clazz) throws StrateException{
		try {
			//初始化转换器配置
			Properties p = loadProperties(clazz);
			if (p != null) {
				ActionConvertConfig config = new ActionConvertConfig(clazz);
				config.initConverters(p);
				addActionConvertConfig(config);
			}
		} catch (Exception e) {
			throw new StrateException("load convert properties file error action " + clazz, e);
		}
	}

	@Override
	public <T> T convert(Class<T> type, Generic generic, Object value) throws ConvertException {
		TypeConverter<T> tc=getTypeConverter(type);
		if(tc!=null){
			return tc.convert(generic, value);
		}
		return (T)GLOBA_CONVERT.convert(type, generic, value);
	}

	@Override
	public <T> T convert(Class<T> type, Object value) throws ConvertException {
		return convert(type, null, value);
	}
	
	/**
	 * action convert 文件
	 * @throws IOException 
	 * */
	public static Properties loadProperties(Class<?> clazz) throws IOException{
		String clazzname=clazz.getSimpleName();
		String xmlPath=ClassPathUtils.getPackageDir(clazz)+clazzname+Strate.conversion;
		InputStream is=clazz.getClassLoader().getResourceAsStream(xmlPath);
		if(is!=null){
			Properties p=new Properties();
			p.load(is);
			return p;
		}
		return null;
	}
	/**
	 * 加载全局转换器配置文件
	 * @return
	 * @throws IOException
	 */
	public static Properties loadGlobaProperties() throws IOException{
		Properties p=new Properties();
		p.loadClassPathFile(Strate.globaconversion, false);
		return p;
	}
}
