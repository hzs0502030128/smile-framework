package org.smile.tag.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "tag-lib")
public class TagLibConfig implements Comparable<TagLibConfig>{
	@XmlElement
	protected String description;
	@XmlElement
	protected String version;
	@XmlElement
	protected String name;
	@XmlElementRef
	protected List<TagConfig> tag;
	@XmlElementRef
	protected List<FunctionConfig> function;

	public String getDescription() {
		return description;
	}

	public String getVersion() {
		return version;
	}

	public String getName() {
		return name;
	}

	public List<TagConfig> getTag() {
		return tag;
	}

	public List<FunctionConfig> getFunction() {
		return function;
	}

	@Override
	public int compareTo(TagLibConfig o) {
		int i=this.name.compareTo(o.name);
		if(i==0){
			return -this.version.compareTo(o.version);
		}
		return i;
	}
	
}
