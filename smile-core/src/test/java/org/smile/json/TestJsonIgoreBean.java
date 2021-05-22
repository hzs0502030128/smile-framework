package org.smile.json;

import org.smile.json.format.JsonIgnore;

public class TestJsonIgoreBean {
	@JsonIgnore("value==null")
	protected String name;
	@JsonIgnore
	protected int age;
	@JsonIgnore
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
}
