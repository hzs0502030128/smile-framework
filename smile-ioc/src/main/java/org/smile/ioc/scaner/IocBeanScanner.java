package org.smile.ioc.scaner;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import org.smile.annotation.AnnotationUtils;
import org.smile.beans.converter.BeanException;
import org.smile.commons.ann.Bean;
import org.smile.commons.ann.Sington;
import org.smile.config.BeanCreateException;
import org.smile.ioc.BeanFactory;
import org.smile.ioc.ann.Configuration;
import org.smile.ioc.bean.BeanBuilder;
import org.smile.ioc.bean.BeanDefinition;
import org.smile.ioc.bean.ConfigBeanBuilder;
import org.smile.ioc.bean.DefaultFactoryBean;
import org.smile.ioc.load.FactoryBeanRegistry;
import org.smile.reflect.ClassTypeUtils;
import org.smile.reflect.MethodUtils;
import org.smile.util.ClassScaner;
import org.smile.util.StringUtils;
/**
 * 用于对注解式IOC注入类的扫描
 * @author 胡真山
 */
public class IocBeanScanner implements FactoryBeanRegistry{
	/**
	 * 用于配置扫描的路径
	 */
	protected List<String> packageString;

	@Override
	public void processBeanRegistry(BeanFactory beanFactory) throws BeanException {
		ClassScaner scaner=new ClassScaner();
		for(String str:packageString){
			Set<Class<?>> classes=scaner.getClasses(str);
			for(Class<?> clazz:classes){
				Bean beanAnn=AnnotationUtils.getAnnotation(clazz,Bean.class);
				if(beanAnn!=null){
					BeanDefinition beanBuilder=registryBean(beanFactory, beanAnn, clazz);
					Configuration configuration=AnnotationUtils.getAnnotation(clazz, Configuration.class);
					if(configuration!=null) {
						registryConfiguration(beanFactory, configuration, beanBuilder);
					}
				}
			}
		}
	}
	
	private BeanDefinition registryBean(BeanFactory beanFactory,Bean beanAnn,Class<?> clazz) throws BeanCreateException {
		String name=beanAnn.value();
		if(StringUtils.isEmpty(name)){
			name=ClassTypeUtils.getFirstCharLowName(clazz);
		}
		BeanDefinition definition=new BeanDefinition();
		definition.getAttribute().setClazz(DefaultFactoryBean.class);
		definition.getAttribute().setId(name);
		definition.addPropertyValue("factory", beanFactory);
		//是否单例
		boolean single=true;
		Sington singtonAnn=AnnotationUtils.getAnnotation(clazz, Sington.class);
		if(singtonAnn!=null) {
			single=singtonAnn.value();
		}
		definition.getAttribute().setSingleton(single);
		definition.addPropertyValue("type", clazz);
		definition.regsitToFactory(beanFactory);
		return definition;
	}
	
	private void registryConfiguration(BeanFactory beanFactory,Configuration configuration,BeanBuilder sourceBeanBuilder) throws BeanCreateException {
		Class clazz=sourceBeanBuilder.getBeanClass();
		Method[] methods=MethodUtils.getDeclaredMethods(clazz);
		for(Method m:methods) {
			Bean beanAnn=AnnotationUtils.getAnnotation(m, Bean.class);
			if(beanAnn!=null) {
				ConfigBeanBuilder configBuilder=new ConfigBeanBuilder(sourceBeanBuilder, m, beanAnn);
				configBuilder.regsitToFactory(beanFactory);
				Configuration subConfigurationAnn=AnnotationUtils.getAnnotation(m, Configuration.class);
				if(subConfigurationAnn!=null) {//方法上存在配置
					registryConfiguration(beanFactory, subConfigurationAnn, configBuilder);
				}
			}
		}
	}

	public List<String> getPackageString() {
		return packageString;
	}
	
	public void setPackageString(List<String> packageString) {
		this.packageString = packageString;
	}
	
}
