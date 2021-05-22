package org.smile.strate.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "interceptors")
public class InterceptorsConfig {
	@XmlElementRef(name="interceptor")
	private List<InterceptorConfig> interceptor;
	@XmlElementRef(name="interceptor-stack")
	private List<InterceptorStackConfig> interceptorStatck;
	public List<InterceptorConfig> getInterceptor() {
		return interceptor;
	}
	public List<InterceptorStackConfig> getInterceptorStatck() {
		return interceptorStatck;
	}
	
	
}
