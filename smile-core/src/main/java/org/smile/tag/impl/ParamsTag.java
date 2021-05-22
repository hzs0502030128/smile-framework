
package org.smile.tag.impl;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

import org.smile.commons.Strings;
import org.smile.commons.ann.Attribute;
import org.smile.util.StringUtils;

/**
 * 为url添加参数
 * @author 胡真山
 *
 */
public class ParamsTag extends UrlTagSupport{
	@Attribute
	private Object value;
	@Attribute
	private String name;
	
	@Override
	public void doTag() throws Exception {
		UrlTag tag=getUrlTag();
		if(tag==null){
			throw new TagException("parent is not urltag");
		}
		if(value!=null){
			doParam(tag);
		}else if(StringUtils.notEmpty(name)){
			value=evaluateAttribute(name);
			if(value!=null){
				doParam(tag);
			}
		}
	}
	
	private void doParam(UrlTag tag) throws Exception{
		if(value instanceof String){
			addMapParam(tag,TagUtils.parseToMap(value));
		}else if(value instanceof Map){
			addMapParam(tag,(Map)value);
		}else{
			addObjectParam(tag,value);
		}
	}
	/**
	 * 添加一个map做为参数
	 * @param map
	 * @throws JspException
	 */
	protected void addMapParam(UrlTag tag,Map<Object,Object> map) throws Exception{
		for(Map.Entry<Object,Object> entry:map.entrySet()){
			String keyName=String.valueOf(entry.getKey());
			addParam(tag,keyName, entry.getValue());
		}
	}
	
	protected void addParam(UrlTag tag,String keyName,Object value) throws Exception{
		if(StringUtils.notEmpty(name)){
			keyName=name+Strings.DOT+keyName;
		}
		tag.addParam(keyName,value);
	}
	/**
	 * 添加一个对象做为参数
	 * @param bean
	 * @throws JspException
	 */
	protected void addObjectParam(UrlTag tag,Object bean) throws TagException{
		Class c=bean.getClass();
		PropertyDescriptor[] propertys;
		try {
			propertys = Introspector.getBeanInfo(c).getPropertyDescriptors();
			for(int i=0;i<propertys.length;i++){
				PropertyDescriptor p=propertys[i];
				Method getter=p.getReadMethod();
				if(getter!=null&&!Strings.CLASS.equals(p.getName())){
					Object value=getter.invoke(bean);
					if(StringUtils.isNotNull(value)){
						addParam(tag,p.getName(), value);
					}
				}
			}
		} catch (Exception e) {
			throw new TagException("设置url参数失败", e);
		}
	}
}