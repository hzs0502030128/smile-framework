package org.smile.cache;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.omg.Messaging.SyncScopeHelper;
import org.smile.annotation.AnnotationUtils;
import org.smile.cache.plugin.CglibCachePluginInterceptor;
import org.smile.cache.plugin.ann.CacheWipe;
import org.smile.cache.plugin.ann.CacheWipes;
import org.smile.cache.redis.RedisCacheManager;
import org.smile.cache.redis.RedisTemplate;
import org.smile.util.SysUtils;

public class TestRedis {
	
	protected RedisTemplate template;
	protected RedisCacheManager manager=new RedisCacheManager("manager");
	@Before
	public void before() {
		template=new RedisTemplate();
		template.setDatabase(0);
		template.setHost("127.0.0.1");
		template.setPort(6379);
		template.setTimeout(5000);
		template.init();
		manager.setDefaultExpiration(30000);
		manager.setTemplate(template);
	}
	@Test
	public void testManager(){
		Cache<String,Object> cache=manager.getCache("cache01");
		cache.put("name", "胡真山");
		String name=(String) cache.get("name");
		System.out.println(name);
		Cache cache2=manager.getCache("cache02");
		cache2.put("name", "胡真山02");
		name=(String) cache.get("name");
		System.out.println(name);
		name=(String) cache2.get("name");
		System.out.println(name);
		cache.put("age", 100);
//		cache2.clear();
//		cache.clear();
		System.out.println(cache.keys());
		System.out.println(cache2.keys());
		cache.remove("name");
		System.out.println(cache.get("name"));
		System.out.println(cache.keys());
		System.out.println(cache2.keys());
	}
	@Test
	public void testCachePlugin(){
		CglibCachePluginInterceptor interceptor =new CglibCachePluginInterceptor();
		interceptor.setCacheManager(manager);
		CacheSevice service= (CacheSevice) interceptor.plugin(new CacheSevice());
		SysUtils.log(service.getList());
		SysUtils.log(service.getList());
		SysUtils.log(service.getList());
	}
}
