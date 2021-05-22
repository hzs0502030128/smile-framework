package org.smile.cache.plugin.impl;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.smile.cache.plugin.CacheKey;
import org.smile.cache.plugin.KeyGenerator;
import org.smile.cache.plugin.ann.CacheWipe;
import org.smile.cache.plugin.ann.Cacheable;
import org.smile.cache.plugin.util.CachePluginUtils;
import org.smile.collection.ArrayUtils;
import org.smile.commons.SmileRunException;
import org.smile.commons.Strings;
import org.smile.json.JSON;
import org.smile.log.LoggerHandler;
import org.smile.plugin.Invocation;
import org.smile.util.ObjectLenUtils;
import org.smile.util.StringUtils;
/**
 * 一个通用的实现  
 * 不支持参数忽略   但可以继承此类实现 
 * @author 胡真山
 *
 */
public class DefaultKeyGenerator implements KeyGenerator,LoggerHandler {
	
	public CacheKey generate(Method method, Object[] args, String configKey, String[] fieldNames,boolean ignoreArgs) {
		if(StringUtils.isEmpty(configKey)){
			configKey=CachePluginUtils.getMethodCacheKey(method);
		}
		if(ArrayUtils.notEmpty(fieldNames)){
			//只取指定了的字段信息
			Map<String,Object> params=CachePluginUtils.getMethodParam(method, fieldNames, args);
			if(params==null){
				throw new SmileRunException("获取 fileds :"+JSON.toJSONString(fieldNames)+"内容失败,请正确指定参数名或使用字节码工具解析方法参数名称");
			}
			return createCacheKey(configKey, JSON.toJSONString(params.values()));
		}else{
			//没有指定字段的时候，直接把所有的参数信息转成json信息
			String paramsValue=null;
			if(ignoreArgs){
				paramsValue=Strings.BLANK;
			}else{
				paramsValue=CachePluginUtils.getMethodParamValues(args);
			}
			return createCacheKey(configKey,paramsValue);
		}
	}
	
	@Override
	public CacheKey generateWrite(Invocation invocation, Cacheable write) {
		return generate(invocation.getMethod(), invocation.getArgs(), write.key(), write.fields(),write.ignoreArgs());
	}

	@Override
	public CacheKey generateWipe(Invocation invocation, CacheWipe wipe) {
		return generate(invocation.getMethod(), invocation.getArgs(), wipe.key(), wipe.fields(),wipe.ignoreArgs());
	}

	@Override
	public List generateLoopWipe(Invocation invocation,CacheWipe wipe) {
		Method method=invocation.getMethod();
		Object[] args=invocation.getArgs();
		String configKey=wipe.key();
		String[] fieldNames=wipe.fields();
		if(args.length!=1){
			throw new SmileRunException("The loop cache key only support one parameter of the method");
		}
		Object loopArg=args[0];
		if(!ObjectLenUtils.hasLength(loopArg)){
			throw new SmileRunException("The parameter of the method must an collection or array, to used loop ");
		}
		if(StringUtils.isEmpty(configKey)){
			throw new SmileRunException("must has designated key");
		}
		List keyList=new LinkedList();
		if(ArrayUtils.notEmpty(fieldNames)){
			Map<String,Object> paramsMap=CachePluginUtils.getMethodParam(method, fieldNames, args);
			Object[][] params=new Object[fieldNames.length][];
			for(int i=0;i<fieldNames.length;i++){
				Object temp=paramsMap.get(fieldNames[i]);
				if(temp instanceof Collection){
					params[i]=ArrayUtils.valuesOf((Collection)temp);
				}else{
					throw new SmileRunException("field:"+fieldNames[i]+" of "+loopArg+" must is is a collection ");
				}
			}
			int oneList=params[0].length;
			for(int i=0;i<oneList;i++){
				Object[]  array=new Object[params.length];
				for(int j=0;j<params.length;j++){
					array[j]=params[j][i];
				}
				keyList.add(createCacheKey(configKey, CachePluginUtils.getMethodParamValues(array)).toKey());
			}
		}else{
			int len=ObjectLenUtils.len(loopArg);
			for(int i=0;i<len;i++){
				Object[] tempArgs=new Object[]{ObjectLenUtils.get(loopArg, i)};
				keyList.add(createCacheKey(configKey, CachePluginUtils.getMethodParamValues(tempArgs)).toKey());
			}
		}
		logger.debug(keyList);
		return keyList;
	}

	
	protected CacheKey createCacheKey(String key,String paramValues){
		return new DefaultCacheKey(key, paramValues);
	}
	
}
