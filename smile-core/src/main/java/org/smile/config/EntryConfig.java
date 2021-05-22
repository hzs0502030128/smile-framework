package org.smile.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "entry")
public class EntryConfig extends ValueConfigSupport implements MapEntrySupport{
	@XmlAttribute
	protected String key;
	@XmlElementRef(name="bean")
	protected ValueConfig valueConfig;

	public String getKey() {
		return key;
	}

	public ValueConfig getValueConfig() {
		return valueConfig;
	}
	
}
