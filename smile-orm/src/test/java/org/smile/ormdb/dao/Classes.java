package org.smile.ormdb.dao;

import org.smile.orm.ann.Column;
import org.smile.orm.ann.Entity;
import org.smile.orm.ann.Id;
import org.smile.orm.record.EnableOrmRecord;

@Entity(table = "clazz")
public class Classes extends EnableOrmRecord{
	@Id
	Long id;
	@Column
	String name;
	@Column
	String number;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	
}
