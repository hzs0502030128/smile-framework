package org.smile.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
/**
 * 配置一个bean的信息
 * xml配置一个bean 
 * @author 胡真山
 * @Date 2016年4月29日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "bean")
public class BeanConfig implements RefSupport{
	@XmlAttribute
	protected String id;
	@XmlAttribute(name="class")
	protected String clazz; 
	/**配置属性*/
	protected List<PropertyConfig> property;
	/**是否是单例*/
	@XmlAttribute
	protected boolean single=true;
	/**可以用于封装map list */
	protected List<ListConfig> list;
	@XmlAttribute
	protected String ref;
	/**map属性*/
	protected List<MapConfig> map;
	@XmlAttribute
	protected String initializing;

	public String getId() {
		return id;
	}

	public String getClazz() {
		return clazz;
	}

	public List<PropertyConfig> getProperty() {
		return property;
	}

	public boolean isSingle() {
		return single;
	}

	public List<ListConfig> getList() {
		return list;
	}

	public List<MapConfig> getMap() {
		return map;
	}

	@Override
	public String getRef() {
		return ref;
	}

	public String getInitializing() {
		return initializing;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public void setProperty(List<PropertyConfig> property) {
		this.property = property;
	}

	public void setSingle(boolean single) {
		this.single = single;
	}

	public void setList(List<ListConfig> list) {
		this.list = list;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public void setMap(List<MapConfig> map) {
		this.map = map;
	}

	public void setInitializing(String initializing) {
		this.initializing = initializing;
	}
	
	
}
