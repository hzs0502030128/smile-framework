package org.smile.orm.load;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.smile.config.BeanConfig;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "application")
public class ApplicationXml {
	private List<BeanConfig> bean;

	public List<BeanConfig> getBean() {
		return bean;
	}
}
