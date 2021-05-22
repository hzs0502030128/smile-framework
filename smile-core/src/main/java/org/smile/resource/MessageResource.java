package org.smile.resource;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;
import java.util.concurrent.ConcurrentHashMap;

import org.smile.Smile;
import org.smile.commons.SmileRunException;
import org.smile.commons.Strings;
import org.smile.log.LoggerHandler;
/***
 * 资源管理器
 * @author 胡真山
 *
 */
public class MessageResource implements LoggerHandler{
	
	protected static Locale ROOT_LOCALE;
	
	static{
		//从配置文件读取默认
		try {
			ROOT_LOCALE = Smile.config.getValue(Smile.I18N_DEFAULT_LOCAL_KEY, Locale.class);
		} catch (SmileRunException e) {
			logger.warn("Invalid recource local config in "+Smile.I18N_DEFAULT_LOCAL_KEY, e);
		}
		if(ROOT_LOCALE==null){
			ROOT_LOCALE=Locale.ROOT;
		}
	}
	/***
	 * 资源文件文件名
	 */
	protected String resourceName=Smile.MESSAGE_RESOURCES_NAME;
	/***
	 * 默认的资源文件
	 */
	private ResourceBundle defaultBundle;
	/**
	 * 缓存所有的资源文件
	 */
	private Map<Locale,ResourceBundle> bundleCach=new ConcurrentHashMap<Locale,ResourceBundle>();
	/**
	 * 默认方言
	 */
	private Locale defaultLocale=ROOT_LOCALE;
	
	public MessageResource(String resourceName){
		this.resourceName=resourceName;
		initDefault();
	}
	
	public MessageResource(String resourceName,Locale defaultLocal){
		this.resourceName=resourceName;
		this.defaultLocale=defaultLocal;
		initDefault();
	}
	
	public MessageResource(){
		initDefault();
	}
	/**
	 * 初始化默认设置 
	 */
	private void initDefault(){
		ResourceBundle bundle=getResourceBundle(defaultLocale);
		//系统默认方言
		ResourceBundle defaultLoad=getResourceBundle(Locale.getDefault());
		if(defaultLoad!=null){
			bundleCach.put(Locale.getDefault(), defaultLoad);
		}
		if(bundle!=null){
			defaultBundle=bundle;
		}else{
			defaultBundle=defaultLoad;
		}
	}
	
	/**如果不存在以key为默认返回*/
	public String getStringKeyDefault(Locale locale,String key){
		return getString(locale, key, true);
	}
	
	/**如果不存在以key为默认返回*/
	public String getString(Locale locale,String key){
		return getString(locale, key, false);
	}
	
	public String getString(Locale locale,String key,boolean keyDefault){
		ResourceBundle bundle=getResourceBundle(locale);
		try{
			if(bundle!=null){
				return bundle.getString(key);
			}
			//还没有使用默认的
			if(defaultBundle!=null){
				return defaultBundle.getString(key);
			}
		}catch(MissingResourceException e){
			if(keyDefault){
				return key;
			}
		}
		return null;
	}
	
	public String getDefaultString(String key){
		return defaultBundle.getString(key);
	}
	/**
	 * 默认的资源
	 * @param locale
	 * @return
	 */
	public ResourceBundle  getResourceBundle(Locale locale){
		ResourceBundle bundle=bundleCach.get(locale);
		if(bundle==null){
			final Locale realyLocal=locale;
			try{
				bundle=ResourceBundle.getBundle(resourceName, locale,new Control(){
					@Override
					public List<Locale> getCandidateLocales(String baseName, Locale local) {
						List<Locale>  list=super.getCandidateLocales(baseName, local);
						List<Locale> result=new LinkedList<Locale>();
						for(Locale l:list){
							if(Strings.BLANK.equals(l.getLanguage())){
								result.add(defaultLocale);
							}else if(l.getLanguage().equals(realyLocal.getLanguage())){
								result.add(l);
							}
						}
						return result;
					}
					@Override
					public List<String> getFormats(String baseName) {
						return Control.FORMAT_PROPERTIES;
					}
				});
				bundleCach.put(locale, bundle);
			}catch(MissingResourceException e){
				logger.info(e.getMessage());
			}
		}
		return bundle;
	}
	
	public static Locale getRootLocale(){
		return ROOT_LOCALE;
	}
	
}
