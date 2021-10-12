
package org.smile.jstl.tags.core;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.smile.commons.Strings;
import org.smile.jstl.tags.JSONParseSupport;
import org.smile.util.StringUtils;
import org.smile.web.scope.ScopeUtils;

/**
 * 为url添加参数
 * @author 胡真山
 *
 */
public class ParamsTag extends UrlTagSupport{
	
	private Object value;
	
	private String name;
	
	@Override
	public int doEndTag() throws JspException {
		if(value!=null){
			doParam();
		}else if(StringUtils.notEmpty(name)){
			value=ScopeUtils.getAttribute(name, pageContext);
			if(value!=null){
				doParam();
			}
		}
		return EVAL_PAGE;
	}
	
	private void doParam() throws JspException{
		if(value instanceof String){
			addMapParam(JSONParseSupport.parseToMap(value));
		}else if(value instanceof Map){
			addMapParam((Map)value);
		}else{
			addObjectParam(value);
		}
	}
	/**
	 * 添加一个map做为参数
	 * @param map
	 * @throws JspException
	 */
	protected void addMapParam(Map<Object,Object> map) throws JspException{
		for(Map.Entry<Object,Object> entry:map.entrySet()){
			String keyName=String.valueOf(entry.getKey());
			addParam(keyName, entry.getValue());
		}
	}
	
	protected void addParam(String keyName,Object value) throws JspException{
		if(StringUtils.notEmpty(name)){
			keyName=name+Strings.DOT+keyName;
		}
		urlTag.addParam(keyName,value);
	}
	/**
	 * 添加一个对象做为参数
	 * @param bean
	 * @throws JspException
	 */
	protected void addObjectParam(Object bean) throws JspException{
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
						addParam(p.getName(), value);
					}
				}
			}
		} catch (Exception e) {
			throw new JspException("设置url参数失败", e);
		}
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}