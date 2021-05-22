package org.smile.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.smile.beans.converter.BasicConverter;
import org.smile.beans.converter.ConvertException;
import org.smile.collection.ArrayUtils;
import org.smile.collection.KeyNoCaseHashMap;
import org.smile.commons.Chars;
import org.smile.commons.SmileRunException;
import org.smile.log.LoggerHandler;
/**
 * 字段操作的工具类
 * @author 胡真山
 * 2015年12月11日
 */
public class FieldUtils implements LoggerHandler{
	/**
	 * 不区分大小写的字段名为key的static 字段集合
	 * @param clazz
	 * @return
	 */
	public static Map<String,Field> getNoCaseNameStaticFields(Class clazz){
		Map<String,Field> fieldMap=new KeyNoCaseHashMap<Field>();
		Field[] fs=clazz.getDeclaredFields();
		for(Field f:fs){
			if(Modifier.isStatic(f.getModifiers())){
				fieldMap.put(f.getName(), f);
			}
		}
		fs=clazz.getFields();
		for(Field f:fs){
			if(!fieldMap.containsKey(f.getModifiers())){
				if(Modifier.isStatic(f.getModifiers())){
					fieldMap.put(f.getName(), f);
				}
			}
		}
		return fieldMap;
	}
	
	/**
	 * 不区分大小写名称的定义方法
	 * @param clazz 类
	 * @return
	 */
	public static Map<String,Field> getNoCaseNameDeclaredFields(Class clazz){
		Map<String,Field> fieldMap=new KeyNoCaseHashMap<Field>();
		Field[] fs=clazz.getDeclaredFields();
		for(Field f:fs){
			fieldMap.put(f.getName(), f);
		}
		return fieldMap;
	}
	/**
	 * 非静态的字段
	 * @param clazz
	 * @return
	 */
	public static Map<String,Field> getNoStaticField(Class clazz){
		Map<String,Field> result=new LinkedHashMap<String,Field>();
		Field[] fs=clazz.getDeclaredFields();
		for(Field f:fs){
			if(!Modifier.isStatic(f.getModifiers())){
				result.put(f.getName(), f);
			}
		}
		return result;
	}
	/***
	 * 包括父类的非静态的字段
	 * 一直递归父类的定义的字段
	 * @param clazz 
	 * @return
	 */
	public static Map<String,Field> getAnyNoStaticField(Class clazz){
		Map<String,Field> result=new LinkedHashMap<String,Field>();
		do{
			if(clazz==null) {
				break;
			}
			Field[] fs=clazz.getDeclaredFields();
			for(Field f:fs){
				if(!Modifier.isStatic(f.getModifiers())){
					if(!result.containsKey(f.getName())){
						result.put(f.getName(), f);
					}
				}
			}
		}while((clazz=clazz.getSuperclass())!=Object.class);
		return result;
	}
	/**
	 * 获取所有的字段  一直查找父类
	 * @param clazz
	 * @return
	 */
	public static List<Field> getAnyField(Class clazz){
		List<Field> result=new LinkedList<Field>();
		do{
			Field[] fs=clazz.getDeclaredFields();
			for(Field f:fs){
				result.add(f);
			}
		}while((clazz=clazz.getSuperclass())!=Object.class);
		return result;
	}
	/**
	 * 不区分大小写名称的公有的方法
	 * @param clazz
	 * @return
	 */
	public static Map<String,Field> getNoCaseNamePublicFields(Class clazz){
		Map<String,Field> fieldMap=new KeyNoCaseHashMap<Field>();
		Field[] fs=clazz.getFields();
		for(Field f:fs){
			fieldMap.put(f.getName(), f);
		}
		return fieldMap;
	}
	/**
	 * 不区分大小写的字段名为key的字段集合
	 * @param clazz
	 * @return
	 */
	public static Map<String,Field> getNoCaseNameFields(Class clazz){
		Map<String,Field> fieldMap=new KeyNoCaseHashMap<Field>();
		Field[] fs=clazz.getDeclaredFields();
		for(Field f:fs){
			fieldMap.put(f.getName(), f);
		}
		fs=clazz.getFields();
		for(Field f:fs){
			fieldMap.put(f.getName(), f);
		}
		return fieldMap;
	}
	
	/**
	 * 为static field 设置值
	 * @param f 
	 * @param value
	 * @throws ConvertException
	 */
	public static void setStaticFieldValue(Field f,Object value) throws ConvertException{
		Object v=BasicConverter.getInstance().convert(f.getType(), ClassTypeUtils.getGenericObj(f.getType()), value);
		try {
			f.set(null, v);
		} catch (Exception e) {
			throw new SmileRunException("set static field value error "+f+" value:"+v,e);
		}
	}
	/**
	 * 设置静态字段值  
	 * @param exp 可以是带.表达式
	 * @param value
	 */
	public static void setStaticExpFieldValue(Class clazz,String exp,Object value){
		int index=exp.indexOf(Chars.DOT);
		if(index>0){
			String name=exp.substring(0,index);
			Field f=getField(clazz, name);
			if(f!=null){
				Class subClass=f.getType();
				setStaticExpFieldValue(subClass,exp.substring(index+1), value);
			}
		}else{
			try{
				setStaticFieldValue(getField(clazz, exp), value);
			}catch(Exception e){
				logger.error("设置"+clazz +"的属性"+exp+" 值:"+value+"出错",e);
			}
		}
	}
	/**
	 * 为一个对象的字段赋值
	 * @param f 字段
	 * @param target 目标对象
	 * @param value 值
	 * @throws ConvertException 数据转换的可能会转换出异常
	 */
	public static void setFieldValue(Field f,Object target,Object value) throws ConvertException{
		Class t=f.getType();
		Object v=BasicConverter.getInstance().convert(t, ClassTypeUtils.getGenericObj(t), value);
		try {
			f.set(target, v);
		} catch (Exception e) {
			throw new SmileRunException("set field value error "+f+" value:"+v,e);
		}
	}
	/**
	 * 获取静态字段的值
	 * @param targetClass 目标类
	 * @param fieldName 静态字段名称
	 * @throws NoSuchFieldException
	 */
	public static Object getStaticFieldValue(Class targetClass,String fieldName) throws NoSuchFieldException{
		Field filed=getField(targetClass, fieldName);
		if(filed==null||!Modifier.isStatic(filed.getModifiers())){
			throw new NoSuchFieldException("no such filed named "+fieldName +" in class "+targetClass);
		}
		try {
			return filed.get(null);
		} catch (Exception e) {
			throw new SmileRunException("access "+targetClass+" static filed "+fieldName, e);
		}
	}
	
	/**
	 * 自定义的field 和 public field
	 * @return
	 */
	public static Field getField(Class clazz,String name){
		Field f=null;
		try {
			f=clazz.getDeclaredField(name);
			f.setAccessible(true);
		} catch (NoSuchFieldException e){
			if(clazz!=Object.class){
				try{
					if(clazz.isInterface()){
						return clazz.getField(name);
					}else{
						Class[] parents=clazz.getInterfaces();
						if(ArrayUtils.notEmpty(parents)){
							for(Class p:parents){
								f=p.getField(name);
								if(f!=null){
									return f;
								}
							}
						}
						return getField(clazz.getSuperclass(), name);
					}
				}catch(NoSuchFieldException ne){
					return null;
				}
			}
		}
		return f;
	}
	
}
