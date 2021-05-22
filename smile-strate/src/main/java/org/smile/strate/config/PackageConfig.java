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
@XmlRootElement(name = "package")
public class PackageConfig {
	
	@XmlAttribute
	private String name;
	
	@XmlAttribute
	private String namespace;

	@XmlAttribute(name = "extends")
	private String extend;

	@XmlElementRef(name = "default-interceptor-ref")
	private DefaultInterceptorConfig defaultInterceptor;

	/**包范围的跳转结果*/
	private List<ResultConfig> result;
	/**此包中的action配置*/
	private List<ActionConfig> action;

	public String getNamespace() {
		return namespace;
	}

	public void setAction(List<ActionConfig> action) {
		this.action = action;
	}

	public List<ActionConfig> getAction() {
		return action;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getName() {
		return name;
	}

	public String getExtend() {
		return extend;
	}

	public DefaultInterceptorConfig getDefaultInterceptor() {
		return defaultInterceptor;
	}

	public List<ResultConfig> getResult() {
		return result;
	}

}
