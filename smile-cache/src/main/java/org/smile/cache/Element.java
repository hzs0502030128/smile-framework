package org.smile.cache;
/***
 * 缓存元素
 * @author 胡真山
 */
public interface Element<K,V>{
	/**
	 * 缓存的键
	 * @return
	 */
	public K getKey();
	/**
	 * 获取缓存对象
	 * @return
	 */
	public V getObject();
	/**
	 * 最后访问时间
	 * @return
	 */
	public long lastAccessTime();
	/**
	 * 访问次数
	 * @return
	 */
	public long accessCount();
	/**
	 * 是否过期
	 * @return
	 */
	public boolean isExpired();
}
