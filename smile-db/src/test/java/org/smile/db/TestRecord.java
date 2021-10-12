package org.smile.db;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.smile.collection.CollectionUtils;
import org.smile.db.criteria.Criteria;
import org.smile.db.criteria.Restrictions;
import org.smile.db.jdbc.JdbcMapRecord;
import org.smile.json.JSON;

public class TestRecord extends BaseTest implements ITable{
	@Test
	public void testRecordDao() throws SQLException{
		List<JdbcMapRecord> list=StudentDao.queryAll();
		System.out.println(JSON.toJSONString(list));
		
		list=StudentDao.queryByIds(1,2,5,4);
		System.out.println(JSON.toJSONString(list));
		list=StudentDao.queryByIds(2);
		System.out.println(JSON.toJSONString(list));
		list=StudentDao.query("name=?", "行者武松");
		
		System.out.println(list);
		StudentDao.deleteByIds(new Object[]{3,7});
		TStudent.buildRecord().set("id", 2).set("name", "神仙").update();
		StudentDao.enableByIds(CollectionUtils.linkedList(52,24));
		
	}
	@Test
	public void testRecordInsert() throws SQLException {
		TStudent.buildRecord().set("name", "小马哥").set("age", 100).insert();
		JdbcMapRecord record=TStudent.buildRecord().setId(115);
		record.load();
		record.set("name", "小李").set("email", "11@com").update();
		List<JdbcMapRecord> list=StudentDao.query("name=? and address is not null ", "胡真山");
	}
	
	@Test
	public void testRecord() throws SQLException{
		JdbcMapRecord record=TStudent.buildRecord();
		record.set("id", 2);
		record.set("name", "行者武松");
		record.set("address", "梁山水泊");
		record.set("age", "10");
		record.insert();
		System.out.println(record);
	}
	@Test
	public void testCriteria() throws SQLException {
		Criteria<JdbcMapRecord> criteria=StudentDao.criteria();
		List<JdbcMapRecord> list=criteria.and(Restrictions.or(Restrictions.eq("age", 3), Restrictions.eq("age", 6))).eq("name", "胡真山").eq("enable", true).list();
		System.out.println(list);
		list=criteria.top(1);
		System.out.println(criteria.first());
		criteria.eq("id", 29);
		long c=criteria.count();
		System.out.println(c);
		c=criteria.delete();
		System.out.println(c);
		list.stream().forEach(v->{
			System.out.println(v);
		});
		List<String> alpha = Arrays.asList("a", "b", "c", "d");
		List<String> collect = alpha.stream().map(String::toUpperCase).collect(Collectors.toList());
        System.out.println(collect); //[A, B, C, D]
		System.out.println(StudentDao.criteria().field("age").eq("name", "胡真山").offset(2).limit(3).listField(Integer.class));
		System.out.println(StudentDao.criteria().limit(5).list());
	}

	@Test
	public void testUpdateOrSave() throws SQLException{
		JdbcMapRecord s=TStudent.buildRecord();
		s.set("id", 2);
		s.put("name","胡真山");
		s.put("address","中国");
		s.put("age",1000);
		StudentDao.saveOrUpdate(s);
		StudentDao.add(s);
		s.insert();
		StudentDao.update(s, new String[]{"name"});
		PageModel<JdbcMapRecord> list=StudentDao.queryPage(2,3,"age=? order by id desc", new Object[]{1000});
		int index=0;
		for(JdbcMapRecord map:list.getRows()){
			map.set("name", "小白"+(++index));
			map.update(new String[]{"name"});
		}
		JdbcMapRecord s2=TStudent.buildRecord();
		s2.setId(2);
		s2.load();
		System.out.println(s2);
	}
	@Test
	public void testHumpRecord() throws SQLException {
		JdbcMapRecord s=TStudent.buildRecord();
		s.set("Name","胡真山");
		s.set("address","小山村");
		s.set("first_name", "胡");
		s.set("classId",100);
		System.out.println(s.get("Name"));
		s.save();
	}
}
