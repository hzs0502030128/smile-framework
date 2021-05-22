package org.smile.strate.config;
/**
 * 配置定义一个拦截器
 */
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "interceptor")
public class InterceptorConfig {
	@XmlAttribute
	private String name;
	@XmlAttribute(name="class")
	private String clazz;
	public String getName() {
		return name;
	}
	public String getClazz() {
		return clazz;
	}
}
