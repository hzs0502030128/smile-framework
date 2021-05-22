package org.smile.util;

import org.smile.bean.Student;
import org.smile.commons.ann.Config;
import org.smile.commons.ann.Value;

@Config("app")
public class AppConfig {
	@Value("calendar.firstDayOfWeek")
	private String firstDayOfWeek;
	private String name;
	private Student stu;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Student getStu() {
		return stu;
	}
	public void setStu(Student stu) {
		this.stu = stu;
	}
	public String getFirstDayOfWeek() {
		return firstDayOfWeek;
	}
	public void setFirstDayOfWeek(String firstDayOfWeek) {
		this.firstDayOfWeek = firstDayOfWeek;
	}
	
	
}
