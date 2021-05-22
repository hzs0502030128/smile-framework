package org.smile.log.impl;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.smile.Smile;
import org.smile.collection.CollectionUtils;
import org.smile.log.Logger.Level;
import org.smile.util.Properties;
import org.smile.util.RegExp;
import org.smile.util.StringUtils;
import org.smile.util.SysUtils;

public abstract class AbstractSmileLogConfig {
	
	protected static final String configFileName=Smile.LOG_CONFIG_FILE;
	
	protected static final String CONFIG_NAME="logger";
	
	protected static final String LEVEL_NAME="level";
	/**
	 * 用于封闭配置文件中的配置信息
	 */
	protected Map<String,LogConfig> loggers=new LinkedHashMap<String,LogConfig>();
	
	protected Level level=Level.INFO;
	/**
	 * 包日志级别控制
	 */
	protected Map<RegExp,PackageSet> packageSet=new TreeMap<RegExp,PackageSet>(new Comparator<RegExp>() {
		@Override
		public int compare(RegExp o1, RegExp o2) {
			return o2.getPattern().compareTo(o1.getPattern());
		}
	});
	
	public PackageSet getPackageSet(String name){
		for(Map.Entry<RegExp,PackageSet> entry:packageSet.entrySet()){
			if(entry.getKey().test(name)){
				return entry.getValue();
			}
		}
		return null;
	}
	/**
	 * 先尝试加载 properties文件
	 * 如果不存在 加载 xml文件 
	 */
	public void loadConfig(){
		Properties pros=Properties.instanceFromClassPath(configFileName, CollectionUtils.linkedHashSet("properties","xml"));
		if(pros!=null){
			try {
				SysUtils.log("日志配置信息:"+pros);
				for(Map.Entry<Object, Object> entry:pros.entrySet()){
					if(entry.getValue() instanceof String){
						String strValue=entry.getValue().toString();
						if(!entry.getKey().toString().startsWith(CONFIG_NAME)&&!LEVEL_NAME.equals(entry.getKey())){
							PackageSet ps=new PackageSet(strValue);
							//如果是包结构设置包级别
							packageSet.put(new RegExp(StringUtils.trim(entry.getKey().toString())), ps);
						}
					}
				}
				pros.convertTo(this);
				//初始化handler
				for(Map.Entry<String,LogConfig> entry:loggers.entrySet()){
					LogConfig config=entry.getValue();
					String name=entry.getKey();
					try{
						initOneConfig(name, config);
					}catch(Throwable e){
						SysUtils.log("加载配置文件"+configFileName+"异常",e);
					}
				}
			}catch(Throwable e){
				SysUtils.log("加载logger配置异常",e );
			}
		}
		//默认一个控制台
		onHandlerEmpty();
		
		if(!packageSet.isEmpty()){
			setPackageFitler();
		}
	}
	/**
	 * 处理一个配置
	 * @param name
	 * @param config
	 * @throws Exception
	 */
	protected abstract void initOneConfig(String name,LogConfig config) throws Exception;
	/**
	 * 当没配置handler的时候
	 */
	protected abstract void onHandlerEmpty();
	/**
	 * 设置包控制过滤
	 */
	protected abstract void setPackageFitler();
	
	public Map<String, LogConfig> getLogger() {
		return loggers;
	}

	public void setLogger(Map<String, LogConfig> logger) {
		this.loggers = logger;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}
	
}
