package org.smile.cache.ecache;

import java.io.IOException;
import java.io.InputStream;

import net.sf.ehcache.CacheManager;

import org.smile.cache.AbstractCacheManager;
import org.smile.cache.Cache;
import org.smile.commons.SmileRunException;
import org.smile.io.ResourceUtils;
import org.smile.util.StringUtils;

public class BaseEcacheManager extends AbstractCacheManager {
	/** ehcache*/
	protected CacheManager ecacheManager;
	/**配置文件*/
	protected String cacheManagerConfigLocal;
	/**配置文件名称 需放在classpath下*/
	protected String configFileName;
	
	protected void initCacheManager(){
		if(ecacheManager==null){
			InputStream is=null;
			if(StringUtils.notEmpty(cacheManagerConfigLocal)){
				try {
					is=ResourceUtils.getInputStreamForPath(cacheManagerConfigLocal);
				} catch (IOException e) {
					throw new SmileRunException("load ecache file "+cacheManagerConfigLocal+" error",e);
				}
			}else if(StringUtils.notEmpty(configFileName)){
				is=ResourceUtils.loadFromClassPath(configFileName);
			}
			if(is!=null){
				ecacheManager=CacheManager.newInstance(is);
			}else{
				ecacheManager=CacheManager.getInstance();
			}
		}
	}

	public void setEcacheManager(CacheManager ecacheManager) {
		this.ecacheManager = ecacheManager;
	}

	

	public void setCacheManagerConfigLocal(String cacheManagerConfigLocal) {
		this.cacheManagerConfigLocal = cacheManagerConfigLocal;
	}

	public void setConfigFileName(String configFileName) {
		this.configFileName = configFileName;
	}

	@Override
	public boolean existsCache(String cacheName) {
		if(cacheMap.containsKey(cacheName)){
			return true;
		}
		return ecacheManager.cacheExists(cacheName);
	}
	

	@Override
	public Cache getCache(String cacheName, boolean ifnullInit) {
		Cache cache=this.cacheMap.get(cacheName);
		if(cache==null){
			synchronized (this) {
				cache=this.cacheMap.get(cacheName);
				if(cache==null){
					initCacheManager();
					net.sf.ehcache.Cache ecache = ecacheManager.getCache(cacheName);
					if (ecache == null) {
						if(ifnullInit){
							//以默认配置新增一个cache
							ecacheManager.addCache(cacheName);
							ecache= ecacheManager.getCache(cacheName);
						}else{
							return null;
						}
					}
					cache=new BaseEcache(ecache);
					addCache(cache);
				}
				return cache;
			}
		}
		return cache;
	}
}
