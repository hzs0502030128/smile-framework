package org.smile.ioc.bean;

public class BeanAttribute {
	/**
	 * bean在容器中的id
	 */
	private String id;
	/**类名*/
	private Class<? extends FactoryBean> clazz;
	
	private boolean singleton=true;
	
	public String getId() {
		return id;
	}
	/**
	 * 设置定义bean的id
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	public Class<? extends FactoryBean> getClazz() {
		return clazz;
	}
	/**
	 * 	设置bean定义的类
	 * @param clazz
	 */
	public void setClazz(Class<? extends FactoryBean> clazz) {
		this.clazz = clazz;
	}
	
	public boolean isSingleton() {
		return singleton;
	}
	
	/**
	 * 设置是否单例
	 * @param singleton
	 */
	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}
	
	
	
	
}
