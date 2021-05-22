package org.smile.cache.redis;

public interface RedisCachePrefix {

	/**
	 * 缓存内容key的前缀
	 * @param managerName
	 * @param cacheName
	 * @return
	 */
	byte[] prefix(String managerName,String cacheName);

	/**
	 * 用于缓存keys的前缀
	 * @param managerName
	 * @param cacheName
	 * @return
	 */
	String keysPrefix(String managerName, String cacheName);

	/**
	 * 前缀中的分隔符
	 * @return
	 */
	String getDelimiter();

}