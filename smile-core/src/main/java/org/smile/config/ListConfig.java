package org.smile.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.smile.util.StringUtils;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "list")
public class ListConfig {
	@XmlAttribute
	protected String name;
	
	protected List<ValueConfig> value;
	
	protected List<BeanConfig> bean;

	public String getName() {
		return name;
	}

	public List<ValueConfig> getValue() {
		return value;
	}
	
	/**
	 *	 把列表的多属性参考转为List<String>
	 * @param list
	 * @return
	 */
	public static List<String> convertRefToList(List<ValueConfig> list){
		List<String> refs=new ArrayList<String>(list.size());
		for(ValueConfig c:list){
			if(StringUtils.notEmpty(c.getRef()))
			refs.add(c.getRef());
		}
		return refs;
	}
	/**
	 * <list><value>1</value><value>2</value><value>2</value><list>  
	 * --->  [1,2,3]
	 *    列表的多value配置转成字符串列表
	 * @param list
	 * @return
	 */
	public static List<String> convertToList(List<ValueConfig> list){
		List<String> map=new ArrayList<String>(list.size());
		for(ValueConfig c:list){
			if(StringUtils.notEmpty(c.getValue())){
				map.add(c.getText());
			}
		}
		return map;
	}

	public List<BeanConfig> getBean() {
		return bean;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(List<ValueConfig> value) {
		this.value = value;
	}

	public void setBean(List<BeanConfig> bean) {
		this.bean = bean;
	}
	
	
	
}
