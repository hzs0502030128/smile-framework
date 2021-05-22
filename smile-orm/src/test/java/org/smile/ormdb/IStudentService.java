package org.smile.ormdb;

import java.util.List;

import org.smile.ormdb.dao.Student;
import org.smile.transaction.Transactional;
@Transactional
public interface IStudentService {
	
	public List<Student> queryStudents();
	
	public void updateStudent();
}
