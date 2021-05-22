package org.smile.cache.plugin;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.smile.cache.Cache;
import org.smile.cache.CacheManager;
import org.smile.cache.plugin.ann.CacheWipe;
import org.smile.cache.plugin.ann.Cacheable;
import org.smile.cache.plugin.impl.DefaultKeyGenerator;
import org.smile.cache.plugin.impl.DefaultKeyOperator;
import org.smile.cache.simple.LFUCache;
import org.smile.commons.Strings;
import org.smile.log.LoggerHandler;
import org.smile.plugin.Invocation;

public class BaseCachePluginHandler implements CachePluginHandler,LoggerHandler {

	private static String defaultSchemeName = "_default_";
	/**
	 * 默认使用的key生成器
	 */
	private KeyGenerator defaultKeyGengerator=new DefaultKeyGenerator();

	private CacheKeyOperator defaultKeyOperator = new DefaultKeyOperator();

	/**扩展其它策略,可以在注解上指名称使用扩展*/
	protected Map<String,CacheScheme> extendCaches=new ConcurrentHashMap<>();

	/**用于产生cache*/
	protected CacheManager cacheManager;

	/***默认策略*/
	protected CacheScheme defaultScheme;
	
	@Override
	public Object doCacheable(Cacheable cacheWrite, Invocation invocation) throws Throwable {
		//使用缓存策略
		CacheScheme cacheScheme=getCacheScheme(cacheWrite.scheme());
		CacheKey cacheKey=null;
		try {
			cacheKey=cacheScheme.generateWrite(invocation,cacheWrite);
		} catch (Exception e) {
			logger.error("转换key出错"+cacheWrite,e);
		}
		if(!cacheWrite.onlyWrite()){//不是只写缓存时，读取缓存
			try {
				Object result = cacheScheme.read(cacheKey);
				if (result!=null) {
					logger.debug(cacheKey);
					return result;
				}
			} catch (Exception e) {
				throw new CachePluginException("使用缓存异常:"+cacheKey,e);
			}
		}
		//读取不到缓存时执行方法 ，写入缓存
		Object result = invocation.proceed();
		if (result != null) {
			try {
				//写入缓存
				if(cacheWrite.validTime() <= 0){
					cacheScheme.write(cacheKey, result);
				}else{
					cacheScheme.write(cacheKey, result,cacheWrite.validTime()*1000L);
				}
			} catch (Exception e) {
				throw new CachePluginException("写入缓存异常:"+cacheKey,e);
			}
		}
		return result;
	}

	@Override
	public Object doCacheWipe(CacheWipe[] cacheWipes, Invocation invocation) throws Throwable {
		Object result = invocation.proceed();
		CacheScheme cacheScheme;
		for (CacheWipe wipe : cacheWipes) {
			try {
				cacheScheme=getCacheScheme(wipe.scheme());
				cacheScheme.remove(wipe, invocation);
			} catch (Exception e) {
				throw new CachePluginException("移除缓存失败:"+wipe,e);
			}
		}
		return result;
	}
	
	protected CacheScheme getCacheScheme(String schemeName){
		if(Strings.BLANK.equals(schemeName)){
			if(this.defaultScheme ==null){
				return createDefaultScheme();
			}
			return defaultScheme;
		}else{
			CacheScheme cs= extendCaches.get(schemeName);
			if (cs == null) {
				if(this.cacheManager == null){
					throw new NullPointerException("not has a scheme named "+ schemeName);
				}
				synchronized (extendCaches) {
					cs= createCacheScheme(schemeName);
				}
			}
			return cs;
		}
	}

	/**
	 * 从cache manager中创建一个缓存策略
	 * @param schemeName
	 * @return
	 */
	protected CacheScheme createCacheScheme(String schemeName){
		CacheScheme cs = this.extendCaches.get(schemeName);
		if(cs==null){
			Cache cache =this.cacheManager.getCache(schemeName,true);
			cs = new DefaultCacheScheme(cache,this.defaultKeyGengerator,this.defaultKeyOperator);
			this.extendCaches.put(schemeName,cs);
		}
		return cs;
	}

	protected synchronized CacheScheme createDefaultScheme(){
		if(this.cacheManager!=null){
			this.defaultScheme=createCacheScheme(defaultSchemeName);
		}else{
			this.defaultScheme=new DefaultCacheScheme(new LFUCache(),defaultKeyGengerator,defaultKeyOperator);
		}
		return this.defaultScheme;
	}

	public void setDefaultScheme(CacheScheme defaultScheme) {
		this.defaultScheme = defaultScheme;
	}

	/**
	 * 扩展缓存策略  
	 * @param scheme 要扩展的策略  必须设置一个名称 
	 */
	public void setExtendCache(CacheScheme scheme){
		this.extendCaches.put(scheme.getName(), scheme);
	}
	/**
	 * 设置多个扩展
	 * @param schemes scheme 要扩展的策略  必须设置一个名称 
	 */
	public void setExtends(Collection<CacheScheme> schemes){
		for(CacheScheme scheme:schemes){
			this.setExtendCache(scheme);
		}
	}

	public void setDefaultKeyGengerator(KeyGenerator defaultKeyGengerator) {
		this.defaultKeyGengerator = defaultKeyGengerator;
	}

	public void setDefaultKeyOperator(CacheKeyOperator defaultKeyOperator) {
		this.defaultKeyOperator = defaultKeyOperator;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
}
