package org.smile.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.smile.collection.CollectionUtils;
import org.smile.util.StringUtils;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "map")
public class MapConfig {
	@XmlAttribute
	protected String name;
	/**
	 * 配置map的元素 同时支持两种子标签配置
	 */
	protected List<PropertyConfig> property;
	/**
	 * map元素配置     同时支持两种子标签配置
	 */
	protected List<EntryConfig> entry;
	/**
	 * 获取配置的Map结果
	 * @return
	 */
	public Map<String,String> getMap(){
		List map=new ArrayList();
		if(property!=null){
			map.addAll(property);
		}
		if(entry!=null){
			map.addAll(entry);
		}
		if(CollectionUtils.notEmpty(map)){
			return convertToMap(map);
		}
		return null;
	}
	
	
	public static Map<String,String> convertToMap(List<? extends MapEntrySupport> list){
		Map<String,String> map=new HashMap<String, String>();
		for(MapEntrySupport c:list){
			if(StringUtils.notEmpty(c.getValue())){
				map.put(c.getKey(), c.getValue());
			}
		}
		return map;
	}
	
	public static Map<String,String> convertRefToMap(List<PropertyConfig> list){
		Map<String,String> map=new HashMap<String, String>();
		for(PropertyConfig c:list){
			if(StringUtils.notEmpty(c.getRef())){
				map.put(c.getName(), c.getRef());
			}
		}
		return map;
	}

	public String getName() {
		return name;
	}

	public List<PropertyConfig> getProperty() {
		return property;
	}


	public List<EntryConfig> getEntry() {
		return entry;
	}
	
	

}
