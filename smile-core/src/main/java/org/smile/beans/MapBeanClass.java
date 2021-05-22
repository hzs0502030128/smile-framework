package org.smile.beans;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.smile.collection.UnmodifiableSet;

public class MapBeanClass{
	/**定义的字段 */
	protected final Map<String,FieldDeclare> properties=new LinkedHashMap<String,FieldDeclare>();
	/**属性处理器，默认使用不区分大小写*/
	protected BeanProperties bp=new BeanProperties();
	/**定义一个字段*/
	public void declareFiled(String name,FieldDeclare propertyValue){
		properties.put(name, propertyValue);
	}
	/**获取一个字段信息*/
	public FieldDeclare getField(String name){
		return properties.get(name);
	}
	/**字段的个数*/
	protected int fieldCount(){
		return properties.size();
	}
	/**是否存在字段*/
	public boolean hasDeclareFiled(String name){
		return properties.containsKey(name);
	}
	
	public <T extends MapBean> T newInstance(){
		T bean=createNew();
		for(Map.Entry<String,FieldDeclare> entry:properties.entrySet()){
			bean.setProperty(entry.getKey(), new PropertyValue(entry.getValue()));
		}
		return bean;
	}
	
	protected <T extends MapBean> T createNew(){
		return (T) new MapBean(this);
	}
	
	public BeanProperties getBeanProperties(){
		return bp;
	}

	@Override
	public String toString() {
		return getClass().getName()+properties.keySet();
	}
	
	/***
	 * 获取所有定义的字段
	 * @return
	 */
	public Set<String> getFieldNames(){
		return new UnmodifiableSet<String>(properties.keySet());
	}
	
}
