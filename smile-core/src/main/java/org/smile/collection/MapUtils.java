package org.smile.collection;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.smile.beans.converter.BasicConverter;
import org.smile.beans.converter.ConvertException;
import org.smile.beans.converter.Converter;
import org.smile.beans.converter.type.BigDecimalConverter;
import org.smile.beans.converter.type.BooleanConverter;
import org.smile.collection.CollectionUtils.GroupKey;
import org.smile.collection.ReferenceHashMap.RefValue;
import org.smile.commons.SmileRunException;
import org.smile.commons.Strings;
import org.smile.json.JSONArray;
import org.smile.json.JSONObject;
import org.smile.json.parser.JSONParseException;
import org.smile.util.DateUtils;
import org.smile.util.StringUtils;

/**
 * 这是一个Map的工具类
 * @author 胡真山
 *
 */
public class MapUtils {
	/***
	 * 类型转换器
	 */
	private static Converter CONVERTER=BasicConverter.getInstance();
	/**
	 * 实例化一个HashMap对象
	 * @return
	 */
	public static <K,V> Map<K,V> hashMap(){
		return new HashMap<K, V>();
	}
	
	public static <K,V> Map<K,V> treeMap(){
		return new TreeMap<K, V>();
	}
	
	public static <K,V> Map<K,V> hashMap(int size){
		return new HashMap<K, V>(size);
	}
	
	public static <K,V> Map<K,V> treeMap(Comparator<K> comparator){
		return new TreeMap<K, V>(comparator);
	}
	
	public static <K,V> Map<K,V> concurrentHashMap(){
		return new ConcurrentHashMap<K, V>();
	} 
	
	public static <K,V> Map<K,V> concurrentHashMap(int size){
		return new ConcurrentHashMap<K, V>(size);
	} 
	/**
	 * 软引用的一个HashMap的实现
	 * @return
	 */
	public static <K,V> SoftHashMap<K,V> softHashMap(){
		return new SoftHashMap<K, V>();
	} 
	/**
	 * ConcurrentHashMap 实现的一个软引用的map
	 * @return
	 */
	public static <K,V> SoftHashMap<K,V> softConcurrentHashMap(){
		return new SoftHashMap<K,V>(new ConcurrentHashMap<K, RefValue<K,V>>());
	} 
	
	/**
	 * 以数组生成一个map 以索引为key值为value
	 * @param array
	 * @return
	 */
	public static <T> Map<Integer, T> treeMap(T[] array) {
		Map<Integer, T> map = new TreeMap<Integer, T>();
		for (int i = 0; i < array.length; i++) {
			map.put(i, array[i]);
		}
		return map;
	}
	/**
	 * 返回浮点型
	 * @param map
	 * @param key
	 * @return
	 */
	public static <K,V> Float getFloat(Map<K,V> map,K key){
		V value=map.get(key);
		if(StringUtils.isNotNull(value)){
			if(value instanceof Number){
				return ((Number) value).floatValue();
			}else{
				return Float.valueOf(String.valueOf(value));
			}
		}
		return null;
	}
	/**
	 * 返回整型
	 * @param map
	 * @param key
	 * @return
	 */
	public static <K,V> Integer getInt(Map<K,V> map,K key){
		V value=map.get(key);
		if(StringUtils.isNotNull(value)){
			if(value instanceof Integer){
				return (Integer)value;
			}else if(value instanceof Number){
				return ((Number) value).intValue();
			}else{
				return Integer.valueOf(String.valueOf(value));
			}
		}
		return null;
	}
	/**
	 * 从map中获取值 以byte类型返回
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @param key
	 * @return
	 */
	public static <K,V> Byte getByte(Map<K,V> map,K key) {
		V value=map.get(key);
		if(StringUtils.isNotNull(value)){
			if(value instanceof Number){
				return ((Number) value).byteValue();
			}else{
				return Byte.valueOf(String.valueOf(value));
			}
		}
		return null;
	}
	
	public static <K,V> Boolean getBoolean(Map<K,V> map,K key) {
		V value=map.get(key);
		if(StringUtils.isNotNull(value)){
			try {
				return BooleanConverter.getInstance().convert(value);
			} catch (ConvertException e) {
				throw new SmileRunException(e);
			}
		}
		return null;
	}
	/**
	 * 从map中获取值 以short类型返回
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @param key
	 * @return
	 */
	public static <K,V> Short getShort(Map<K,V> map,K key) {
		V value=map.get(key);
		if(StringUtils.isNotNull(value)){
			if(value instanceof Number){
				return ((Number) value).shortValue();
			}else{
				return Short.valueOf(String.valueOf(value));
			}
		}
		return null;
	}
	
	/**
	 * 以日期类型返回
	 * @param map
	 * @param key
	 * @return
	 */
	public static <K,V> Date getDate(Map<K,V> map,K key){
		V value=map.get(key);
		if(value!=null){
			if(value instanceof Date){
				return (Date) value;
			}else{
				return DateUtils.convertToDate(value);
			}
		}
		return null;
	}
	

	/**
	 * 双精度类型返回
	 * @param map
	 * @param key
	 * @return
	 */
	public static <K,V> Double getDouble(Map<K,V> map,K key){
		V value=map.get(key);
		if(StringUtils.isNotNull(value)){
			if(value instanceof Double){
				return (Double)value;
			}else if(value instanceof Number){
				return ((Number) value).doubleValue();
			}else{
				return Double.valueOf(String.valueOf(value));
			}
		}
		return null;
	}
	/**
	 * 以long类型返回
	 * @param map
	 * @param key
	 * @return
	 */
	public static <K,V> Long getLong(Map<K,V> map,K key){
		V value=map.get(key);
		if(StringUtils.isNotNull(value)){
			if(value instanceof Long){
				return (Long)value;
			}else if(value instanceof Number){
				return ((Number) value).longValue();
			}else{
				return Long.valueOf(String.valueOf(value));
			}
		}
		return null;
	}
	/**
	 * 以字符串类型返回
	 * @param map
	 * @param key
	 * @return
	 */
	public static <K,V> String getString(Map<K,V> map,K key){
		V value=map.get(key);
		if(value !=null ){
			if(value instanceof String){
				return (String)value;
			}else{
				return String.valueOf(value);
			}
		}
		return null;
	}
	/**
	 * 指定一个目标类型，返回值会向此目标类型进行转换
	 * @param map
	 * @param key
	 * @param returnType 返回值目标类型
	 * @return
	 */
	public static <K,V,T> T getObject(Map<K,V> map,K key,Class<T> returnType){
		V value=map.get(key);
		if(value !=null ){
			try {
				return CONVERTER.convert(returnType, value);
			} catch (ConvertException e) {
				throw new SmileRunException("key:"+key+"-->value:"+value,e);
			}
		}
		return null;
	}
	
	/**
	 * 构建一个map从父map中继承内容 
	 *  但修改时不影响父map的值
	 * @param parentMap
	 * @return 一个HashMap
	 */
	public static <K,V> Map<K,V> extendsMap(Map <K,V> parentMap){
		return new ExtendsMap<K, V>(parentMap);
	}
	/**
	 * 连接两个map 把后面的map中的内容全添加到前map中
	 * @param <K>
	 * @param <V>
	 * @param toMap
	 * @param m
	 * @return
	 */
	public static <K,V> Map<K,V> concatMap(Map<K,V> toMap,Map<K,V> m){
		toMap.putAll(m);
		return toMap;
	}
	
	/**创建一个map*/
	public static <K, V> Map<K, V> hashMap(K key, V value) {
		Map<K, V> map = new HashMap<K, V>();
		map.put(key, value);
		return map;
	}
	
	
	/***
	 * @param key  
	 * @param value
	 * @return
	 */
	public static <K, V> Map<K, V> linkedHashMap(K key, V value) {
		Map<K, V> map = new LinkedHashMap<K, V>();
		map.put(key, value);
		return map;
	}
	
	/**
	 * 对一个集合分组成Map
	 * @param list
	 * @param groupKey 分组的关键规则
	 * @return
	 */
	public static <K, T> Map<K, T> keymap(Collection<T> list, GroupKey<K, T> groupKey) {
		Map<K, T> groupMap = new LinkedHashMap<K, T>();
		return keymap(list, groupKey, groupMap);
	}
	
	/**
	 * 把list转成一个map
	 * @param list
	 * @param groupKey 生成key的规则
	 * @param groupMap
	 * @return
	 */
	public static <K,T> Map<K,T> keymap(Collection<T> list, GroupKey<K, T> groupKey,Map<K, T> groupMap){
		for (T value : list) {
			K key = groupKey.getKey(value);
			groupMap.put(key, value);
		}
		return groupMap;
	}

	/**
	 * 对一个集合分组成Map
	 * @param list
	 * @param groupKey 分组的关键规则
	 * @return
	 */
	public static <K, T> Map<String, T> nocaseKeyMap(Collection<T> list, GroupKey<String, T> groupKey) {
		Map<String, T> groupMap = new KeyNoCaseHashMap<T>();
		for (T value : list) {
			String key = groupKey.getKey(value);
			groupMap.put(key, value);
		}
		return groupMap;
	}
	/**
	 * 把map连接成字符串
	 * key1:value2;key2:value2
	 * @param map
	 * @return
	 */
	public static <K, V> String joinMapToString(Map<K, V> map) {
		if (isEmpty(map)) {
			return Strings.BLANK;
		}
		char seperator = ';';
		StringBuilder strBuilder = new StringBuilder();
		for (Map.Entry<K, V> entry : map.entrySet()) {
			strBuilder.append(seperator).append(entry.getKey()).append(':').append(entry.getValue());
		}
		return strBuilder.substring(1);

	}
	
	/***
	 * 新建一个map
	 * @param keks
	 * @param vas
	 * @return
	 */
	public static <K, T> Map<K, T> linkedHashMap(K[] keks, T[] vas) {
		Map<K, T> map = new LinkedHashMap<K, T>(keks.length);
		for (int i = 0; i < keks.length; i++) {
			map.put(keks[i], vas[i]);
		}
		return map;
	}
	
	/**
	 * 是否为空对象  或内容为空
	 * @param map
	 * @return
	 */
	public static <K, V> boolean isEmpty(Map<K, V> map) {
		return map == null || map.isEmpty();
	}
	
	/**
	 * 不为空
	 * @param map
	 * @return
	 */
	public static <K, V> boolean notEmpty(Map<K, V> map) {
		return map!=null&&map.size()>0;
	}
	
	/**
	 * 是否存在指定的key
	 * @param map
	 * @param key
	 * @return
	 */
	public static  <K, V>  boolean containsKey(Map<K, V> map ,Object key){
		return map==null?false:map.containsKey(key);
	}
	
	
	/**
	 * 移个一个map中的多个键
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @param keys
	 */
	public static <K,V> void removeKeys(Map<K,V> map,K[] keys) {
		for(K k:keys) {
			map.remove(k);
		}
	}
	/**
	 * 删除一个map中的多个key
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @param keys
	 */
	public static <K,V> void removeKeys(Map<K,V> map,Iterable<K> keys) {
		for(K k:keys) {
			map.remove(k);
		}
	}
	/**
	 * 只要存在一个就可以
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @param keys
	 */
	public static <K,V> boolean hasAnyKey(Map<K,V> map,Object[] keys) {
		for(int i=0;i<keys.length;i++) {
			if(map.containsKey(keys[i])) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 包含所有的
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @param keys
	 * @return
	 */
	public static <K,V> boolean hasEveryKeys(Map<K,V> map,Object[] keys) {
		for(int i=0;i<keys.length;i++) {
			if(!map.containsKey(keys[i])) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 装饰一个不可修改的map
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @return
	 */
	public static <K,V> UnmodifiableMap<K,V> unmodifiableMap(Map<K,V> map) {
		return new UnmodifiableMap<K,V>(map);
	}
	/**
	 * 	从map中获取一个字符串 解析成json对象返回
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @param key
	 * @return
	 */
	public static <K,V> JSONObject getJSONObject(Map<K,V> map,K key) {
		Object value=map.get(key);
		if(value==null) {
			return null;
		}
		if(value instanceof JSONObject) {
			return (JSONObject)value;
		}else if(value instanceof String) {
			try {
				return new JSONObject((String)value);
			} catch (JSONParseException e) {
				throw new SmileRunException(e);
			}
		}
		throw new SmileRunException("not a json objct string");
	}
	/**
	 *	获取一个json array 字符串并解析  解析json  array
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @param key
	 * @return
	 */
	public static <K,V> JSONArray getJSONArray(Map<K,V> map,K key) {
		Object value=map.get(key);
		if(value==null) {
			return null;
		}
		if(value instanceof JSONArray) {
			return (JSONArray)value;
		}else if(value instanceof String) {
			try {
				return new JSONArray((String)value);
			} catch (JSONParseException e) {
				throw new SmileRunException(e);
			}
		}
		throw new SmileRunException("not a json array string");
	}
	
	/**
	 * 构建一个ResultMap的实例
	 * @return
	 */
	public static ResultMap resultMap() {
		return new HashResultMap();
	}
	/**
	 * 对一个map进行装饰
	 * @param map
	 * @return
	 */
	public static ResultMap resultMap(Map<String,Object> map) {
		return new DecoratedResultMap(map);
	}
	/**
	 * 用于封装key为String类型的map
	 * @author 胡真山
	 *
	 */
	private static class HashResultMap extends org.smile.collection.LinkedHashMap<String, Object> implements ResultMap{
		//a  base instance 
	}

	/**
	 * 从map中获取value以BigDecimal类型返回
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @param key
	 * @return
	 */
	public static <K,V> BigDecimal getBigDecimal(Map<K, V> map, K key) {
		V value=map.get(key);
		try {
			return BigDecimalConverter.instance.convert(value);
		} catch (ConvertException e) {
			throw new SmileRunException(e);
		}
	}

}
