package org.smile.xml;

import java.util.List;
import java.util.Map;
import java.util.Properties;
public class TestVo {
	private String name;
	private XmlVo user;
	private Map<Integer,Object> map;
	private List<XmlVo> list;
	private List<String> strs;
	private Properties props;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public XmlVo getUser() {
		return user;
	}
	public void setUser(XmlVo user) {
		this.user = user;
	}
	
	public Map<Integer, Object> getMap() {
		return map;
	}
	public void setMap(Map<Integer, Object> map) {
		this.map = map;
	}
	public List<XmlVo> getList() {
		return list;
	}
	public void setList(List<XmlVo> list) {
		this.list = list;
	}
	public List<String> getStrs() {
		return strs;
	}
	public void setStrs(List<String> strs) {
		this.strs = strs;
	}
	public Properties getProps() {
		return props;
	}
	public void setProps(Properties props) {
		this.props = props;
	}
	
	
}
