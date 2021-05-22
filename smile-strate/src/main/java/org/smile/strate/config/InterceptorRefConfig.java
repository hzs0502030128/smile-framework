package org.smile.strate.config;
/**
 * 定义一个拦截器引用
 */
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "interceptor-ref")
public class InterceptorRefConfig {
	@XmlAttribute
	private String name;
	public String getName() {
		return name;
	}
}
