package org.smile.xml;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TestXmlVo {
	
	public TestXmlVo(){
		
	}
	public void init(){
		name="<胡>";
		address="中国";
		list=new LinkedList();
		list.add(new XmlVo());
		list.add(new XmlVo());
		list.add(new XmlVo());
	}
	private String name;
	private String address;
	List<XmlVo> list;
	public String getName() {
		return name;
	}
	public String getAddress() {
		return address;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<XmlVo> getList() {
		return list;
	}
	public void setList(List<XmlVo> list) {
		this.list = list;
	}
	
	
}
