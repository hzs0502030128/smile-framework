package org.smile.ormdb;

import java.util.List;

import org.smile.commons.ann.Resource;
import org.smile.commons.ann.Service;
import org.smile.db.JdbcTemplate;
import org.smile.ormdb.dao.ITestDao;
import org.smile.ormdb.dao.Student;
//@Service
public class StudentService implements IStudentService{
	@Resource
	ITestDao iTestDao;
	@Resource
	JdbcTemplate dbTemplate;

	@Override
	public List<Student> queryStudents() {
		return iTestDao.queryStudentList();
	}

	@Override
	public void updateStudent() {
		dbTemplate.executeUpdate("update student set name='测试事务'  where id =?", 20);
		dbTemplate.executeUpdate("update student set name='测试事务'  where id =?", 21);
	}
}
