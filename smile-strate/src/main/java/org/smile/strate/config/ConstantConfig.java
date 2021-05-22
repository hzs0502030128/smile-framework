package org.smile.strate.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
/**
 * 常量配置标签
 * @author 胡真山
 * @Date 2016年1月18日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "constant")
public class ConstantConfig {
	@XmlAttribute
	private String name;
	@XmlAttribute
	private String value;
	public String getName() {
		return name;
	}
	public String getValue() {
		return value;
	}
	
	
}
