package org.smile.db;

import org.smile.db.jdbc.JdbcMapper;
import org.smile.db.handler.HumpKeyColumnSwaper;
import org.smile.db.jdbc.JdbcMapRecordDao;
import org.smile.db.jdbc.SimpleJdbcMapper;
import org.smile.db.jdbc.TableInfoCfg;

public interface ITable {
	
	TableInfoCfg TStudent=new TableInfoCfg("student").autoincrement().enabled("enable").keyColumnSwaper(HumpKeyColumnSwaper.instance);
	/**学生表*/
	JdbcMapper<Student> STUDENT=new SimpleJdbcMapper<Student>(Student.class,TStudent);
	
	
	JdbcMapRecordDao StudentDao=new JdbcMapRecordDao(TStudent);
}
