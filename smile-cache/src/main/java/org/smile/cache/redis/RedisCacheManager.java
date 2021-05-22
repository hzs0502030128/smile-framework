package org.smile.cache.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.smile.cache.AbstractCacheManager;
import org.smile.cache.Cache;
import org.smile.collection.CollectionUtils;
import org.smile.commons.SmileRunException;
import org.smile.commons.Strings;
import org.smile.io.serialize.JdkSerializer;
import org.smile.io.serialize.Serializer;
import org.smile.io.serialize.StringSerializer;
import org.smile.log.Logger;
import org.smile.log.LoggerFactory;

import org.smile.util.StringUtils;
import redis.clients.jedis.Jedis;

public class RedisCacheManager extends AbstractCacheManager {

	private final Logger logger = LoggerFactory.getLogger(RedisCacheManager.class);
	/**redis操作模板*/
	private RedisTemplate template;

	private RedisCachePrefix cachePrefix = new DefaultRedisCachePrefix();
	//
	private boolean loadRemoteCachesOnStartup = false;
	/**是否可动态创建cache*/
	private boolean dynamic = true;
	/** 对象序列化 */
	protected Serializer<Object> serializer=new JdkSerializer();
	/**
	 * 用于对缓存键的序列化
	 */
	protected Serializer<String> stringSerializer=new StringSerializer();

	// 0 - never expire
	private long defaultExpiration = 0;
	//用cache名称来设置过期时间
	private Map<String, Long> expires = null;

	/**
	 * Construct a {@link RedisCacheManager}.
	 * 
	 * @param template
	 */
	@SuppressWarnings("rawtypes")
	public RedisCacheManager(RedisTemplate template) {
		this(template, Collections.<String> emptyList());
	}

	public RedisCacheManager(String name) {
		this.name=name;
		setCacheNames(Collections.<String> emptyList());
	}

	public RedisCacheManager() {
		setCacheNames(Collections.<String> emptyList());
	}



	/**
	 * Construct a static {@link RedisCacheManager}, managing caches for the
	 * specified cache names only.
	 * 
	 * @param template
	 * @param cacheNames
	 * @since 1.2
	 */
	@SuppressWarnings("rawtypes")
	public RedisCacheManager(RedisTemplate template, Collection<String> cacheNames) {
		this.template = template;
		setCacheNames(cacheNames);
	}

	/**
	 * Specify the set of cache names for this CacheManager's 'static' mode. <br/>
	 * The number of caches and their names will be fixed after a call to this
	 * method, with no creation of further cache regions at runtime.
	 */
	public void setCacheNames(Collection<String> cacheNames) {
		if (!CollectionUtils.isEmpty(cacheNames)) {
			for (String cacheName : cacheNames) {
				createAndAddCache(cacheName);
			}
			this.dynamic = false;
		}
	}
	/**
	 * Sets the cachePrefix. Defaults to 'DefaultRedisCachePrefix').
	 * 
	 * @param cachePrefix
	 *            the cachePrefix to set
	 */
	public void setCachePrefix(RedisCachePrefix cachePrefix) {
		this.cachePrefix = cachePrefix;
	}

	/**
	 * Sets the default expire time (in seconds).
	 * 
	 * @param defaultExpireTime
	 *            time in seconds.
	 */
	public void setDefaultExpiration(long defaultExpireTime) {
		this.defaultExpiration = defaultExpireTime;
	}

	/**
	 * Sets the expire time (in seconds) for cache regions (by key).
	 * 
	 * @param expires
	 *            time in seconds
	 */
	public void setExpires(Map<String, Long> expires) {
		this.expires = (expires != null ? new ConcurrentHashMap<String, Long>(expires) : null);
	}

	/**
	 * If set to {@code true} {@link RedisCacheManager} will try to retrieve
	 * cache names from redis server using {@literal KEYS} command and
	 * initialize {@link RedisCache} for each of them.
	 * 
	 * @param loadRemoteCachesOnStartup
	 * @since 1.2
	 */
	public void setLoadRemoteCachesOnStartup(boolean loadRemoteCachesOnStartup) {
		this.loadRemoteCachesOnStartup = loadRemoteCachesOnStartup;
	}

	public Collection<? extends Cache> loadCaches() {
		if (this.template == null) {
			throw new SmileRunException("A redis template is required in order to interact with data store");
		}
		return addConfiguredCachesIfNecessary(loadRemoteCachesOnStartup ? loadAndInitRemoteCaches() : Collections.<Cache> emptyList());
	}

	/**
	 * Returns a new {@link Collection} of {@link Cache} from the given caches
	 * collection and adds the configured {@link Cache}s of they are not already
	 * present.
	 * 
	 * @param caches
	 *            must not be {@literal null}
	 * @return
	 */
	private Collection<? extends Cache> addConfiguredCachesIfNecessary(Collection<? extends Cache> caches) {

		if (caches == null) {
			throw new IllegalArgumentException("Caches must not be null!");
		}

		Collection<Cache> result = new ArrayList<Cache>(caches);

		for (String cacheName : getCacheNames()) {

			boolean configuredCacheAlreadyPresent = false;

			for (Cache cache : caches) {
				if (cache.getName().equals(cacheName)) {
					configuredCacheAlreadyPresent = true;
					break;
				}
			}

			if (!configuredCacheAlreadyPresent) {
				result.add(getCache(cacheName));
			}
		}

		return result;
	}

	private Cache createAndAddCache(String cacheName) {
		addCache(createCache(cacheName));
		return super.getCache(cacheName);
	}

	/**
	 * 创建一个cache
	 * @param cacheName
	 * @return
	 */
	private RedisCache createCache(String cacheName) {
		long expiration = computeExpiration(cacheName);
		return new RedisCache(getName(),cacheName,this.cachePrefix, template, expiration);
	}

	private long computeExpiration(String name) {
		Long expiration = null;
		if (expires != null) {
			expiration = expires.get(name);
		}
		return (expiration != null ? expiration.longValue() : defaultExpiration);
	}

	private List<RedisCache> loadAndInitRemoteCaches() {

		List<RedisCache> caches = new ArrayList<RedisCache>();

		try {
			Set<String> cacheNames = loadRemoteCacheKeys();
			if (!CollectionUtils.isEmpty(cacheNames)) {
				for (String cacheName : cacheNames) {
					if (null == super.getCache(cacheName)) {
						caches.add(createCache(cacheName));
					}
				}
			}
		} catch (Exception e) {
			if (logger.isWarnEnabled()) {
				logger.warn("Failed to initialize cache with remote cache keys.", e);
			}
		}

		return caches;
	}

	@SuppressWarnings("unchecked")
	public Set<String> loadRemoteCacheKeys() {
		return (Set<String>) template.execute(new RedisCallback<Set<String>>() {
			@Override
			public Set<String> onCallRedis(Jedis jedis) {
				// we are using the ~keys postfix as defined in
				// RedisCache#setName
				Set<byte[]> keys = jedis.keys(stringSerializer.serialize(getName()+cachePrefix.getDelimiter()+"*~keys"));
				Set<String> cacheKeys = new LinkedHashSet<String>();

				if (!CollectionUtils.isEmpty(keys)) {
					for (byte[] key : keys) {
						String keyName=stringSerializer.deserialize(key).toString().replace("~keys", Strings.BLANK);
						String cacheName=StringUtils.split(keyName,cachePrefix.getDelimiter())[1];
						cacheKeys.add(cacheName);
					}
				}

				return cacheKeys;
			}
		});
	}

	@Override
	public Cache getCache(String name) {
		return getCache(name, this.dynamic);
	}

	@Override
	public Cache getCache(String cacheName, boolean ifNullInit) {
		Cache cache = this.cacheMap.get(cacheName);
		if (cache == null && ifNullInit) {
			return createAndAddCache(cacheName);
		}
		return cache;
	}

	private boolean transactionAware = false;

	/**
	 * Set whether this CacheManager should expose transaction-aware Cache
	 * objects.
	 * <p>
	 * Default is "false". Set this to "true" to synchronize cache put/evict
	 * operations with ongoing Spring-managed transactions, performing the
	 * actual cache put/evict operation only in the after-commit phase of a
	 * successful transaction.
	 */
	public void setTransactionAware(boolean transactionAware) {
		this.transactionAware = transactionAware;
	}

	/**
	 * Return whether this CacheManager has been configured to be
	 * transaction-aware.
	 */
	public boolean isTransactionAware() {
		return this.transactionAware;
	}

	public void setTemplate(RedisTemplate template) {
		this.template = template;
	}

}
