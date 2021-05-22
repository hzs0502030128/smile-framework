package org.smile.strate.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import org.smile.strate.action.Action;
import org.smile.strate.ann.Result;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "result")
public class ResultConfig {
	@XmlAttribute
	private String name=Action.SUCCESS;
	@XmlAttribute
	private String type;
	@XmlValue
	private String value;
	
	public ResultConfig(){}
	
	public ResultConfig(Result result){
		this.name=result.name();
		this.type=result.type();
		this.value=result.value();
	}
	
	public ResultConfig(String type,String value){
		this.type=type;
		this.value=value;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
