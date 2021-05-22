package org.smile.db;

import java.sql.SQLException;
import java.util.List;

import org.junit.Test;
import org.smile.collection.MapUtils;
import org.smile.dataset.DataSet;
import org.smile.db.handler.HumpKeyColumnSwaper;
import org.smile.db.handler.ResultSetMap;
import org.smile.db.sql.MappingBoundSql;
import org.smile.json.JSON;

public class TestTemplate extends BaseTest implements ITable{
	
	@Test
	public void testQuery() throws SQLException{
		String sql="select s.id, s.name  ,c.type as 'score.type',c.score as 'score.score',c.id as 'score.id' from student s left join score c on c.student_id=s.id";
		Student stu=new Student();
		List list=template.query(Student.class,sql);
		System.out.println(JSON.toJSONString(list));
		list=template.query(sql);
		System.out.println(JSON.toJSONString(list));
	}
	
	
	@Test
	public void testMapper() throws SQLException{
		Student s=template.find(STUDENT, 1);
		System.out.println(JSON.toJSONString(s));
	}
	
	@Test
	public void testDataSet() throws SQLException{
		String sql="select name,age,address,update_user as updateUser from student where name = %{name}\r\nand age>10";
		DataSet dataSet=template.queryDataSet(new MappingBoundSql(sql,MapUtils.hashMap("name","小李")));
		while(dataSet.rollNext()){
			System.out.println(dataSet.getString("name"));
			System.out.println(dataSet.getString("age"));
		}
		
		List<ResultSetMap> list=template.query(new MappingBoundSql(sql,MapUtils.hashMap("name","小李")));
		System.out.println(list);
	}
	@Test
	public void test() {
		System.out.println((char)('A'+32));
		String s=HumpKeyColumnSwaper.instance.KeyToColumn("xxXXxxxXxXxx");
		System.out.println(s);
		System.out.println(HumpKeyColumnSwaper.instance.columnToKey("xx_xXxxx_xx_xxx"));
	}
	@Test
	public void testNumber() {
		String sql="select CAST('3.21' as DECIMAL(15,6)) as num from student limit 1";
		List list=template.query(sql);
		System.out.println(list);
	}
	
	
}
