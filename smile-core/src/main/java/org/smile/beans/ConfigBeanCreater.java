package org.smile.beans;

import java.lang.reflect.Field;
import java.util.List;

import org.smile.Smile;
import org.smile.annotation.AnnotationUtils;
import org.smile.beans.converter.BeanException;
import org.smile.commons.ann.Sington;
import org.smile.commons.ann.Value;
import org.smile.reflect.ClassTypeUtils;
import org.smile.reflect.FieldUtils;
import org.smile.util.Properties;
import org.smile.util.StringUtils;

public class ConfigBeanCreater extends AbstractBeanProducer{

	/**创建的bean的类型*/
	protected Class clazz;
	
	protected Properties properties;
	
	protected boolean single=true;
	
	public String getBeanId(){
		return clazz.getClass().getName();
	}
	
	public ConfigBeanCreater(String configName,Class clazz){
		this.clazz=clazz;
		if(StringUtils.notEmpty(configName)) {
			this.properties=Properties.build(configName);
		}
		Sington sington=AnnotationUtils.getAnnotation(this.clazz, Sington.class);
		if(sington!=null) {
			this.single=sington.value();
		}
	}
	
	/**
	 * 是否单例
	 * @return
	 */
	@Override
	public boolean isSingle(){
		return this.single;
	}
	
	/**
	 * 创建新的bean
	 * @return
	 * @throws BeanException
	 */
	@Override
	protected Object createBean() throws BeanException {
		Object target=ClassTypeUtils.newInstance(clazz);
		List<Field> fields=FieldUtils.getAnyField(clazz);
		for(Field f:fields) {
			//有注解时从smile配置文件中获取值
			Value valueAnn=AnnotationUtils.getAnnotation(f, Value.class);
			if(valueAnn!=null) {
				f.setAccessible(true);
				String key=valueAnn.value();
				if(StringUtils.notEmpty(key)) {
					Object value=Smile.config.get(key);
					if(value!=null) {
						FieldUtils.setFieldValue(f, target, value);
					}
				}
			}
		}
		if(this.properties!=null) {//当前指定的配置文件
			properties.convertTo(target);
		}
		return target;
	}
	/**
	 * 用于创建对象的类
	 * @return
	 */
	public Class getClazz() {
		return clazz;
	}
	

	@Override
	public String toString() {
		return clazz+"-"+super.toString();
	}


	@Override
	public Class getBeanClass() {
		return clazz;
	}
	
}
