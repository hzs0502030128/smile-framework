package org.smile.beans.property;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

import org.smile.beans.BeanInfo;
import org.smile.beans.PropertyKeyType;
import org.smile.collection.KeyNoCaseHashMap;
import org.smile.util.StringUtils;
/**
 * 使用映射的方式对字段进行对应
 * 根据不同的keyType去配置字段信息
 * @author 胡真山
 *
 */
public class MapPropertyConverter extends PropertyDescriptorConverter{
	
	private Map<String,String> keyConvertPropertyNames;
	
	private Map<String,String> propertyConvertKeys;
	
	private PropertyKeyType keyType;
	
	private BeanInfo beanInfo;
	
	/**
	 * @param keyMap 字段对应映射 字段对应映射 key->源对象属性   value->赋值目标对象属性
	 * @param targetBeanClass
	 * @throws IntrospectionException
	 */
	public MapPropertyConverter(Map keyMap,Class targetBeanClass){
		this(keyMap, targetBeanClass, PropertyKeyType.nocase);
	}
	/**
	 * 
	 * @param keyMap 字段对应映射 key->源对象属性   value->赋值目标对象属性
	 * @param targetBeanClass
	 * @param keyType
	 * @throws IntrospectionException
	 */
	public MapPropertyConverter(Map keyMap,Class targetBeanClass,PropertyKeyType keyType){
		super(targetBeanClass);
		keyConvertPropertyNames=keyMap;
		this.keyType=keyType;
	}
	
	@Override
	public PropertyDescriptor keyToProperty(String key) {
		String convertKey=keyConvertPropertyNames.get(key);
		if(StringUtils.notEmpty(convertKey)){
			key=convertKey;
		}
		return keyType.getBeanInfoProperty(beanInfo, key);
	}

	@Override
	public String propertyToKey(PropertyDescriptor property) {
		initProperty2KeyMap();
		String name=property.getName();
		if(this.keyType==PropertyKeyType.like){
			name=LikeKeyUtil.nameToKey(name);
		}
		String convertKey=propertyConvertKeys.get(name);
		if(convertKey!=null){
			return convertKey;
		}
		return name;
	}
	
	private void initProperty2KeyMap(){
		if(propertyConvertKeys==null){
			synchronized (beanInfo) {
				propertyConvertKeys=new KeyNoCaseHashMap<String>();
				for(Map.Entry<String, String> entry:keyConvertPropertyNames.entrySet()){
					propertyConvertKeys.put(entry.getValue(),entry.getKey());
				}
			}
		}
	}

}
