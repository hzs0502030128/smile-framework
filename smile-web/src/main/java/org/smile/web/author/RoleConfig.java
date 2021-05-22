package org.smile.web.author;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "role")
public class RoleConfig {
	@XmlAttribute
	protected String name;
	@XmlValue
	protected String url;
	
	public String getName() {
		return name;
	}
	public String getUrl() {
		return url;
	}
	
	
	
}
