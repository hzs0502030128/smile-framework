package org.smile.ioc.bean;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.smile.Smile;
import org.smile.annotation.AnnotationUtils;
import org.smile.beans.converter.BasicConverter;
import org.smile.beans.converter.Converter;
import org.smile.collection.CollectionUtils;
import org.smile.commons.Chars;
import org.smile.commons.Strings;
import org.smile.commons.ann.Config;
import org.smile.commons.ann.Resource;
import org.smile.commons.ann.Value;
import org.smile.config.BeanCreateException;
import org.smile.ioc.BeanFactory;
import org.smile.ioc.IocInitException;
import org.smile.reflect.ClassTypeUtils;
import org.smile.reflect.FieldUtils;
import org.smile.reflect.Generic;
import org.smile.util.Properties;
import org.smile.util.StringUtils;

public class DefaultFactoryBean<T> extends AbstractFactoryBean<T>{
	/**IOC工厂*/
	protected BeanFactory factory;
	
	protected Converter converter= BasicConverter.getInstance();
	/**
	 * @Resource注解引用信息
	 */
	private List<ResourceFieldInfo> resources=new LinkedList<ResourceFieldInfo>();;
	/**
	 * @Value 注解引用信息
	 */
	private List<ResourceFieldInfo> values=new LinkedList<ResourceFieldInfo>();
	
	private Properties properties;
	
	public void setFactory(BeanFactory factory) {
		this.factory = factory;
	}

	@Override
	public void onRegsitToFactory() {
		//配置文件前缀
		String prefix=initProperties();
		Map<String,Field> fields=FieldUtils.getAnyNoStaticField(type);
		for(Field f:fields.values()){
			Resource resource=AnnotationUtils.getAnnotation(f,Resource.class);
			if(resource!=null){
				addResourceInfo(f, resource);
			}
			//@Value 注解属性
			Value value=AnnotationUtils.getAnnotation(f, Value.class);
			if(value!=null) {
				addValueInfo(f, value, prefix);
			}
		}
	}
	
	private String  initProperties() {
		//是否存在配置文件
		Config config=AnnotationUtils.getAnnotation(type, Config.class);
		String prefix=null;
		if(config!=null) {
			if(StringUtils.notEmpty(config.value())) {
				properties=Properties.build(config.value());
				if(properties==null) {
					throw new IocInitException("config file "+config.value()+" do not exists ");
				}
			}else {
				properties=Smile.config;
			}
			if(StringUtils.notEmpty(config.prefix())) {
				prefix=config.prefix().endsWith(Strings.DOT)?config.prefix():config.prefix()+Chars.DOT;
			}
		}else {
			properties=Smile.config;
		}
		return prefix;
	}
	/**
	 * value 属性引用
	 * @param f
	 * @param value
	 * @param prefix
	 */
	private void addValueInfo(Field f,Value value,String prefix) {
		String expression=value.value();
		if(StringUtils.isEmpty(expression)){
			expression=f.getName();
		}
		if(prefix!=null&&!expression.startsWith(prefix)) {//拼接上前缀
			expression=prefix+expression;
		}
		f.setAccessible(true);
		values.add(new ResourceFieldInfo(f,expression, ClassTypeUtils.getGenericObj(f.getType())));
	}
	/**
	 * 注册注解引用
	 * @param f 字段
	 * @param name 变量名
	 */
	private void addResourceInfo(Field f,Resource resource){
		String name=resource.name();
		f.setAccessible(true);
		if(StringUtils.isEmpty(name)){
			name=f.getName();
		}
		resources.add(new ResourceFieldInfo(f,name, ClassTypeUtils.getGenericObj(f.getType())));
	}
	/**
	 * 注解引用信息封装
	 * @author 胡真山
	 * @Date 2016年5月9日
	 */
	private class ResourceFieldInfo{
		Field f;
		String resourceName;
		Generic generic;
		ResourceFieldInfo(Field f,String resource,Generic generic){
			this.f=f;
			this.resourceName=resource;
			this.generic=generic;
		}
	}
	
	@Override
	protected T createObject() throws BeanCreateException {
		try {
			T bean =newInstance();
			if(CollectionUtils.notEmpty(resources)){
				for(ResourceFieldInfo info:resources){
					Field f=info.f;
					Object resourceObject=factory.getBean(info.resourceName,false);
					if(resourceObject==null){
						resourceObject=factory.getBean(info.f.getType());
					}
					if(resourceObject!=null){
						f.set(bean, converter.convert(f.getType(), info.generic, resourceObject));
					}
				}
			}
			//注解配置文件属性
			if(CollectionUtils.notEmpty(values)) {
				for(ResourceFieldInfo info:values){
					Field f=info.f;
					Object resourceObject=properties.getValue(info.resourceName);
					if(resourceObject!=null){
						f.set(bean, converter.convert(f.getType(), info.generic, resourceObject));
					}
				}
			}
			return bean;
		} catch (Exception e) {
			throw new BeanCreateException("初例化bean出错"+type, e);
		}
	}
}
