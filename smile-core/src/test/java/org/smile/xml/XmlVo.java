package org.smile.xml;



public class XmlVo {
	
	public XmlVo(){
		name="<胡>";
		address="	?h?F#	?100";
		age=100;
	}
	
	private String name;
	private String address;
	private int age;
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
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	
}
