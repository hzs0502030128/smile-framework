package org.smile.beans;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.smile.beans.converter.BasicConverter;
import org.smile.beans.converter.Converter;
import org.smile.collection.MapUtils;
import org.smile.reflect.ClassTypeUtils;
import org.smile.reflect.Generic;

public class CglibBeanWrapper<T> implements BeanWrapper<T> {
	/**目录类*/
	private Class<T> targetClass;
	/**对字段进行一些映射处理*/
	private LinkedHashMap<String,String> fields;
	/***/
	private BeanInfo targetBeaninfo;
	/**对值类型的转换器*/
	private Converter converter=BasicConverter.getInstance();
	/**
	 * 
	 * @param targetClass 要生成对象的目标类型
	 */
	public CglibBeanWrapper(Class<T> targetClass){
		this.fields=new LinkedHashMap<String,String>();
		this.targetClass=targetClass;
		targetBeaninfo=BeanInfo.getInstance(targetClass);
	}
	
	@Override
	public BeanWrapper<T> fields(String propertyName,String mappingName){
		fields.put(propertyName, mappingName);
		return this;
	}
	
	@Override
	public T build(Object source){
		if(source instanceof Map){
			return new MapMapperTarget((Map)source).createProxy();
		}else{
			return new BeanWrapperSource(source).createProxy();
		}
	}
	
	
	
	@Override
	public Class<T> getTargetClass() {
		return targetClass;
	}

	@Override
	public void setConverter(Converter converter) {
		this.converter = converter;
	}



	abstract class WrapperTarget implements MethodInterceptor{
		/**
		 * 创建代理类对象
		 * @param clazz
		 * @return
		 */
		protected T createProxy() {
			Enhancer enhancer = new Enhancer(); 
			enhancer.setSuperclass(targetClass);
			enhancer.setCallback(this);
			return (T)enhancer.create();
		}
		
		protected boolean isReadMethod(Method method,PropertyDescriptor pd){
			return method.getName().equals(pd.getReadMethod().getName());
		}
	}
	
	
	class BeanWrapperSource extends WrapperTarget{
		BeanInfo sourceBeaninfo;
		Object source;
		BeanWrapperSource(Object bean){
			this.source=bean;
			this.sourceBeaninfo=BeanInfo.getInstance(this.source.getClass());
		}
		
		@Override
		public Object intercept(Object target, Method method, Object[] args, MethodProxy proxy) throws Throwable {
			PropertyDescriptor pd=targetBeaninfo.getPropertyDescriptor(method);
			if(pd!=null){
				String sourcePdName=fields.get(pd.getName());
				if(sourcePdName==null){
					sourcePdName=pd.getName();
				}
				PropertyDescriptor sourcePd=sourceBeaninfo.getPropertyDescriptor(sourcePdName);
				if(sourcePd!=null){
					Method sourceMethod;
					if(isReadMethod(method, pd)){
						sourceMethod=sourcePd.getReadMethod();
						Object result= sourceMethod.invoke(source, args);
						Generic generic = ClassTypeUtils.getGenericObj(method.getGenericReturnType());
						// 转换类型
						return converter.convert(method.getReturnType(), generic, result);
					}else{
						sourceMethod=sourcePd.getWriteMethod();
						Type type=method.getGenericParameterTypes()[0];
						Generic generic = ClassTypeUtils.getGenericObj(type);
						// 转换类型
						Object param= converter.convert(sourcePd.getPropertyType(), generic, args[0]);
						return sourceMethod.invoke(source, param);
					}
				}
			}
			return proxy.invokeSuper(target, args);
		}
	}
	
	class MapMapperTarget extends WrapperTarget{
		
		private Map<String,Object> sourceMap;
		
		MapMapperTarget(Map<String,Object> map){
			this.sourceMap=map;
		}
		
		@Override
		public Object intercept(Object target, Method method, Object[] args, MethodProxy proxy) throws Throwable {
			PropertyDescriptor pd=targetBeaninfo.getPropertyDescriptor(method);
			if(pd!=null){
				String sourcePdName=fields.get(pd.getName());
				if(sourcePdName==null){
					sourcePdName=pd.getName();
				}
				if(isReadMethod(method, pd)){
					return sourceMap.get(sourcePdName);
				}else{
					sourceMap.put(sourcePdName, args[0]);
					return null;
				}
			}
			return proxy.invokeSuper(target, args);
		}
		
	}

	@Override
	public T build() {
		return new MapMapperTarget(MapUtils.resultMap()).createProxy();
	}
	
}
