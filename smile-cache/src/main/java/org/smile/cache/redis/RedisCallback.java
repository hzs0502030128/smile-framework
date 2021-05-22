package org.smile.cache.redis;

import redis.clients.jedis.Jedis;

public interface RedisCallback<T> {
	/***
	 * 调用redis方法
	 * @param jedis
	 * @return
	 */
	public T onCallRedis(Jedis jedis);
}
