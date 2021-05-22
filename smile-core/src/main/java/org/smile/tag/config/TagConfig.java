package org.smile.tag.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "tag")
public class TagConfig {
	@XmlElement
	String name;
	@XmlElement(name="tag-class")
	String tagClass;
	@XmlElement
	String description;
	
	public String getName() {
		return name;
	}
	public String getTagClass() {
		return tagClass;
	}
	public String getDescription() {
		return description;
	}

	
}
