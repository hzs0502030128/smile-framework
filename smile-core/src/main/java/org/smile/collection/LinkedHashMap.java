package org.smile.collection;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.smile.json.JSONArray;
import org.smile.json.JSONObject;

public class LinkedHashMap<K, V> extends java.util.LinkedHashMap<K, V> implements DataGetter<K>{

	public LinkedHashMap(int size) {
		super(size);
	}

	public LinkedHashMap() {
		super();
	}

	public LinkedHashMap(Map<? extends K, ? extends V> paramMap) {
		super(paramMap);
	}

	/**
	 * 返回值转换成日期类型
	 * @param key
	 * @return
	 */
	@Override
	public Date getDate(K key) {
		return MapUtils.getDate(this, key);
	}

	/**
	 * 返回值转换成字符串类型
	 * @param key
	 * @return
	 */
	@Override
	public String getString(K key) {
		return MapUtils.getString(this, key);
	}

	/**
	 * 返回值转成int类型
	 * @param key
	 * @return
	 */
	@Override
	public Integer getInt(K key) {
		return MapUtils.getInt(this, key);
	}

	/**
	 * 返回值转换成double类型
	 * @param key
	 * @return
	 */
	@Override
	public Double getDouble(K key) {
		return MapUtils.getDouble(this, key);
	}
	/**
	 * 返回值转换成float类型
	 * @param key
	 * @return
	 */
	@Override
	public Float getFloat(K key){
		return MapUtils.getFloat(this, key);
	}

	/**
	 * 会做一个类型的转换
	 * */
	@Override
	public <T> T get(K key, Class<T> returnType) {
		return MapUtils.getObject(this, key, returnType);
	}
	/**
	 * 以boolean类型返回
	 * @param key
	 * @return
	 */
	@Override
	public Boolean getBoolean(K key) {
		return MapUtils.getBoolean(this, key);
	}
	/**
	 * 不为空
	 * @return
	 */
	public boolean notEmpty(){
		return !isEmpty();
	}
	
	/**
	 * 	从map中获取一个字符串 解析成json对象返回
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @param key
	 * @return
	 */
	@Override
	public JSONObject getJSONObject(K key) {
		return MapUtils.getJSONObject(this, key);
	}
	/**
	 *	获取一个json array 字符串并解析  解析json  array
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @param key
	 * @return
	 */
	@Override
	public  JSONArray getJSONArray(K key) {
		return MapUtils.getJSONArray(this, key);
	}

	@Override
	public Long getLong(K key) {
		return MapUtils.getLong(this, key);
	}

	@Override
	public BigDecimal getBigDecimal(K key) {
		return MapUtils.getBigDecimal(this,key);
	}

	@Override
	public Byte getByte(K key) {
		return MapUtils.getByte(this, key);
	}

	@Override
	public Short getShort(K key) {
		return MapUtils.getShort(this, key);
	}

}
