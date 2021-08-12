package org.smile.beans;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.smile.beans.converter.BasicConverter;
import org.smile.beans.converter.BeanException;
import org.smile.beans.converter.ConvertException;
import org.smile.beans.converter.Converter;
import org.smile.beans.property.LikePropertyConverter;
import org.smile.beans.property.NoCasePropertyConverter;
import org.smile.beans.property.PropertyConverter;
import org.smile.commons.Chars;
import org.smile.commons.SmileRunException;
import org.smile.log.LoggerHandler;
import org.smile.reflect.ClassTypeUtils;
import org.smile.reflect.Generic;

/**
 * beanutils 处理bean的一些方法工具类
 * 
 * @author strive
 *
 */
public class BeanUtils implements LoggerHandler {
	
	/**在bean赋值时需要类型转换的时候，使用的类型器*/
	public static Converter CONVERTER =new BasicConverter();
	/**
	 * 从一个bean生成一个新的Map 属性为空时不会填充到map中
	 * @param sourceBean  源bean对象
	 * @return
	 * @throws BeanException
	 */
	public static Map<String,Object> mapFromBean(Object sourceBean) {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			beanToMap(sourceBean, map, false);
		} catch (BeanException e) {
			throw new SmileRunException(e);
		}
		return map;
	}
	/**
	 * 从bean构造一个map 当bean的属性值为空是也会填充到 map中
	 * @param surceBean
	 * @param includes 需要填充到map的属性名
	 * @return
	 */
	public static Map<String,Object> mapFromBean(Object surceBean,Set<String> includes){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			beanToMap(surceBean, map,includes, true);
		} catch (BeanException e) {
			throw new SmileRunException(e);
		}
		return map;
	}

	/**
	 * bean转入map中
	 * @param sourceBean
	 * @param targetMap 
	 * @param nullTo 是否为空的属性成要填充到map中   即例 map.put("name",null);
	 * @return 填充后的map
	 * @throws BeanException
	 */
	public static void beanToMap(Object sourceBean, Map targetMap, boolean nullTo) throws BeanException {
		Class c = sourceBean.getClass();
		PropertyDescriptor[] readers = BeanInfo.getInstance(c).beanUtilProperties();
		for (PropertyDescriptor p : readers) {
			Method getter = p.getReadMethod();
			try {
				if(getter!=null){
					Object value = getter.invoke(sourceBean);
					if (nullTo || value != null) {
						targetMap.put(p.getName(), value);
					}
				}
			} catch (Exception e) {
				throw new BeanException(sourceBean + "转换成Map异常-->"+p.getName(), e);
			}
		}
	}
	
	/**
	 * bean转入map中
	 * @param sourceBean
	 * @param targetMap 
	 * @param nullTo 是否为空的属性成要填充到map中   即例 map.put("name",null);
	 * @return 填充后的map
	 * @throws BeanException
	 */
	public static void beanToMap(Object sourceBean, Map targetMap,Set<String> includes, boolean nullTo) throws BeanException {
		Class c = sourceBean.getClass();
		BeanInfo beanInfo = BeanInfo.getInstance(c);
		for (String pname:includes) {
			PropertyDescriptor p =beanInfo.getPropertyDescriptor(pname);
			if(p==null){
				throw new NoPropertyException("can not exists property named "+pname+" in class "+c);
			}
			Object  value = getObjectProperty(sourceBean, p);
			if (nullTo || value != null) {
				targetMap.put(p.getName(), value);
			}
		}
	}

	/**
	 * bean填充到map中
	 * @param sourceBean
	 * @param targetMap
	 * @param filler 自定义属性转换类
	 * @return
	 * @throws BeanException
	 */
	public static void beanToMap(Object sourceBean, Map targetMap, BeanPropertyFiller filler) throws BeanException {
		Class c = sourceBean.getClass();
			PropertyDescriptor[] propertys = BeanInfo.getInstance(c).beanUtilProperties();
			for (int i = 0; i < propertys.length; i++) {
				PropertyDescriptor p = propertys[i];
				Method getter = p.getReadMethod();
				if (getter == null) {
					continue;
				}
				Object value=null;
				try {
					value = getter.invoke(sourceBean);
				} catch (Exception e) {
					throw new BeanException(e);
				}
				filler.fillPropertyToMap(getter.getReturnType(), value, p.getName(), targetMap);
			}
	}

	/**
	 * bean的属性转到map中
	 * @param sourceBean 要转的bean
	 * @param targetMap 目录map
	 * @param propertyConverter 属性名转换工具 可以对属性名对应转换
	 * @return
	 * @throws BeanException
	 */
	public static void beanToMap(Object sourceBean, Map targetMap, PropertyConverter propertyConverter) throws BeanException {
		Class c = sourceBean.getClass();
		PropertyDescriptor[] propertys = BeanInfo.getInstance(c).beanUtilProperties();
		for (int i = 0; i < propertys.length; i++) {
			PropertyDescriptor p = propertys[i];
			Method getter = p.getReadMethod();
			try {
				if (getter == null) {
					continue;
				}
				Object value = getter.invoke(sourceBean);
				targetMap.put(propertyConverter.propertyToKey(p), value);
			} catch (Exception e) {
				throw new BeanException(sourceBean + "转换成Map异常-->"+p.getName(), e);
			}
		}
	}

	/**
	 * @param targetBean 要填充数据的目标对象
	 * @param sourceBean 数据来源的对象
	 * @param excludes  排除掉不填充的字段
	 * @throws BeanException
	 */
	public static void fillProperties(Object targetBean, Object sourceBean, Set<String> excludes) throws BeanException {
		fillProperties(targetBean, sourceBean, new NoCasePropertyConverter(targetBean.getClass()), excludes);
	}

	/**
	 * 填充属性值
	 * @param targetBean
	 * @param source
	 * @throws BeanException
	 * @throws ConvertException 
	 */
	public static void fillProperties(Object targetBean, Object source) throws BeanException {
		if (source instanceof Map) {
			fillMapToBean((Map) source, targetBean, new NoCasePropertyConverter(targetBean.getClass()), CONVERTER, true);
		} else {
			fillProperties(targetBean, source, new NoCasePropertyConverter(targetBean.getClass()));
		}
	}

	/**
	 * 以map中的内容填充到一个bean中,只是转移key对应的数据
	 * 不会对目标类型做任何转换，如存在类型转换 可使用fillProperties
	 * @param sourceMap 数据来源的map
	 * @param targetBean 接收数据的目标bean对象
	 */
	public static void populate(Map<String, Object> sourceMap, Object targetBean) {
		try {
			BeanInfo beanInfo = BeanInfo.getInstance(targetBean.getClass());
			for (Map.Entry<String, Object> entry : sourceMap.entrySet()) {
				String key = entry.getKey();
				PropertyDescriptor property = beanInfo.getPropertyDescriptor(key);
				if (property != null) {
					Method setter = property.getWriteMethod();
					if (setter != null) {
						Object value = entry.getValue();
						// 不须知类型转换，直接赋值
						setter.invoke(targetBean, value);
					}
				}
			}
		} catch (Exception e) {
			throw new SmileRunException(e);
		}
	}

	/**
	 * 从一个bean中迁移属性到另一个bean中
	 * 但属性的类型必须是一致的
	 * @param sourceBean
	 * @param targetBean
	 */
	public static void populate(Object sourceBean, Object targetBean) {
		try {
			BeanInfo targetBeanInfo = BeanInfo.getInstance(targetBean.getClass());
			BeanInfo sourceBeanInfo= BeanInfo.getInstance(sourceBean.getClass());
			Set<PropertyDescriptor> targetPds = targetBeanInfo.getWritePropertyDescriptors();
			for (PropertyDescriptor pd : targetPds) {
				PropertyDescriptor sourcePd = sourceBeanInfo.getPropertyDescriptor(pd.getName());
				if (sourcePd != null) {
					Method getter = sourcePd.getReadMethod();
					if (getter != null) {
						//源对象的值
						Object value = getter.invoke(sourceBean);
						// 不须知类型转换，直接赋值
						pd.getWriteMethod().invoke(targetBean, value);
					}
				}
			}
		} catch (Exception e) {
			throw new SmileRunException(e);
		}
	}

	/**
	 * 复制一个bean对象
	 * @param bean
	 * @param <T>
	 * @return
	 */
	public static <T> T cloneBean(T bean){
		Class clazz = bean.getClass();
		T res = null;
		try {
			res = (T)ClassTypeUtils.newInstance(clazz);
		} catch (BeanException e) {
			throw new SmileRunException(e);
		}
		populate(bean,res);
		return res;
	}

	/**
	 * 属性不区分大小写
	 * 以目标对象为基准从源对象中加载属性的值
	 * @param targetBean 需赋值的目标对象
	 * @param sourceBean 属性值来源的源对象
	 * @throws BeanException
	 */
	public static void loadProperties(Object targetBean, Object sourceBean) throws BeanException {
		loadProperties(targetBean, sourceBean, PropertyKeyType.nocase);
	}

	/**
	 * 使用不区分大小写加载属性从另一个对象中
	 * @param targetBean
	 * @param sourceBean
	 * @param includes 需要加载的属性名称
	 * @throws BeanException
	 */
	public static void loadProperties(Object targetBean, Object sourceBean, Set<String> includes) throws BeanException {
		loadProperties(targetBean, sourceBean, PropertyKeyType.nocase, includes);
	}

	/**
	 * 填充属性值
	 * @param targetBean
	 * @param sourceBean
	 * @throws BeanException
	 * @throws ConvertException 
	 */
	public static void loadProperties(Object targetBean, Object sourceBean, PropertyKeyType type) throws BeanException {
		// 以目标为基准加载
		PropertyDescriptor[] targetProps = BeanInfo.getInstance(targetBean.getClass()).beanUtilProperties();
		BeanInfo beanInfo=BeanInfo.getInstance(sourceBean.getClass());
		for (int i = 0; i < targetProps.length; i++) {
			PropertyDescriptor p = targetProps[i];
			Method setter = p.getWriteMethod();
			if (setter != null) {
				PropertyDescriptor sourcePd =type.getBeanInfoProperty(beanInfo,p.getName());
				if (sourcePd != null&&sourcePd.getReadMethod()!=null) {
					Object value = null;
					try {
						value = getObjectProperty(sourceBean, sourcePd);
						setObjectProperty(targetBean, p, value);
					} catch (Exception e) {
						throw new BeanException(p + "load value->"+value+" errro", e);
					}
				}
			}
		}
	}
	
	/**
	 * 填充属性值
	 * @param targetBean  目标对象
	 * @param sourceBean 源对象
	 * @param type
	 * @param includes 获取包含的字段
	 * @throws BeanException
	 */
	public static void loadProperties(Object targetBean, Object sourceBean, PropertyKeyType type,Set<String> includes) throws BeanException {
		// 以目标为基准加载
		BeanInfo targetBeanInfos = BeanInfo.getInstance(targetBean.getClass());
		BeanInfo sourceBeanInfo=BeanInfo.getInstance(sourceBean.getClass());
		for (String pdName:includes) {
			PropertyDescriptor p = type.getBeanInfoProperty(targetBeanInfos, pdName);
			if(p!=null){
				Method setter = p.getWriteMethod();
				if (setter != null) {
					PropertyDescriptor sourcePd =type.getBeanInfoProperty(sourceBeanInfo,p.getName());
					if (sourcePd != null&&sourcePd.getReadMethod()!=null) {
						Object value = null;
						try {
							value = getObjectProperty(sourceBean, sourcePd);
							setObjectProperty(targetBean, p, value);
						} catch (Exception e) {
							throw new BeanException(p + "load value->"+value+" errro", e);
						}
					}
				}
			}
		}
	}

	/**
	 * 填充属性值 会循环源对象的所有属性
	 * 去查找目标对象对应的属性  
	 * @param targetBean
	 * @param sourceBean
	 * @param convert 是对目标对象的属性的一个查找的一个转换
	 * @throws BeanException
	 * @throws ConvertException 
	 */
	public static void fillProperties(Object targetBean, Object sourceBean, PropertyConverter<PropertyDescriptor> convert) throws BeanException {
		Class sourceClazz = sourceBean.getClass();
		PropertyDescriptor[] propertys = BeanInfo.getInstance(sourceClazz).beanUtilProperties();
		for (int i = 0; i < propertys.length; i++) {
			PropertyDescriptor p = propertys[i];
			Method getter = p.getReadMethod();
			if (getter != null) {
				try {
					Object value = getter.invoke(sourceBean);
					setObjectProperty(targetBean, convert, p.getName(), value);
				} catch (Exception e) {
					logger.warn("method " + getter + " invoke errro", e);
				}
			}
		}
	}

	/***
	 * 以源对象为基准向目标对象填充属性
	 * @param targetBean 目标对象
	 * @param sourceBean 源对象
	 * @param convert 字段转换器
	 * @param excludes 要排除的字段
	 * @throws BeanException
	 */
	public static void fillProperties(Object targetBean, Object sourceBean, PropertyConverter<PropertyDescriptor> convert, Set<String> excludes) throws BeanException {
		Class sourceClazz = sourceBean.getClass();
		PropertyDescriptor[] propertys =  BeanInfo.getInstance(sourceClazz).beanUtilProperties();
		for (int i = 0; i < propertys.length; i++) {
			PropertyDescriptor p = propertys[i];
			Method getter = p.getReadMethod();
			if (getter != null && !excludes.contains(p.getName())) {
				try {
					Object value = getter.invoke(sourceBean);
					setObjectProperty(targetBean, convert, p.getName(), value);
				} catch (Exception e) {
					throw new BeanException(p.getName()+" fill invoke error", e);
				}
			}
		}
	}

	/**
	 * 设计一个对象的属性值
	 * @param targetBean 目标对象
	 * @param property 目标对象的属性
	 * @param value 要设置的值
	 * @throws BeanException
	 */
	public static void setObjectProperty(Object targetBean, PropertyDescriptor property, Object value) throws BeanException {
		Method setter = property.getWriteMethod();
		if (setter != null) {
			// 泛型
			Generic generic = ClassTypeUtils.getGenericObj(setter.getGenericParameterTypes()[0]);
			// 转换类型
			try {
				Class type = property.getPropertyType();
				value = CONVERTER.convert(type, generic, value);
				setter.invoke(targetBean, value);
			} catch (Exception e) {
				throw new AccessPropertyException(targetBean + " write property " + property.getName() + " " + value + " error", e);
			}
		}
	}

	/**
	 * 设置一个对象的属性值
	 * @param targetBean 要设置的目标对象
	 * @param convert key属性转换器
	 * @param key 设置的key
	 * @param value 要设置的值
	 * @throws BeanException
	 */
	public static void setObjectProperty(Object targetBean, PropertyConverter<PropertyDescriptor> convert, String key, Object value) throws BeanException {
		PropertyDescriptor pd = convert.keyToProperty(key);
		if (pd != null) {
			setObjectProperty(targetBean, pd, value);
		}
	}

	/**
	 * 获取对象的属性
	 * @param targetBean
	 * @param property
	 * @return
	 * @throws BeanException
	 */
	public static Object getObjectProperty(Object targetBean, PropertyDescriptor property) throws BeanException {
		Method getter = property.getReadMethod();
		if (getter != null) {
			try {
				return getter.invoke(targetBean);
			} catch (Exception e) {
				throw new AccessPropertyException(targetBean + " get property " + property.getName() + " error", e);
			}
		}
		throw new NoPropertyException("property "+property.getName()+" not a getter ");
	}

	/**
	 * 获取属性表达式的值
	 * @param targetData
	 * @param exp 可以是带 . 的表达式 如 name.firstName
	 * @return 
	 * @throws BeanException
	 */
	public static Object getExpValue(Object targetData, String exp) throws BeanException {
		int index = exp.indexOf(Chars.DOT);
		if (index > 0) {
			PropertyHandler handler=PropertyHandlers.getHanlder(targetData.getClass());
			return handler.getExpFieldValue(targetData, exp);
		}
		return getValue(targetData, exp);
	}

	/**
	 * 获取字段的值  支持map key获取值
	 * @param targetData
	 * @param field
	 * @return
	 * @throws BeanException
	 */
	public static Object getValue(Object targetData, String field) throws BeanException {
		if (targetData instanceof Map) {
			Map map = (Map) targetData;
			return map.get(field);
		} else {
			try {
				Method method = ClassTypeUtils.getGetter(targetData.getClass(), field);
				return method.invoke(targetData);
			} catch (Exception e) {
				throw new AccessPropertyException("获取" + targetData + "的" + field + "属性失败", e);
			}
		}
	}

	/**
	 * 设置表达式value 
	 * 但此方法不支持对为Map List 子属性赋值   此需求可使用{@link BeanProperties}
	 * 
	 * @param target
	 * @param exp 可以是表达式  name.firstName
	 * @param value
	 * @throws BeanException
	 */
	public static void setExpValue(Object target, String exp, Object value) throws BeanException {
		int index = exp.indexOf(Chars.DOT);
		if (index > 0) {
			PropertyHandler handler=PropertyHandlers.getHanlder(target.getClass());
			handler.setExpFieldValue(target, exp,value);
		} else {
			setValue(target, exp, value);
		}
	}

	/**
	 * 获取字段的值 支持对map key 设置值 
	 * @param targetObject
	 * @param field
	 * @return
	 * @throws BeanException
	 */
	public static void setValue(Object targetObject, String field, Object value) throws BeanException {
		if (targetObject instanceof Map) {
			Map map = (Map) targetObject;
			map.put(field, value);
		} else {
			try {
				Method method = ClassTypeUtils.getSetter(targetObject.getClass(), field);
				method.invoke(targetObject, CONVERTER.convert(method.getParameterTypes()[0], null, value));
			} catch (Exception e) {
				throw new AccessPropertyException("设置" + targetObject + "的" + field + "属性失败", e);
			}
		}
	}

	/**
	 * map 转成 bean
	 * @param sourceMap
	 * @param targetBean
	 * @return
	 * @throws ConvertException 
	 * @throws IntrospectionException
	 * @throws Exception
	 */
	public static void mapToBean(Map sourceMap, Object targetBean) throws ConvertException {
		mapToBean(sourceMap, targetBean, CONVERTER);
	}

	/**
	 * map 模糊转入 bean  以不分大小写 忽略下划线的 原则
	 * @param sourceMap
	 * @param targetBean
	 * @return
	 * @throws ConvertException
	 * @throws IntrospectionException
	 */
	public static Object mapLikeToBean(Map sourceMap, Object targetBean) throws ConvertException {
		return fillMapToBean(sourceMap, targetBean, new LikePropertyConverter(targetBean.getClass()), CONVERTER, false);
	}

	/**
	 * 把 map 转成 bean  以key 和 属性不分大小写的原原则转换
	 * @param sourceMap
	 * @param targetBean
	 * @return
	 * @throws ConvertException
	 */
	public static Object mapIgnoreCaseToBean(Map sourceMap, Object targetBean) throws ConvertException {
		return fillMapToBean(sourceMap, targetBean, new NoCasePropertyConverter(targetBean.getClass()), CONVERTER, false);
	}

	/**
	 * 模糊转换 可以把 first_name key 转成 bean 中的 firstName或firstname 属性
	 * 会对目标类型进行转换
	 * @param sourceMap
	 * @param targetBean
	 * @return 目标bean的引用
	 * @throws ConvertException 
	 */
	public static Object fillMapToBean(Map<String, Object> sourceMap, Object targetBean, PropertyConverter<PropertyDescriptor> propertyConverter, Converter converter, boolean isNullTo)
			throws ConvertException {
		for (Map.Entry<String, Object> entry : sourceMap.entrySet()) {
			String key = entry.getKey();
			PropertyDescriptor property = propertyConverter.keyToProperty(key);
			if (property != null) {
				try {
					Method setter = property.getWriteMethod();
					if (setter != null) {
						Object value = entry.getValue();
						if (value != null || isNullTo) {
							// 泛型
							Generic generic = ClassTypeUtils.getGenericObj(setter.getGenericParameterTypes()[0]);
							Class type = property.getPropertyType();
							// 转换类型
							value = converter.convert(type, generic, value);
							setter.invoke(targetBean, value);
						}
					}
				} catch (Exception e) {
					throw new ConvertException(entry.toString()+" -->"+property.getName(), e);
				}
			}
		}
		return targetBean;
	}

	/**
	 * 从map创建一个bean
	 * @param beanClass 
	 * @param sourceMap
	 * @return
	 * @throws BeanException
	 */
	public static Object newBean(Class beanClass, Map sourceMap) throws ConvertException {
		try {
			Object bean = beanClass.newInstance();
			mapToBean(sourceMap, bean);
			return bean;
		}catch (InstantiationException e) {
			throw new ConvertException(e);
		} catch (IllegalAccessException e) {
			throw new ConvertException(e);
		}
	}
	/**
	 * map 转数据到bean 中
	 * @param sourceMap
	 * @param targetBean
	 * @param converter
	 * @throws ConvertException
	 */
	public static void mapToBean(Map sourceMap, Object targetBean, Converter converter) throws ConvertException {
		 mapToBean(sourceMap, targetBean, converter, false);
	}

	/**
	 * map 转换成 bean
	 * @param sourceMap
	 * @param targetBean
	 * @param converter
	 * @param isNullTo 当map中不存在属性时上否要设置bean属性值为空
	 * @throws ConvertException
	 */
	public static void mapToBean(Map sourceMap, Object targetBean, Converter converter, boolean isNullTo) throws ConvertException {
		Class c = targetBean.getClass();
		try {
			PropertyDescriptor[] propertys = Introspector.getBeanInfo(c).getPropertyDescriptors();
			PropertyDescriptor property;
			for (int i = 0; i < propertys.length; i++) {
				property = propertys[i];
				Method setter = property.getWriteMethod();
				Class type = property.getPropertyType();
				String key = property.getName();
				if (setter != null && sourceMap.containsKey(key)) {
					Object value = sourceMap.get(key);
					if (value != null || isNullTo) {
						// 泛型
						Generic generic = ClassTypeUtils.getGenericObj(setter.getGenericParameterTypes()[0]);
						// 转换类型
						value = converter.convert(type, generic, value);
						try{
							setter.invoke(targetBean, value);
						}catch(Exception e){
							throw new ConvertException("setter "+key+" "+value,e);
						}
					}
				}
			}
		}catch (IntrospectionException e) {
			throw new ConvertException("map to bean "+c+" bean infos exception ", e);
		}
	}
	
    /**
     * Gets the type of a property of an object.
     * 
     * @param object The object that the property belongs to, cannot be null.
     * @param property The property to get type, can be nested. for example, 'foo.bar.baz'.
     * @return The type of the property. If the property doesn't exists in the object, returns null.
     */
    public static Class getPropertyType(Object object, String property) {
        if (object == null) {
            throw new IllegalArgumentException("Object cannot be null.");
        }
        return getPropertyType(object.getClass(), property);
    }

    /**
     * Gets the type of a property of a class.
     * 
     * @param clazz The class that the property belongs to, cannot be null.
     * @param property The property to get type, can be nested. for example, 'foo.bar.baz'.
     * @return The type of the property. If the property doesn't exists in the clazz, returns null.
     */
    public static Class getPropertyType(Class clazz, String property) {
        if (clazz == null) {
            throw new IllegalArgumentException("Clazz cannot be null.");
        }

        if (property == null) {
            throw new IllegalArgumentException("Property cannot be null.");
        }

        int dotIndex = property.lastIndexOf('.');

        if (dotIndex == -1) {
            Method method = getReadMethod(clazz, property);
            return method == null ? null : method.getReturnType();
        } 
            
        String deepestProperty = property.substring(dotIndex + 1);
        String parentProperty = property.substring(0, dotIndex);
        return getPropertyType(getPropertyType(clazz, parentProperty), deepestProperty);
    }
    
    /**
     * Gets the read method for a property in a class.
     * 
     * for example: <code>
     * class Foo {
     *      public String getBar() { return "bar"; } 
     *      public Boolean isBaz() { return false; }
     * }
     * 
     * BeanUtils.getReadMethod(Foo.class, "bar"); // return Foo#getBar()
     * BeanUtils.getReadMethod(Foo.class, "baz"); // return Foo#isBaz()
     * BeanUtils.getReadMethod(Foo.class, "baa"); // return null
     * </code>
     * 
     * @param clazz The class to get read method.
     * @param property The property to get read method for, can NOT be nested.
     * @return The read method (getter) for the property, if there is no read method for
     *          the property, returns null.
     */
    public static Method getReadMethod(Class clazz, String property) {
        BeanInfo beanInfo=BeanInfo.getInstance(clazz);
        PropertyDescriptor pd=beanInfo.getPropertyDescriptor(property);
        if(pd==null){
        	return null;
        }
        return pd.getReadMethod();
    }

	/**
	 * 获取bean读取属性方法
	 * @param clazz
	 * @param property
	 * @return
	 */
	public static Method getWriteMethod(Class clazz, String property) {
		BeanInfo beanInfo=BeanInfo.getInstance(clazz);
		PropertyDescriptor pd=beanInfo.getPropertyDescriptor(property);
		if(pd==null){
			return null;
		}
		return pd.getWriteMethod();
	}
}
