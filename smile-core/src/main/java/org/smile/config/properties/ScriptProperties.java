package org.smile.config.properties;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

import org.smile.commons.ExceptionUtils;
import org.smile.commons.SmileRunException;
import org.smile.config.Config;
import org.smile.io.IOUtils;
import org.smile.log.LoggerHandler;
import org.smile.script.Executor;
import org.smile.util.ClassPathUtils;
import org.smile.util.RegExp;
import org.smile.util.RegExp.MatchInfo;
import org.smile.util.StringUtils;

public abstract class ScriptProperties implements LoggerHandler,Config{
	
	protected static final String GROOVY_CONFIG_EXT="_groovy.cfg";
	
	protected static final String JS_CONFIG_EXT="_js.cfg";
	
	protected static final String DEFAULT_RESULT_VAR="config";
	
	private RegExp startReg=new RegExp("\\[config(:[a-zA-Z0-9]+)?\\]");
	
	private RegExp endReg=new RegExp("\\[/config\\]");
	
	/**索引与对象的对象关系*/
	protected Map<Integer,Object> configs=new LinkedHashMap<Integer,Object>();
	/**
	 * 名称与对象的对应
	 */
	protected Map<String,Object> keyConfigs=new HashMap<String, Object>();
	/**名称与索引的对应*/
	protected Map<String,Integer> keyIndexs=new HashMap<String, Integer>();
	
	protected Object initParams;
	
	protected abstract Executor getExecutor();
	
	protected abstract String getScriptType();
	/**
	 * 对返回的对象进行转换
	 * @param obj
	 * @return
	 */
	protected abstract Object convertConfig(Object obj);
	
	public void load(InputStream is) throws IOException{
		String content=IOUtils.readString(is);
		Matcher start=startReg.matcher(content);
		Matcher end=endReg.matcher(content);
		Map<String,String> configsStr=findConfigString(content, start, end);
		int index=0;
		Executor executor=getExecutor();
		for(Map.Entry<String, String> c:configsStr.entrySet()){
			executor.setResultVar(c.getValue());
			Object obj=executor.execute(c.getKey(),initParams);
			configs.put(index++,convertConfig(obj));
		}
		initKeyConfigs();
	}
	/**
	 * 初始化键值对应的map中
	 */
	private void initKeyConfigs(){
		for(Map.Entry<String, Integer> entry:keyIndexs.entrySet()){
			keyConfigs.put(entry.getKey(), configs.get(entry.getValue()));
		}
	}
	/**
	 * 查看出配置单位对象
	 * @param content 配置文件的全部文本内容
	 * @param start 开始标记正则匹配
	 * @param end 结束标签正则匹配
	 * @return 配置内容与名称的对应
	 */
	private Map<String,String> findConfigString(String content,Matcher start,Matcher end){
		Map<String,String> list=new LinkedHashMap<String,String>();
		MatchInfo startInfo=startReg.firstMatch(start, 0);
		int index=0;
		while(startInfo!=null){
			MatchInfo endInfo=endReg.firstMatch(end, startInfo.getEnd());
			String key=parserKey(startInfo);
			if(key!=null){
				if(keyIndexs.containsKey(key)){
					throw new SmileRunException("重复的key值"+key);
				}
				keyIndexs.put(key,index);
			}else{
				key=DEFAULT_RESULT_VAR;
			}
			String config=StringUtils.trim(content.substring(startInfo.getEnd(), endInfo.getStart()));
			list.put(config,key);
			startInfo=startReg.firstMatch(start, endInfo.getEnd());
			index++;
		}
		return list;
	}
	/**
	 * 解析出配置单位的名称
	 * @param header
	 * @return
	 */
	private String parserKey(MatchInfo header){
		String config=header.getContext();
		if(config.indexOf(":")>0){
			config=config.substring(1, config.length()-1);
			return config.split(":")[1];
		}
		return null;
	}
	/**
	 * 加载classpath下的文件
	 * @param filename
	 * @throws IOException
	 */
	public void loadClassPathFile(String filename) throws IOException{
		InputStream is=GroovyProperties.class.getClassLoader().getResourceAsStream(filename);
		load(is);
	}
	/**
	 * 以索引获取配置内容
	 * @param index 配置的顺序 从0开始
	 * @return
	 */
	public Object getProperty(int index){
		return configs.get(index);
	}
	
	public Object getFirst(){
		return configs.get(0);
	}

	public Map<Integer, Object> getConfigs() {
		return configs;
	}
	
	public Set<Integer> indexSet(){
		return configs.keySet();
	}
	
	public Collection<Object> getValues(){
		return configs.values();
	}


	public Object getInitParams() {
		return initParams;
	}
	/**
	 * 以配置名称获取内容
	 * @param name 配置的名称[config:xxx] 名称为xxx
	 * @return
	 */
	public Object getProperty(String name){
		Integer index=keyIndexs.get(name);
		if(index!=null){
			return getProperty(index);
		}
		return null;
	}
	
	public Map<String,Integer> getKeyIndexs(){
		return this.keyIndexs;
	}
	/**
	 * 以文件结尾形式加载配置文件
	 * _js.cfg 为javascript类型配置文件 
	 * _groovy.cfg 为groovy类型配置文件 
	 * .cfg 结尾  为基础的baseproperties类型
	 * @param name
	 * @return
	 */
	public static ScriptProperties loadPropertiesFormClassPath(String name){
		ScriptProperties properties=null;
		FileFilter filter=new ConfigNameFilter(name);
		List<File> dirs=ClassPathUtils.getClassPathDir();
		retrun:
		for(File dir:dirs){
			File[] filelist=dir.listFiles(filter);
			for(File f:filelist){
				String filename=f.getName();
				if(filename.endsWith(GROOVY_CONFIG_EXT)){
					properties=new GroovyProperties();
				}else if(filename.endsWith(JS_CONFIG_EXT)){
					properties=new JavaScriptProperties();
				}else{
					properties=new BaseProperties();
				}
				try{
					properties.load(new FileInputStream(f),new HashMap<Object,Object>(System.getProperties()));
					logger.debug(f.getAbsolutePath());
					break retrun;
				}catch(Exception e){
					logger.error(ExceptionUtils.getExceptionMsg(e));
				}
			}
		}
		return properties;
	}

	public void setInitParams(Map<String, ? extends Object> initParams) {
		this.initParams = initParams;
	}

	@Override
	public void load(File file) throws IOException {
		InputStream is=new FileInputStream(file);
		try{
			load(is);
		}finally{
			IOUtils.close(is);
		}
	}

	@Override
	public void load(File file, Object initParam) throws IOException {
		this.initParams=initParam;
		load(file);
	}

	@Override
	public void load(InputStream is, Object initParam) throws IOException {
		this.initParams=initParam;
		try{
			load(is);
		}finally{
			IOUtils.close(is);
		}
	}

	@Override
	public <T> T getValue(String key) {
		return (T)getProperty(key);
	}

	@Override
	public Set getKeys() {
		return keyConfigs.keySet();
	}
	
	
}
