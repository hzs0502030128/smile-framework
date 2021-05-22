package org.smile.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import org.smile.json.JSONFiledSerializer;
import org.smile.json.JSONObject;
import org.smile.json.JSONStringWriter;
import org.smile.json.JSONValue;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "vo")
public class MVo {
	@XmlAttribute
	protected String name;
	@XmlValue
	protected String content;
	@Override
	public String toString() {
		return new JSONFiledSerializer().serialize(this);
	}
	
	
}
