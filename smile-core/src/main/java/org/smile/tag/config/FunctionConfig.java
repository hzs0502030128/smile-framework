package org.smile.tag.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "function")
public class FunctionConfig {
	@XmlElement
	String description;
	@XmlElement
	String name;
	@XmlElement(name="function-class")
	String functionClass;
	@XmlElement
	String method;
	@XmlElement(name="arg-type")
	String argType;
	
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public String getFunctionClass() {
		return functionClass;
	}
	
	public String getMethod() {
		return method;
	}
	public String getArgType() {
		return argType;
	}
	
	
}
