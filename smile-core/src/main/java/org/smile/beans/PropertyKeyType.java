package org.smile.beans;

import java.beans.PropertyDescriptor;
import java.util.Map;

public enum PropertyKeyType {
	
	normal(new PdGetter(){
		@Override
		public PropertyDescriptor get(BeanInfo beanInfo, String name) {
			return beanInfo.getPropertyDescriptor(name);
		}

		@Override
		public Map<String, PropertyDescriptor> getPdMap(BeanInfo beanInfo) {
			return beanInfo.getPdMap();
		}}),
	nocase(new PdGetter(){
		@Override
		public PropertyDescriptor get(BeanInfo beanInfo, String name) {
			return beanInfo.getPropertyDescriptorNocase(name);
		}

		@Override
		public Map<String, PropertyDescriptor> getPdMap(BeanInfo beanInfo) {
			return beanInfo.getNocasePdMap();
		}}),
	like(new PdGetter(){
		@Override
		public PropertyDescriptor get(BeanInfo beanInfo, String name) {
			return beanInfo.getPropertyDescriptorLike(name);
		}

		@Override
		public Map<String, PropertyDescriptor> getPdMap(BeanInfo beanInfo) {
			return beanInfo.getLikePdMap();
		}});
	
	private PdGetter getter;
	
	private PropertyKeyType(PdGetter getter){
		this.getter=getter;
	}
	/**
	 * 以此属性名称对应方式获取一个bean属性
	 * @param beanInfo
	 * @param name 要获取的属性名称 
	 * @return
	 */
	public PropertyDescriptor getBeanInfoProperty(BeanInfo beanInfo,String name){
		return getter.get(beanInfo,name);
	}
	
	public Map<String,PropertyDescriptor> getBeanInfoPdMap(BeanInfo beanInfo){
		return getter.getPdMap(beanInfo);
	}
	
	interface PdGetter{
		
		PropertyDescriptor get(BeanInfo beanInfo,String name);
		
		Map<String,PropertyDescriptor> getPdMap(BeanInfo beanInfo);
	}
}
