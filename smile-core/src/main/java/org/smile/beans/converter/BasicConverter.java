package org.smile.beans.converter;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.smile.beans.BeanUtils;
import org.smile.beans.converter.type.BigDecimalArrayConverter;
import org.smile.beans.converter.type.BigDecimalConverter;
import org.smile.beans.converter.type.BigIntConverter;
import org.smile.beans.converter.type.BooleanArrayConverter;
import org.smile.beans.converter.type.BooleanConverter;
import org.smile.beans.converter.type.ByteArrayConverter;
import org.smile.beans.converter.type.ByteConverter;
import org.smile.beans.converter.type.CharacterConverter;
import org.smile.beans.converter.type.ClassConverter;
import org.smile.beans.converter.type.DateConverter;
import org.smile.beans.converter.type.DateTimeConverter;
import org.smile.beans.converter.type.DoubleArrayConverter;
import org.smile.beans.converter.type.DoubleConverter;
import org.smile.beans.converter.type.FileConverter;
import org.smile.beans.converter.type.FloatArrayConverter;
import org.smile.beans.converter.type.FloatConverter;
import org.smile.beans.converter.type.IntArrayConverter;
import org.smile.beans.converter.type.IntegerConverter;
import org.smile.beans.converter.type.IntervalConverter;
import org.smile.beans.converter.type.LocaleConverter;
import org.smile.beans.converter.type.LongArrayConverter;
import org.smile.beans.converter.type.LongConverter;
import org.smile.beans.converter.type.ShortArrayConverter;
import org.smile.beans.converter.type.ShortConverter;
import org.smile.beans.converter.type.SqlDateConverter;
import org.smile.beans.converter.type.StringArrayConverter;
import org.smile.beans.converter.type.StringConverter;
import org.smile.beans.converter.type.TimeConverter;
import org.smile.beans.converter.type.TimestampConverter;
import org.smile.beans.converter.type.URLConverter;
import org.smile.collection.WrapBeanAsMap;
import org.smile.json.JSON;
import org.smile.reflect.ClassTypeUtils;
import org.smile.reflect.Generic;
import org.smile.util.ObjectLenUtils;
import org.smile.util.RegExp;
import org.smile.util.StringUtils;

/**
 * 一个基本的转换器实现 ，基本包括所有的数据类型转换
 *   实现在基础数据 和date  list 几种类型的转换
 *   以及 list 的基础数据类型的泛型转换
 *   例如: bean 中是double类型  都会把map中的值向double转换 如不能解析则出异常
 *        bean 中是list 都会把map中的值转成list 如map 中是单个值就会把这个值加入到list中 
 *             如也是list直接转 ,是数据组则把数据转成list
 *        当bean中属性有泛型的时候，
 *        		当前只是实现了list的泛型 和 set 的泛型
 *                如map中是list 则会把map中的list的每一个object都向泛型类型转换
 *              并且只支持基础类型 日期 字符串 泛型                    
 * @author 胡真山
 *
 */
public class BasicConverter extends AbstractConverter {
	
	private static BasicConverter instance=null;
	
	public static Converter getInstance(){
		if(instance==null){
			instance=new BasicConverter();
		}
		return instance;
	}
	
	/**注册默认转换*/
	protected void regsiterDefaultConverter(){
		regsiterTypeConverter(new ByteConverter());
		regsiterTypeConverter(new CharacterConverter());
		regsiterTypeConverter(new DateConverter());
		regsiterTypeConverter(new SqlDateConverter());
		regsiterTypeConverter(new DoubleConverter());
		regsiterTypeConverter(new FloatConverter());
		regsiterTypeConverter(new IntegerConverter());
		regsiterTypeConverter(new LongConverter());
		regsiterTypeConverter(new ShortConverter());
		regsiterTypeConverter(new StringConverter());
		regsiterTypeConverter(new BigDecimalConverter());
		regsiterTypeConverter(new BigIntConverter());
		regsiterTypeConverter(new BooleanConverter());
		regsiterTypeConverter(new ByteArrayConverter());
		regsiterTypeConverter(new DoubleArrayConverter());
		regsiterTypeConverter(new FloatArrayConverter());
		regsiterTypeConverter(new IntArrayConverter());
		regsiterTypeConverter(new LongArrayConverter());
		regsiterTypeConverter(new ShortArrayConverter());
		regsiterTypeConverter(new StringArrayConverter());
		regsiterTypeConverter(new BigDecimalArrayConverter());
		regsiterTypeConverter(new BooleanArrayConverter());
		regsiterTypeConverter(new ClassConverter());
		regsiterTypeConverter(new TimestampConverter());
		regsiterTypeConverter(new TimeConverter());
		regsiterTypeConverter(new DateTimeConverter());
		regsiterTypeConverter(new URLConverter());
		regsiterTypeConverter(new FileConverter());
		regsiterTypeConverter(new IntervalConverter());
		regsiterTypeConverter(new LocaleConverter());
		this.typeConvertMap.put(Map.class, new AbstractTypeConverter<Map>(){
			@Override
			public Map convert(Generic generic, Object value) throws ConvertException {
				return covertToMap(HashMap.class,generic, value);
			}
		});
		this.typeConvertMap.put(Set.class, new AbstractTypeConverter<Set>(){
			@Override
			public Set convert(Generic generic, Object value) throws ConvertException {
				return converToSet(HashSet.class,generic, value);
			}
		});
		this.typeConvertMap.put(List.class, new AbstractTypeConverter<List>(){
			@Override
			public List convert(Generic generic, Object value) throws ConvertException {
				return converToList(LinkedList.class,generic, value);
			}
		});
	}
	/**
	 * 日期与字符串之间的转换方法 是字符串就转成日期 是日期就转成字符串
	 * 
	 * @param value
	 *            一个要转换的值 可以是日期格式的字符串 或 日期
	 * @return
	 * @throws Exception 
	 */
	@Override
	public Object convert(Class type,Generic generic, Object value) throws ConvertException {
		//基础类型
		if(ClassTypeUtils.isBasicType(type)){
			return convertBasicType(type,value);
		}else if(ClassTypeUtils.isBasicArrayType(type)){
			//基础数组类型
			return convertBasicArrayType(type,value);
		}else{
			//为空
			if(value==null){
				return value;
			}else if(generic==null&&type.isAssignableFrom(value.getClass())){
				//类型相同时
				return value;
			}
			TypeConverter tc=getTypeConverter(type);
			if(tc!=null){
				//注册了typeconverter的类型
				return tc.convert(generic, value);
			}else if(List.class.isAssignableFrom(type)){
				return BasicConverter.this.converToList(type,generic, value);
			}else if(Set.class.isAssignableFrom(type)){
				return BasicConverter.this.converToSet(type,generic, value);
			}else if(Map.class.isAssignableFrom(type)){
				return BasicConverter.this.covertToMap(type,generic, value);
			}else if(Enum.class.isAssignableFrom(type)){
				return Enum.valueOf(type, String.valueOf(value));
			}else if(type.isAssignableFrom(value.getClass())){
				return value;
			}else{
				//转化目标类型是没有长度的类型 只取源对象的第一个
				if(ObjectLenUtils.hasLength(value)){
					value=ObjectLenUtils.get(value,0);
				}
				if(value instanceof Map){
					Object targetBean;
					try {
						targetBean = type.newInstance();
						BeanUtils.mapToBean((Map)value, targetBean, this);
						return targetBean;
					} catch (Exception e) {
						throw new ConvertException(e);
					}
				}else if(value instanceof String){
					String jsonStr=(String)value;
					if(needParseJsonObject(jsonStr)){
						return JSON.parseJSONObject(jsonStr,type);
					}
				}
				throw new ConvertException("不支持的目标转换类型:"+type.getName()+"源类型,目标类型"+value.getClass().getName());
			}
		}
	}
	
	/**
	 * 是否需要解析json对象
	 * @param jsonStr
	 * @return
	 */
	protected boolean needParseJsonObject(String jsonStr){
		if(jsonStr.charAt(0)=='{'&& jsonStr.charAt(jsonStr.length()-1)=='}'){
			return true;
		}
		return false;
	}
	/***
	 * 是否需要解析json列表
	 * @param jsonStr
	 * @return
	 */
	protected boolean needParseJsonArray(String jsonStr){
		if(jsonStr.charAt(0)=='['&& jsonStr.charAt(jsonStr.length()-1)==']'){
			return true;
		}
		return false;
	}
	/**
	 * 转成map
	 * @param generic
	 * @param value
	 * @return
	 */
	protected Map covertToMap(Class<? extends Map> mapClass,Generic generic, Object value) throws ConvertException {
		if(value instanceof String){//对json字符串转map进行支持
			String jsonStr=(String)value;
			if(needParseJsonObject(jsonStr)){
				value=JSON.parseJSONObject(jsonStr);
			}
		}
		if(Generic.isEmpty(generic)){
			if(value instanceof Map){//原对象是map类型的时候
				if(mapClass.isAssignableFrom(value.getClass())){
					return (Map)value;
				}else{
					try {
						Map resultMap=mapClass.newInstance();
						resultMap.putAll((Map)value);
						return resultMap;
					} catch (Exception e) {
						throw new ConvertException("从"+value.getClass().getName()+"转到"+mapClass+"出错",e);
					}
				}
			}else{
				try {
					Map resultMap;
					if(mapClass!=Map.class){
						resultMap=mapClass.newInstance();
					}else{
						resultMap=new HashMap();
					}
					BeanUtils.beanToMap(value, resultMap, false);
					return resultMap;
				} catch (Exception e) {
					throw new ConvertException("从"+value.getClass().getName()+"转到map出错",e);
				}
			}
		}else{
			Map map;
			try {
				map = mapClass.newInstance();
			} catch (Exception e) {
				throw new ConvertException(e);
			}
			if(!(value instanceof Map)){
				value=new WrapBeanAsMap(value);
			}
			for(Map.Entry entry:(Set<Map.Entry>)((Map)value).entrySet()){
				Object key=this.convert(generic.values[0],generic.sub(0),entry.getKey());
				Object val=this.convert(generic.values[1],generic.sub(1),entry.getValue());
				map.put(key, val);
			}
			return map;
		}
	}
	/***
	 * 装换成基础数据类型数组
	 * @param type
	 * @param value
	 * @return
	 * @throws ConvertException
	 */
	public Object convertBasicArrayType(Class type,Object value) throws ConvertException{
		if(value==null){
			return null;
		}
		return BaseArrayTypeConverter.getInstance().convert(type, value);
	}
	/***
	 * 基础数据类型转换
	 * @param type
	 * @param value
	 * @return
	 * @throws ConvertException
	 */
	public Object convertBasicType(Class type,Object value) throws ConvertException{
		if(StringUtils.isNull(value)){
			return ClassTypeUtils.basicNullDefault(type);
		}else{
			return BaseTypeConverter.getInstance().convert(type, value);
		}
	}
	/**
	 * 把一个值转换成一个set集合
	 * 如果是数组就把数组每一个添加到set并进行泛型转换
	 * 如果 是没有长度的对象 ，则把这个对象添加到 set中 即最得到的set只有一个长度
	 * @param generic
	 * @param value
	 * @return
	 * @throws ConvertException
	 */
	public Set converToSet(Class<? extends Set> setClass,Generic generic, Object value) throws ConvertException {
		//为空时,不做转换
		if(value==null){
			return null;
		}
		Set<Object> set;
		try {
			set = setClass.newInstance();
		} catch (Exception e) {
			throw new ConvertException(e);
		}
		if(value instanceof String){
			value=stringToList((String)value);
		}
		convertToCollection(set, generic, value);
		return set;
	}
	/***
	 * 对字符串转成list
	 * @param value
	 * @return
	 */
	protected Collection stringToList(String value){
		if(needParseJsonArray(value)){
			return JSON.parseJSONArray(value);
		}
		return RegExp.DEF_STR_SPLIT.splitAndTrimNoBlack(value);
	}
	/**
	 * 转换到列表中
	 * @param set
	 * @param generic
	 * @param value
	 * @throws ConvertException
	 */
	protected void convertToCollection(Collection set,Generic generic, Object value) throws ConvertException{
		if(value instanceof Collection){
			Collection collection=(Collection)value;
			if(Generic.isEmpty(generic)){
				for(Iterator iter=collection.iterator();iter.hasNext();){
					set.add(iter.next());
				}
			}else {
				for(Iterator iter=collection.iterator();iter.hasNext();){
					set.add(this.convert(generic.values[0], generic.sub(0), iter.next()));
				}
			}
		}else if(value instanceof Object[]){
			Object[] objs=(Object[])value;
			for(Object o:objs){
				if(Generic.isEmpty(generic)){
					set.add(o);
				}else{
					set.add(this.convert(generic.values[0], generic.sub(0), o));
				}
			}
		}else if(ObjectLenUtils.hasLength(value)){
			int len=ObjectLenUtils.len(value);
			for(int i=0;i<len;i++){
				set.add(ObjectLenUtils.get(value, i));
			}
		}else{
			if(Generic.isEmpty(generic)){
				set.add(value);
			}else{
				set.add(this.convert(generic.values[0], generic.sub(0), value));
			}
		}
	}
	/**
	 * @param value
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List converToList(Class<? extends List> listClass,Generic generic,Object value) throws ConvertException{
		//为空时,不做转换
		if(value==null){
			return null;
		}
		List<Object> list;
		try {
			list = listClass.newInstance();
		}catch (Exception e) {
			throw new ConvertException(e);
		}
		if(value instanceof String){
			value=stringToList((String)value);
		}
		convertToCollection(list, generic, value);
		return list;
	}
}
