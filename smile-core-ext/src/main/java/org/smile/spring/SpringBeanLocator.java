package org.smile.spring;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.smile.commons.SmileRunException;
import org.smile.reflect.FieldUtils;
import org.smile.util.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;

/***
 * spring factory 操作类 需要配置到spring中
 * 
 * @author 胡真山 2015年9月8日
 */
@Service("mySpringBeanLocator")
public class SpringBeanLocator implements BeanFactoryAware, BeanNameAware {
	/**
	 * 用于保存所有已加载了的类的字段信息
	 */
	private static Map<Class<?>, Set<FieldAnnConfig>> fieldsCache = new ConcurrentHashMap<Class<?>, Set<FieldAnnConfig>>();
	/** 保存beanname 这样我们就不需要与配置文件中的配置 id 进行耦合 */
	private static String BEAN_NAME;
	/** 保存spring中的beanfactory */
	private static BeanFactory beanFactory = null;
	/** 单例 */
	private static SpringBeanLocator instance;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		SpringBeanLocator.beanFactory = beanFactory;
	}

	/**
	 * 是否已经在spring中初始化
	 * 
	 * @return
	 */
	public static boolean isInit() {
		return beanFactory != null;
	}

	/**
	 * 获取spring的beanfactory
	 * 
	 * @return
	 */
	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public String getBeanName() {
		return BEAN_NAME;
	}

	/**
	 * 单例模式
	 * 
	 * @return
	 */
	public static SpringBeanLocator getInstance() {
		if (instance == null) {
			if (beanFactory == null) {
				throw new SmileRunException("没有把SpringBeanLocator 注入到spring中,你可以在xml中配置一个bean或都把 此包名配置到spring的扫描路径中");
			}
			instance = (SpringBeanLocator) beanFactory.getBean(BEAN_NAME);
		}
		return instance;
	}

	/***
	 * 获取spring中的bean对象
	 * 
	 * @param name
	 * @return
	 */
	public <T> T getBean(String name) {
		return (T) beanFactory.getBean(name);
	}

	/***
	 * 获取spring中的bean对象
	 * 
	 * @param name
	 * @return
	 */
	public <T> T getBean(Class clazz) {
		return (T) beanFactory.getBean(clazz);
	}

	/**
	 * 自动注入spring中的bean为属性
	 * 
	 * @param bean
	 */
	public void autowireBeanPropertiesByName(Object bean) {
		((AutowireCapableBeanFactory) beanFactory).autowireBeanProperties(bean, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
	}

	@Override
	public void setBeanName(String name) {
		BEAN_NAME = name;
	}

	/***
	 * 把注解式的bean加载到未被spring管理的类上
	 * 
	 * @param serviceBean
	 *            目录对象
	 * @throws Exception
	 */
	public void loadResourceProperites(Object serviceBean) {
		Set<FieldAnnConfig> fieldset = findClassFieldConfigs(serviceBean.getClass());
		for (FieldAnnConfig fac : fieldset) {
			Object value = fac.getValue(serviceBean);
			if (value == null) {// 为空的才赋值
				value = getBeanByName(fac.name);
				// 不存在是使用类名获取
				if (value == null) {
					value = beanFactory.getBean(fac.field.getType());
				}
				fac.setValue(serviceBean, value);
			}
		}
	}
	
	protected Object getBeanByName(String name){
		try{
			return beanFactory.getBean(name);
		}catch(NoSuchBeanDefinitionException e){
			return null;
		}
	}

	/***
	 * 获取类的字段信息 从保存了的map中查找，不如不存在，生成字段信息放入map中
	 * 
	 * @param beanClass
	 *            要获取的类名
	 * @return 此类的字段信息
	 */
	private Set<FieldAnnConfig> findClassFieldConfigs(Class<?> beanClass) {
		Set<FieldAnnConfig> set = fieldsCache.get(beanClass);
		if (set == null) {
			synchronized (beanClass) {
				Iterable<Field> fields = FieldUtils.getAnyNoStaticField(beanClass).values();
				set = new HashSet<FieldAnnConfig>();
				for (Field f : fields) {
					Resource resource = f.getAnnotation(Resource.class);
					if (resource != null) {
						String name = resource.name();
						if (StringUtils.isEmpty(name)) {
							name = f.getName();// 没有名称是用字段名
						}
						set.add(new FieldAnnConfig(f, name));
						f.setAccessible(true);
					}else{
						org.smile.commons.ann.Resource res = f.getAnnotation(org.smile.commons.ann.Resource.class);
						if(res!=null){
							String name = res.name();
							if (StringUtils.isEmpty(name)) {
								name = f.getName();// 没有名称是用字段名
							}
							set.add(new FieldAnnConfig(f, name));
							f.setAccessible(true);
						}
					}
				}
				fieldsCache.put(beanClass, set);
			}
		}
		return set;
	}
	
	class FieldAnnConfig{
		/**字段*/
		protected Field field;
		/**参考bean的id */
		protected String name;
		
		protected FieldAnnConfig(Field f,String name){
			this.field=f;
			this.name=name;
		}
		
		protected Object getValue(Object bean){
			try {
				return field.get(bean);
			} catch (Exception e) {
				throw new SmileRunException("get field value error -->" + field, e);
			}
		}
		
		protected void setValue(Object bean,Object value){
			try {
				field.set(bean, value);
			} catch (Exception e) {
				throw new SmileRunException("set field value error -->" + field, e);
			}
		}
	}
}
