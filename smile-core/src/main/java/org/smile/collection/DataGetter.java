package org.smile.collection;

import java.math.BigDecimal;
import java.util.Date;

import org.smile.json.JSONArray;
import org.smile.json.JSONObject;

public interface DataGetter<K> {
	/**
	 * 获取值返回long
	 * @param key
	 * @return
	 */
	public Long getLong(K key);
	/**
	 * 获取值以float类型返回
	 * @param key
	 * @return
	 */
	public Float getFloat(K key);
	/**
	 * 获取值以double类型返回
	 * @param key
	 * @return
	 */
	public Double getDouble(K key);
	/**
	 * 获取值以BigDecimal类型返回
	 * @param key
	 * @return
	 */
	public BigDecimal getBigDecimal(K key);
	/**
	 * 获取值以String类型返回
	 * @param key
	 * @return
	 */
	public String getString(K key);
	/**
	 * 获取值以Date类型返回
	 * @param key
	 * @return
	 */
	public Date getDate(K key);
	/**
	 * 获取值以Integer类型返回
	 * @param key
	 * @return
	 */
	public Integer getInt(K key);
	/**
	 * 获取值以byte类型返回
	 * @param key
	 * @return
	 */
	public Byte getByte(K key);
	/**
	 * 获取值以short类型返回
	 * @param key
	 * @return
	 */
	public Short getShort(K key);
	/**
	 * 获取值指定返回转换类型
	 * @param <T>
	 * @param key
	 * @param resultClass 返回类型
	 * @return
	 */
	public <T> T get(K key,Class<T> resultClass);
	/**
	 * 返回值类型以json对象
	 * @param key
	 * @return
	 */
	public JSONObject getJSONObject(K key);
	/**
	 * 返回值为jsonArray
	 * @param key
	 * @return
	 */
	public JSONArray getJSONArray(K key);
	/**
	 * 返回boolean类型
	 * @param key
	 * @return
	 */
	public Boolean getBoolean(K key);
}
