package org.smile.strate.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "exception-mapping")
public class ExceptionConfig {
	@XmlAttribute
	private String result;
	@XmlAttribute
	private String exception;
	public String getResult() {
		return result;
	}
	public String getException() {
		return exception;
	}
}
