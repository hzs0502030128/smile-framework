package org.smile.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.smile.collection.CollectionUtils;
import org.smile.util.Properties;
import org.smile.util.RegExp;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "properties")
public class PropertiesConfig {
	
	protected static final RegExp splitReg=new RegExp("[\n;]+");
	@XmlAttribute
	protected String name;
	
	protected List<ValueConfig> value;
	
	protected List<EntryConfig> entry;
	
	
	public Properties getProperties(){
		Properties p=new Properties();
		if(CollectionUtils.notEmpty(value)){
			for(ValueConfig c:value){
				loadString(c.getValue(), p);
			}
		}
		if(CollectionUtils.notEmpty(entry)){
			for(EntryConfig c:entry){
				p.put(c.getKey(), c.getValue());
			}
		}
		return p;
	}
	
	protected void loadString(String text,Properties p){
		List<String> pvString=splitReg.splitAndTrimNoBlack(text);
		for(String pv:pvString){
			String[] strs=pv.split("=");
			p.put(strs[0].trim(), strs[1].trim());
		}
	}

	public String getName() {
		return name;
	}


	public List<ValueConfig> getValue() {
		return value;
	}

	public List<EntryConfig> getEntry() {
		return entry;
	}
	
	
	
}
