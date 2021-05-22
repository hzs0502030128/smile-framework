package org.smile.bean;

import java.util.Date;

import org.smile.beans.PropertiesGetter;

public class Student extends SuperBean {

	private String name;
	private int age;
	private String firstName;
	private String secondName;
	private String address;
	private Date birthday;
	protected PropertiesGetter<String, Object> properties;
	public Student(){}
	
	
	public Student(String name,Number age){
		this.name=name;
		this.age=age.intValue();
	}
	
	public Student(Object name,int age){
		this.name=(String)name;
		this.age=age;
	}
	
	public Student(String name,int age){
		this.name=name;
		this.age=age;
	}
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
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getSecondName() {
		return secondName;
	}
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	@Override
	public String getId() {
		return super.getId();
	}
	
	public PropertiesGetter<String, Object> getProperties() {
		return properties;
	}
	public void setProperties(PropertiesGetter<String, Object> properties) {
		this.properties = properties;
	}


	@Override
	public Student testBrig() {
		return null;
	}

}
