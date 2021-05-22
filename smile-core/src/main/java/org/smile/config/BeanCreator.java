package org.smile.config;

import java.lang.reflect.Method;

import org.smile.beans.AbstractBeanProducer;
import org.smile.beans.BeanProperties;
import org.smile.beans.converter.BeanException;
import org.smile.commons.SmileRunException;
import org.smile.config.parser.TagConfigParser;
import org.smile.config.parser.TagParser;
import org.smile.util.StringUtils;
/**
 * 从 {@link BeanConfig }配置对象创建一个bean 
 * @author 胡真山
 * 2015年10月27日
 */
public class BeanCreator extends AbstractBeanProducer{
	/**创建bean的配置信息*/
	protected BeanConfig config;
	/**创建的bean的类型*/
	protected Class clazz;
	
	/**用于属性操作*/
	protected BeanProperties beanProperties=BeanProperties.NORAL;
	/**实始化后调用方法*/
	protected Method initializingMethod;
	/**用于对配置的信息解析*/
	protected TagParser parser;
	
	public String getBeanId(){
		if(StringUtils.isEmpty(config.getId())) {
			return this.clazz.getName();
		}
		return config.getId();
	}
	
	public BeanCreator(){
		//用于子类继承
		parser=new TagConfigParser();
	}
	
	public BeanCreator(BeanConfig config){
		parser=new TagConfigParser();
		setConfig(config);
	}
	
	public BeanCreator(BeanConfig config,TagParser parser){
		this.parser=parser;
		setConfig(config);
	}
	
	/**
	 * 是否单例
	 * @return
	 */
	@Override
	public boolean isSingle(){
		return config.isSingle();
	}
	
	/**
	 * 创建新的bean
	 * @return
	 * @throws BeanException
	 */
	@Override
	protected Object createBean() throws BeanException {
		return parser.parse(config,beanProperties);
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
		return config.getId()+"-"+clazz+"-"+super.toString();
	}
	/**
	 * 设置创建对象的配置信息
	 * @param config
	 */
	public void setConfig(BeanConfig config) {
		try {
			this.clazz=Class.forName(config.getClazz());
		} catch (ClassNotFoundException e) {
			throw new SmileRunException(e);
		}
		this.config=config;
	}

	public void setParser(TagParser parser) {
		this.parser = parser;
	}

	@Override
	public Class getBeanClass() {
		return clazz;
	}
}
