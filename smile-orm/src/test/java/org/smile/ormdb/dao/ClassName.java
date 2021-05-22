package org.smile.ormdb.dao;

import org.smile.orm.ann.Mixes;
import org.smile.orm.ann.Property;

@Mixes(entity = Classes.class,primaryKey = "classId")
public interface ClassName {
	@Property(column = "name")
	public void setClassName(String className);
	public String getClassName();
}
