package org.smile.log.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.smile.collection.CollectionUtils;
import org.smile.log.record.ConsoleHandler;
import org.smile.log.record.Handler;
import org.smile.log.record.RecordFormatter;
import org.smile.reflect.ClassTypeUtils;

public class SimpleSmileLogConfig extends AbstractSmileLogConfig{
	
	protected Map<String,Handler> handlers=new HashMap<String,Handler>();
	/**
	 * 获取handler 如果是handler 名称  返回一个handler 
	 * 不然返回所有的handler
	 * @param name
	 * @return
	 */
	public Collection<Handler> getHandlers(String name,PackageSet set){
		Handler handler=handlers.get(name);
		if(handler==null){
			if(set!=null){
				if(CollectionUtils.notEmpty(set.getLoggerNames())){
					List<Handler> handerlist=new LinkedList<Handler>();
					for(String n:set.getLoggerNames()){
						handler=handlers.get(n);
						handerlist.add(handler);
					}
					return handerlist;
				}
			}
			return handlers.values();
		}else{
			return CollectionUtils.linkedList(handler);
		}
	}
	/**
	 * 设置包控制过滤
	 */
	@Override
	protected void setPackageFitler(){
		for(Handler handler:handlers.values()){
			handler.setFilter(new SimplePackageLevelFilter());
		}
	}
	@Override
	protected void initOneConfig(String name,LogConfig config) throws Exception{
		Handler handler=null;
		if(config.pattern!=null){
			handler=(Handler)config.handler.getConstructor(String.class).newInstance(config.pattern);
		}else{
			Class<Handler> clazz=config.handler;
			handler=ClassTypeUtils.newInstance(clazz);
		}
		if(config.formatter!=null){
			RecordFormatter formatter=(RecordFormatter)ClassTypeUtils.newInstance(config.formatter);
			//设置格式化模板
			if(config.formatPattern!=null){
				formatter.setPattern(config.formatPattern);
			}
			handler.setFormatter(formatter);
		}
		if(config.level!=null){
			handler.setLevel(config.level);
		}else if(level!=null){
			handler.setLevel(level);
		}
		handlers.put(name, handler);
	}

	@Override
	protected void onHandlerEmpty() {
		//默认一个控制台
		if(handlers.isEmpty()){
			Handler handler=new ConsoleHandler();
			if(level!=null){
				handler.setLevel(level);
			}
			handlers.put(CONFIG_NAME,handler);
		}
	}

}
