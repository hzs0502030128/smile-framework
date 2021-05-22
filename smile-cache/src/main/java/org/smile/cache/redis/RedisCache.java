package org.smile.cache.redis;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.smile.cache.Cache;
import org.smile.cache.CacheModel;
import org.smile.cache.Element;
import org.smile.collection.ArrayUtils;
import org.smile.commons.NotImplementedException;
import org.smile.io.serialize.Serializer;
import org.smile.util.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class RedisCache<K,V> implements Cache<K, V>{

	private static final int PAGE_SIZE = 128;
	private final String name;
	private final RedisTemplate template;
	private final byte[] prefix;
	private final byte[] setName;
	private final byte[] cacheLockName;
	/**等待锁的时间*/
	private long WAIT_FOR_LOCK = 300;
	/**过期时间*/
	private final long expiration;

	/**
	 * Constructs a new <code>RedisCache</code> instance.
	 * 
	 * @param name cache name
	 * @param template
	 * @param expiration
	 */
	RedisCache(String managerName,String name,RedisCachePrefix cachePrefix, RedisTemplate template, long expiration) {
		StringUtils.noText(name, "non-empty cache name is required");
		this.name = name;
		this.template = template;
		this.expiration = expiration;
		// name of the set holding the keys
		this.setName =  template.getStringSerializer().serialize(cachePrefix.keysPrefix(managerName,name)+"~keys");
		this.prefix=cachePrefix.prefix(managerName,name);
		this.cacheLockName = template.getStringSerializer().serialize(cachePrefix.keysPrefix(managerName,name)+"~lock");
	}

	@Override
	public String getName() {
		return name;
	}

	public Object getNativeCache() {
		return template;
	}

	@Override
	public V get(final K key) {
		return template.execute(new RedisCallback<V>() {
			@Override
			public V onCallRedis(Jedis jedis) {
				waitForLock(jedis);
				byte[] bs = jedis.get(computeKey(key));
				Object value = template.getValueSerializer() != null ? template.getValueSerializer().deserialize(bs) : bs;
				return (V)value;
			}
			
		}, true);
	}

	@Override
	public void put(final K key, final V value) {
		put(key, value, expiration);
	}

	public Element<K, V> putIfAbsent(K key, final V value) {

		final byte[] keyBytes = computeKey(key);
		final byte[] valueBytes = convertToBytesIfNecessary(template.getValueSerializer(), value);

		return toWrapper(key,template.execute(new RedisCallback<V>() {

			@Override
			public V onCallRedis(Jedis jedis) {
				waitForLock(jedis);

				V resultValue = value;
				boolean valueWasSet = jedis.setnx(keyBytes, valueBytes)==1L;
				if (valueWasSet) {
					jedis.zadd(setName, 0, keyBytes);
					if (expiration > 0) {
						jedis.expire(keyBytes, (int)expiration);
						// update the expiration of the set of keys as well
						jedis.expire(setName, (int)expiration);
					}
				} else {
					resultValue =(V) deserializeIfNecessary(template.getValueSerializer(), jedis.get(keyBytes));
				}

				return resultValue;
			}
			
		}, true));
	}

	private Element<K, V> toWrapper(K k,V value) {
		return (value != null ? new RedisCacheElement<K,V>(k,value) : null);
	}

	@Override
	public void clear() {
		// need to del each key individually
		template.execute(new RedisCallback<Object>() {

			@Override
			public Object onCallRedis(Jedis jedis) {
				// another clear is on-going
				if (jedis.exists(cacheLockName)) {
					return null;
				}

				try {
					jedis.set(cacheLockName, cacheLockName);

					int offset = 0;
					boolean finished = false;
					do {
						// need to paginate the keys
						Set<byte[]> keys = jedis.zrange(setName, (offset) * PAGE_SIZE, (offset + 1) * PAGE_SIZE - 1);
						finished = keys.size() < PAGE_SIZE;
						offset++;
						if (!keys.isEmpty()) {
							jedis.del(keys.toArray(new byte[keys.size()][]));
						}
					} while (!finished);

					jedis.del(setName);
					return null;

				} finally {
					jedis.del(cacheLockName);
				}
			}
			
		}, true);
	}

	private byte[] computeKey(Object key) {

		byte[] keyBytes = convertToBytesIfNecessary(template.getKeySerializer(),key);

		if (prefix == null || prefix.length == 0) {
			return keyBytes;
		}

		byte[] result = Arrays.copyOf(prefix, prefix.length + keyBytes.length);
		System.arraycopy(keyBytes, 0, result, prefix.length, keyBytes.length);

		return result;
	}

	private boolean waitForLock(Jedis connection) {
		boolean retry;
		boolean foundLock = false;
		do {
			retry = false;
			if (connection.exists(cacheLockName)) {
				foundLock = true;
				try {
					Thread.sleep(WAIT_FOR_LOCK);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				retry = true;
			}
		} while (retry);

		return foundLock;
	}
	/**
	 * 转成byte数组
	 * @param serializer
	 * @param value
	 * @return
	 */
	private byte[] convertToBytesIfNecessary(Serializer serializer, Object value) {
		if (serializer == null && value instanceof byte[]) {
			return (byte[]) value;
		}
		return serializer.serialize(value);
	}
	
	/**
	 * 解析成对象
	 * @param serializer
	 * @param value
	 * @return
	 */
	private Object deserializeIfNecessary(Serializer serializer, byte[] value) {
		if (serializer != null) {
			return serializer.deserialize(value);
		}

		return value;
	}

	@Override
	public long getCacheSize() {
		return 0L;
	}

	@Override
	public long getCacheTimeout() {
		return expiration;
	}

	@Override
	public void put(K key, V object, final long timeout) {
		final byte[] keyBytes = computeKey(key);
		final byte[] valueBytes = convertToBytesIfNecessary(template.getValueSerializer(), object);

		template.execute(new RedisCallback<Object>() {

			@Override
			public Object onCallRedis(Jedis jedis) {
				waitForLock(jedis);
				Transaction transaction=jedis.multi();
				transaction.set(keyBytes, valueBytes);
				transaction.zadd(setName, 0, keyBytes);
				long expire=expiration;
				if(timeout>0){
					expire=timeout/1000;
				}
				if (expire > 0) {
					transaction.expire(keyBytes, (int)expire);
					// update the expiration of the set of keys as well
					transaction.expire(setName,(int)expire);
				}
				transaction.exec();
				return null;
			}
			
		}, true);
	}

	@Override
	public void put(CacheModel<K, V> model, long timeout) {
		put(model.getKey(), model.getObject(), timeout);
	}

	@Override
	public Iterator<V> iterator() {
		throw new NotImplementedException();
	}

	@Override
	public Iterator<Element<K, V>> elements() {
		throw new NotImplementedException();
	}

	@Override
	public int prune() {
		return 0;
	}

	@Override
	public boolean isFull() {
		return false;
	}

	@Override
	public void remove(K key) {
		final byte[] k = computeKey(key);
		template.execute(new RedisCallback<Object>() {
			@Override
			public Object onCallRedis(Jedis connection) {
				connection.del(k);
				// remove key from set
				connection.zrem(setName, k);
				return null;
			}
			
		}, true);
	}

	@Override
	public void remove(Collection<K> keys) {
		//多个key一起删除
		final byte[][] keyList=new byte[keys.size()][];
		int idx=0;
		for(K k:keys){
			keyList[idx++]=computeKey(k);
		}
		template.execute(new RedisCallback<Object>() {
			@Override
			public Object onCallRedis(Jedis jedis) {
				jedis.del(keyList);
				// remove key from set
				jedis.zrem(setName, keyList);
				return null;
			}
			
		}, true);
	}

	@Override
	public long size() {
		return template.execute(new RedisCallback<Long>() {
			@Override
			public Long onCallRedis(Jedis connection) {
				waitForLock(connection);
				Long size = connection.zcard(setName);
				if(size ==null) return 0L;
				return size;
			}
			
		}, true);
	}

	@Override
	public boolean isEmpty() {
		return size()==0;
	}

	@Override
	public Collection<K> keys() {
		return template.execute(new RedisCallback<Set<K>>() {

			@Override
			public Set<K> onCallRedis(Jedis connection) {
				waitForLock(connection);
				Set<K> keySet=new LinkedHashSet<K>();
				int offset = 0;
				boolean finished = false;
				do {
					// need to paginate the keys
					Set<byte[]> keys = connection.zrange(setName, (offset) * PAGE_SIZE, (offset + 1) * PAGE_SIZE - 1);
					finished = keys.size() < PAGE_SIZE;
					offset++;
					if (!keys.isEmpty()) {
						for(byte[] b:keys){
							b=ArrayUtils.subarray(b,prefix.length, b.length-prefix.length);
							keySet.add((K)deserializeIfNecessary(template.getKeySerializer(), b));
						}
					}
				} while (!finished);
				return keySet;
			}
			
		}, true);
	}

	@Override
	public Collection<K> noExpiredKeys() {
		throw new NotImplementedException("Unsupport this method ");
	}

}
