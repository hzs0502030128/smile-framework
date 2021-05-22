package org.smile.ormdb.dao;

import java.util.List;
import java.util.Map;

import org.smile.db.jdbc.RecordDao;
import org.smile.orm.ann.Mapper;
import org.smile.orm.ann.Select;
@Mapper(include = "method.sql",namespace = "record.test")
public interface ITestRecordDao extends RecordDao<Student>{
	@Select
	public List<Student> queryStudent(Map params);
}
