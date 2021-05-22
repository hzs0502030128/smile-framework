package org.smile.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

import org.smile.util.StringUtils;
/**
 * 对属性的值配置支持  
 * 可以配置value属性也可以配置子文本节点来实现
 * @author 胡真山
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
public class ValueConfigSupport {
	@XmlValue
	protected String text;
	@XmlAttribute
	protected String value;
	/**
	 * 获取配置的值
	 * @return
	 */
	public String getValue() {
		if(value==null){
			if(StringUtils.notEmpty(getText())){
				return text;
			}
		}
		return value;
	}
	/**
	 * 获取子文本属性
	 * @return
	 */
	protected String getText(){
		text=StringUtils.trim(text);
		return text;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
}
