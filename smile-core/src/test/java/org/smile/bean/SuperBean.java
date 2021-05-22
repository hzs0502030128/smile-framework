package org.smile.bean;

public abstract class SuperBean implements StudentSup<Student>{
	String id;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
}
