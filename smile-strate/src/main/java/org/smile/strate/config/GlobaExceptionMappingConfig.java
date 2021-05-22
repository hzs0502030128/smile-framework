package org.smile.strate.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "global-exception-mappings")
public class GlobaExceptionMappingConfig {
	
	@XmlElementRef(name="exception-mapping")
	private List<ExceptionConfig> exceptions;

	public List<ExceptionConfig> getExceptions() {
		return exceptions;
	}

	public void setExceptions(List<ExceptionConfig> exceptions) {
		this.exceptions = exceptions;
	}
	
}
