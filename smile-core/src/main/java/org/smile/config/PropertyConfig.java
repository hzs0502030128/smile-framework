package org.smile.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.smile.util.StringUtils;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "property")
public class PropertyConfig extends ValueConfigSupport implements MapEntrySupport, RefSupport {
	@XmlAttribute
	protected String name;
	/***
	 * 参考引用其它对象的id
	 */
	@XmlAttribute
	protected String ref;
	@XmlElementRef(name = "bean")
	protected BeanConfig beanConfig;
	/** 列表属性 */
	protected ListConfig list;
	/** Map属性 */
	protected MapConfig map;
	/** Value属性 */
	@XmlElementRef(name = "value")
	protected ValueConfig valueConfig;
	/** Properties属性 */
	protected PropertiesConfig properties;

	public String getName() {
		return name;
	}

	/**
	 * 参考属性 可用于配置参考其它对象
	 * 
	 * @return
	 */
	@Override
	public String getRef() {
		if (StringUtils.notEmpty(ref)) {
			return ref;
		} else if (beanConfig != null) {
			return beanConfig.getRef();
		}
		return ref;
	}

	/**
	 * 属性名称
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public BeanConfig getBeanConfig() {
		return beanConfig;
	}

	public ListConfig getList() {
		return list;
	}

	public MapConfig getMap() {
		return map;
	}

	@Override
	public String getKey() {
		return name;
	}

	public ValueConfig getValueConfig() {
		return valueConfig;
	}

	public PropertiesConfig getProperties() {
		return properties;
	}

}
