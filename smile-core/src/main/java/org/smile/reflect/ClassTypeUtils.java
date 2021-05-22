package org.smile.reflect;

import java.beans.PropertyDescriptor;
import java.io.Closeable;
import java.io.Externalizable;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.smile.beans.BeanInfo;
import org.smile.beans.converter.BeanException;
import org.smile.collection.CollectionUtils;
import org.smile.collection.KeyNoCaseHashMap;
import org.smile.commons.SmileRunException;
import org.smile.log.LoggerHandler;
import org.smile.util.StringUtils;

/**
 * 对 class 操作的一些方法
 * @author 胡真山
 *
 */
public class ClassTypeUtils implements LoggerHandler{
	/**基本数据类型*/
	private static final Map<Class,Object> basiceTypeMap=new HashMap<Class,Object>();
	/**基本数据类型数组*/
	private static final Map<Class,Object> basiceArrayTypeMap=new HashMap<Class,Object>();
	/**基本数据类型名称*/
	private static final Map<String,Object> basicTypeNames=new HashMap<String,Object>();
	/**基本类型名称与类映射*/
	private static KeyNoCaseHashMap<Class> basicTypeClass=new KeyNoCaseHashMap<Class>();
	/*** 常用 的map类型配置名称*/
	private static Set<String> MAP_TYPE=CollectionUtils.hashSet("map","java.util.Map","Map");
	/**常用 的string类型配置名称*/
	private static Set<String> STRING_TYPE=CollectionUtils.hashSet("string","java.lang.String","String");
	/** 常用的date 类型配置名称*/
	private static Set<String> DATE_TYPE=CollectionUtils.hashSet("date","java.util.Date","Date");
	/**常用接口默认的实现类*/
	private static Map<Class,Class> interfaceDefaultClass=new HashMap<Class, Class>();
	/**基础类型封装*/
	private static final Map<Class,Object> basicObjectTypeMap=new HashMap<Class, Object>();
	/**基本类型数组*/
	private static final Map<Class,Object> basicObjectArrayTypeMap=new HashMap<Class, Object>();
	/**
	 * 基本类型与基本封装类型映射
	 */
	private static final Map<Class,Class> basicTypeBasicObjTypeMap=new HashMap<Class,Class>();
	
	private static final Set<Class> javaLanguageInterfaces =new HashSet<>();
	
	static{
		//基本类型名称与默认值
		basiceTypeMap.put(int.class, 0);
		basiceTypeMap.put(short.class, (short)0);
		basiceTypeMap.put(long.class, 0l);
		basiceTypeMap.put(byte.class, (byte)0);
		basiceTypeMap.put(boolean.class,false );
		basiceTypeMap.put(float.class, 0f);
		basiceTypeMap.put(double.class, 0d);
		basiceTypeMap.put(char.class, (char)0);
		//名称与默认值
		basicTypeNames.put("int", 0);
		basicTypeNames.put("short", (short)0);
		basicTypeNames.put("long", 0l);
		basicTypeNames.put("byte", (byte)0);
		basicTypeNames.put("boolean",false );
		basicTypeNames.put("float", 0f);
		basicTypeNames.put("double", 0d);
		basicTypeNames.put("char", (char)0);
		//基础类型封闭类默认值
		basicObjectTypeMap.put(Integer.class, 0);
		basicObjectTypeMap.put(Short.class, (short)0);
		basicObjectTypeMap.put(Long.class, 0l);
		basicObjectTypeMap.put(Byte.class, (byte)0);
		basicObjectTypeMap.put(Boolean.class,false );
		basicObjectTypeMap.put(Float.class, 0f);
		basicObjectTypeMap.put(Double.class, 0d);
		basicObjectTypeMap.put(Character.class, (char)0);
		///基础类型封闭类数组
		basicObjectArrayTypeMap.put(Integer[].class, 0);
		basicObjectArrayTypeMap.put(Short[].class, (short)0);
		basicObjectArrayTypeMap.put(Long[].class, 0l);
		basicObjectArrayTypeMap.put(Byte[].class, (byte)0);
		basicObjectArrayTypeMap.put(Boolean[].class,false );
		basicObjectArrayTypeMap.put(Float[].class, 0f);
		basicObjectArrayTypeMap.put(Double[].class, 0d);
		basicObjectArrayTypeMap.put(Character[].class, (char)0);
		
		//基本数据类型的类名
		basicTypeClass.put("int", int.class);
		basicTypeClass.put("short",short.class);
		basicTypeClass.put("long", long.class);
		basicTypeClass.put("byte", byte.class);
		basicTypeClass.put("boolean",boolean.class );
		basicTypeClass.put("float", float.class);
		basicTypeClass.put("double", double.class);
		basicTypeClass.put("char", char.class);
		//基本数据类型数组
		basiceArrayTypeMap.put(int[].class, 0);
		basiceArrayTypeMap.put(short[].class, (short)0);
		basiceArrayTypeMap.put(long[].class, 0l);
		basiceArrayTypeMap.put(byte[].class, (byte)0);
		basiceArrayTypeMap.put(boolean[].class,false );
		basiceArrayTypeMap.put(float[].class, 0f);
		basiceArrayTypeMap.put(double[].class, 0d);
		basiceArrayTypeMap.put(char[].class, (char)0);
		//集合默认初始化类
		interfaceDefaultClass.put(Map.class, HashMap.class);
		interfaceDefaultClass.put(List.class, LinkedList.class);
		interfaceDefaultClass.put(Collection.class, LinkedList.class);
		interfaceDefaultClass.put(Set.class, LinkedHashSet.class);
		//基本类型与封装类型对应
		basicTypeBasicObjTypeMap.put(int.class, Integer.class);
		basicTypeBasicObjTypeMap.put(Integer.class, int.class);
		basicTypeBasicObjTypeMap.put(short.class, Short.class);
		basicTypeBasicObjTypeMap.put(Short.class, short.class);
		basicTypeBasicObjTypeMap.put(byte.class, Byte.class);
		basicTypeBasicObjTypeMap.put(Byte.class, byte.class);
		basicTypeBasicObjTypeMap.put(long.class, Long.class);
		basicTypeBasicObjTypeMap.put(Long.class, long.class);
		basicTypeBasicObjTypeMap.put(float.class, Float.class);
		basicTypeBasicObjTypeMap.put(Float.class, float.class);
		basicTypeBasicObjTypeMap.put(double.class, Double.class);
		basicTypeBasicObjTypeMap.put(Double.class, double.class);
		basicTypeBasicObjTypeMap.put(boolean.class, Boolean.class);
		basicTypeBasicObjTypeMap.put(Boolean.class, boolean.class);
		basicTypeBasicObjTypeMap.put(void.class, Void.class);
		basicTypeBasicObjTypeMap.put(Void.class, void.class);
		
		CollectionUtils.addItem(javaLanguageInterfaces,Serializable.class, Externalizable.class,
				Closeable.class, AutoCloseable.class, Cloneable.class, Comparable.class);
	}
	/**
	 * 判断是否是基础数据类型
	 * @param type
	 * @return
	 */
	public static boolean isBasicType(Class type){
		return basiceTypeMap.keySet().contains(type);
	}
	/**
	 * 是不是是基础类型
	 * byte int ...
	 * @param type 基础类型名称
	 * @return
	 */
	public static boolean isBasicType(String type){
		return basicTypeNames.containsKey(type);
	}
	/**
	 * 基本类型的封装类型
	 * 
	 * int -> Integer
	 * @param basicClass
	 * @return
	 */
	public static Class getBasicTypeObjectClass(Class basicClass){
		Object basicObj=basiceTypeMap.get(basicClass);
		if(basicObj!=null){
			return basicObj.getClass();
		}
		return null;
	}
	/**
	 * 基础类型名称获取类
	 * @param name
	 * @return
	 */
	public static Class getBasicTypeClass(String name){
		return basicTypeClass.get(name);
	}
	/**
	 * 是不是基础类型数组
	 * @param type
	 * @return
	 */
	public static boolean isBasicArrayType(Class type){
		return basiceArrayTypeMap.keySet().contains(type);
	}
	/**
	 * 基础类型默认值
	 * @param fieldType
	 * @return
	 */
	public static <E> E basicNullDefault(Class<E> fieldType){
		return (E)basiceTypeMap.get(fieldType);
	}
	/**
	 * 是不是是基础类型的封装
	 * @param type
	 * @return
	 */
	public static boolean isBasicObjType(Class type){
		return basicObjectTypeMap.containsKey(type);
	}
	/***
	 * 获取泛型
	 * @param prop
	 * @return
	 */
	public static Class[] getGeneric(PropertyDescriptor prop){
		//泛型
		Class[] generic=null;
		try{
			return getGeneric(getGenericType(prop));
		}catch(Exception e){
			logger.debug(e);
		}
		return generic;
	}
	
	/**
	 * 从bean的property中获取此属性的泛型
	 * @param prop
	 * @return
	 */
	public static Type getGenericType(PropertyDescriptor prop){
		Type type=null;
		Method method=prop.getReadMethod();
		if(method!=null){
			type=method.getGenericReturnType();
		}else{
			method=prop.getWriteMethod();
			if(method!=null){
				type=method.getGenericParameterTypes()[0];
			}
		}
		return type;
	}
	/**
	 * 转成class[] 数组
	 * @param type
	 * @return
	 */
	public static Class castToClass(Type type){
		if(type instanceof Class){
			return (Class)type;
		}else if(type instanceof ParameterizedType){
			return (Class)((ParameterizedType)type).getRawType();
		}else if(type instanceof GenericArrayType) {
			Type arrayType=((GenericArrayType)type).getGenericComponentType();
			return castToClass(arrayType);
		}
		return null;
	}
	
	/**
	 * 得到一个字段的泛型
	 * @param field
	 * @return
	 */
	public static Class[] getGeneric(Field field){
		return getGeneric(field.getGenericType());
	}
	/**
	 * 得到一个字段的泛型
	 * @param field
	 * @return
	 */
	public static Generic getGenericObj(Type type){
		//泛型
		Generic generic=null;
		try{
			if(type instanceof ParameterizedType){
				ParameterizedType pt=(ParameterizedType)type;
				Type[] genericType=pt.getActualTypeArguments();
				generic=new Generic(genericType.length);
				for(int j=0;j<genericType.length;j++){
					Type tempType=genericType[j];
					if(tempType instanceof Class){
						generic.setIndex(j, (Class)tempType);
					}else if(tempType instanceof ParameterizedType){
						generic.setIndex(j, (ParameterizedType)tempType);
					}
				}
			}
		}catch(Exception e){
			logger.info(e);
		}
		return generic;
	}
	/**
	 * 得到一个字段的泛型
	 * @param field
	 * @return
	 */
	public static Class[] getGeneric(Type type){
		//泛型
		Class[] generic=null;
		try{
			if(ParameterizedType.class.isAssignableFrom(type.getClass())){
				ParameterizedType pt=(ParameterizedType)type;
				Type[] genericType=pt.getActualTypeArguments();
				generic=new Class[genericType.length];
				for(int j=0;j<genericType.length;j++){
					generic[j]=castToClass(genericType[j]);
					if(generic[j]==null){
						return null;
					}
				}
			}else if(GenericArrayType.class.isAssignableFrom(type.getClass())) {
				GenericArrayType gt=(GenericArrayType)type;
				Type gcType=gt.getGenericComponentType();
				return getGeneric(gcType);
			}
		}catch(Exception e){
			logger.debug(e);
		}
		return generic;
	}
	/**
	 * 获取getter方法
	 * @param clazz
	 * @param name
	 * @return
	 */
	public static Method getGetter(Class clazz ,String name){
		BeanInfo beanInfo=BeanInfo.getInstance(clazz);
		PropertyDescriptor pd=beanInfo.getPropertyDescriptor(name);
		if(pd!=null){
			Method method=pd.getReadMethod();
			if(method!=null){
				return method;
			}
		}
		throw new SmileRunException("not a getter "+name+" in "+clazz);
	}
	
	/**
	 *	 以类名获取类型
	 * @param clazzName
	 * @return
	 */
	public static Class getClassType(String clazzName){
		Class c=getBasicTypeClass(clazzName);
		if(c==null){
			try {
				return Class.forName(clazzName);
			} catch (ClassNotFoundException e) {
				throw new SmileRunException(e);
			}
		}
		return c;
	}
	/**
	 * 获取 setter方法
	 * @param clazz
	 * @param name
	 * @return
	 */
	public static Method getSetter(Class clazz ,String name){
		BeanInfo beanInfo=BeanInfo.getInstance(clazz);
		PropertyDescriptor pd=beanInfo.getPropertyDescriptor(name);
		if(pd!=null){
			Method method=pd.getWriteMethod();
			if(method!=null){
				return method;
			}
		}
		throw new SmileRunException("not a getter "+name+" in "+clazz);
	}
	/***
	 * 首字母小写名称
	 * @param clazz
	 * @return
	 */
	public static String getFirstCharLowName(Class clazz){
		String name=clazz.getSimpleName();
		return StringUtils.getFirstCharLow(name);
	}
	
	/**
	 * 获取classloader
	 * @return
	 */
	public static ClassLoader getClassLoader(){
		ClassLoader classloader=Thread.currentThread().getContextClassLoader();
		if(classloader==null){
			return ClassTypeUtils.class.getClassLoader();
		}
		return classloader;
	}
	
	/**是不可以代表map类型*/
	public static boolean isMapName(String name){
		return MAP_TYPE.contains(name);
	}
	
	/**
	 *	是否可以看成是字符串类型
	 * @param name
	 * @return
	 */
	public static boolean isStringName(String name){
		return STRING_TYPE.contains(name);
	}
	
	/**
	 * 是否可以看成日期类型
	 * @param name
	 * @return
	 */
	public static boolean isDateName(String name){
		return DATE_TYPE.contains(name);
	}
	/**
	 * 获取方法的第一个参数
	 * @param method
	 * @return
	 */
	public static Class getFirstParameter(Method method){
		Class[] classes=method.getParameterTypes();
		if(classes!=null&&classes.length>0){
			return classes[0];
		}
		return null;
	}
	
	/**
	 * 实例化一个类
	 * @param clazz 要实例化的类型
	 * @return
	 * @throws BeanException
	 */
	public static <T> T newInstance(Class<T> clazz) throws BeanException{
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			try {
				return (T)interfaceDefaultClass.get(clazz).newInstance();
			} catch (Exception e1) {
				throw new BeanException("实现化"+clazz+"出错",e);
			}
		}catch (IllegalAccessException e) {
			throw new BeanException("实现化"+clazz+"出错",e);
		}
	}
	/**
	 * 类名 实例化一个对象
	 * @param clazz
	 * @return
	 * @throws BeanException
	 */
	public static <T> T newInstance(String clazz) throws BeanException{
		try {
			return (T)newInstance(Class.forName(clazz));
		} catch (ClassNotFoundException e) {
			throw new BeanException("实例化"+clazz+"失败", e);
		}
	}
	/**
	 * 是否是基础数据类型封装对象的数组
	 * @param clazz
	 * @return
	 */
	public static boolean isBasicObjectArray(Class clazz){
		return basicObjectArrayTypeMap.containsKey(clazz);
	}
	/**
	 * 是否基本类型可以装箱对应
	 * @param class1
	 * @param class2
	 * @return
	 */
	private static boolean isBaseTypeAssignableFrom(Class class1,Class class2){
		return basicTypeBasicObjTypeMap.get(class1)==class2;
	}
	
	
	public static boolean isAssignable(Class<?> lhsType, Class<?> rhsType) {
		if (lhsType.isAssignableFrom(rhsType)) {
			return true;
		}
		if (lhsType.isPrimitive()) {
			Class<?> resolvedPrimitive = basicTypeBasicObjTypeMap.get(rhsType);
			if (lhsType == resolvedPrimitive) {
				return true;
			}
		}
		else {
			Class<?> resolvedWrapper = basicTypeBasicObjTypeMap.get(rhsType);
			if (resolvedWrapper != null && lhsType.isAssignableFrom(resolvedWrapper)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 获取构造函数
	 * @param clazz
	 * @param args
	 * @return
	 */
	public static Constructor getConstructors(Class clazz,Object[] args){
		Constructor[] constructors=clazz.getConstructors();
		List<Constructor> suitable=new LinkedList<Constructor>();
		nextConstructor:
		for(Constructor c:constructors){
			if(c.getParameterTypes().length==args.length){
				Class[] types=c.getParameterTypes();
				for(int i=0;i<types.length;i++){
					if(!types[i].isAssignableFrom(args[i].getClass())&&!isBaseTypeAssignableFrom(types[i], args[i].getClass())){
						continue nextConstructor;
					}
				}
				suitable.add(c);
			}
		}
		if(suitable.size()>=1){
			return suitable.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 所有的父接口
	 * @param type
	 * @return
	 */
	public static Class<?>[] getAllInterfaces(Class<?> type) {
		return getAllInterfacesAsSet(type).toArray(new Class[]{});
	}
	/**
	 * 获取一个类的所有接口
	 * @param type
	 * @return
	 */
	public static Set<Class> getAllInterfacesAsSet(Class type){
		if(type.isInterface()){
			return Collections.singleton(type);
		}
		Set<Class> interfaces = new HashSet<>();
		while (type != null) {
			for (Class c : type.getInterfaces()) {
				interfaces.add(c);
			}
			type = type.getSuperclass();
		}
		return interfaces;
	}
	
	/**
	 * 是否通过指定的classload可访问的类
	 */
	public static boolean isVisible(Class<?> clazz, ClassLoader classLoader) {
		if (classLoader == null) {
			return true;
		}
		try {
			Class<?> actualClass = classLoader.loadClass(clazz.getName());
			return (clazz == actualClass);
			// Else: different interface class found...
		}catch (ClassNotFoundException ex) {
			// No interface class found...
			return false;
		}
	}
	/**
	 * java一些顶级接口定义类型
	 * @param baseType
	 * @return
	 */
	public static boolean isJavaLanguageInterface(Class<?> baseType) {
		return javaLanguageInterfaces.contains(baseType);
	}
}
