package org.smile.ormdb.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.smile.commons.ann.Param;
import org.smile.dataset.DataSet;
import org.smile.db.handler.HumpResultSetMap;
import org.smile.db.sql.parameter.BatchParameterMap;
import org.smile.orm.ann.Batch;
import org.smile.orm.ann.Mapper;
import org.smile.orm.ann.Select;
import org.smile.orm.ann.Sql;
import org.smile.orm.base.BaseDAO;

@Mapper(include = "method.sql",namespace = "sql.test")
public interface ITestDao extends BaseDAO{
	@Select(sql="select count(0) from student where name like %{like:arg0}",template="smile")
	public Number queryCount2(String name,int age);
	@Select(sql="select birthday from student where id=%{id}",template="smile")
	public Date queryCountBirthday(String name,int age,@Param(name="id")int id);
	@Select(sql="select birthday from student where 1=1 <#if id??> and id=%{id}</#if>",template="smile" )
	public List<Map<String,Object>> queryMap(Object obj);
	@Select(sql="select count(0) from student")
	public int queryNamesCounts(Object obj);
	@Select
	public List<String> queryNames(Object obj);
	@Select(template="smile",sql="select * from student where 1=1 <c:if test='${id!=null}'> and id=%{id}</c:if>",include="method.sql")
	public List<Student> queryStu(@Param(name="id")Long id);
	@Select(sql="select id,name,address from student limit 1000")
	public DataSet queryDatas(Student s);
	@Select(sql="select * from student where 1=1 <c:if test='${notEmpty(name)}'> and name like %{name}", template="smile")
	public List<HumpResultSetMap> queryStudentAsMap(Object params);
	@Batch(sql="update student set name=%{name} where id=%{id}")
	public int batchUpdate(List params);
	@Sql(sql="select name,create_user as 'updateInfo.createUser',update_user,class_id from student where id<100",type = "select")
	public List<Student> queryStudentList();
	@Batch(sql="delete from student where id=%{id}")
	public void deleteByIds(Object[] ids);
	
	@Batch(sql="delete from student where id=%{id} and name!=%{name}")
	public void deleteBatchByIds(BatchParameterMap params);
	
}
