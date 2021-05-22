package org.smile.orm.xml.execut;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "snippet")
public class Snippet {
	@XmlAttribute
	protected String id;
	@XmlValue
	protected String content;

	public String getId() {
		return id;
	}

	public String getContent() {
		return content;
	}
}
