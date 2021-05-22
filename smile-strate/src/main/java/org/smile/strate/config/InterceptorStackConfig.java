package org.smile.strate.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "interceptor-stack")
public class InterceptorStackConfig {
	@XmlAttribute
	private String name;
	@XmlElementRef(name="interceptor-ref")
	private List<InterceptorRefConfig> interceptorRefs;
	public String getName() {
		return name;
	}
	public List<InterceptorRefConfig> getInterceptorRefs() {
		return interceptorRefs;
	}
	
}
