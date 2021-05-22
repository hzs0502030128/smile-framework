package org.smile.bean;

import org.smile.commons.ann.Skip;
public interface StudentSup<T> {
	public String getName();
	public void setName(String name);
	public int getAge();
	public void setAge(int age);
	public T testBrig();
}
