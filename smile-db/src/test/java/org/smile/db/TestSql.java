package org.smile.db;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.smile.db.sql.BoundSql;
import org.smile.db.sql.MappingBoundSql;

public class TestSql {
	@Test
	public void test() throws SQLException{
		String sql="select * from wms_t where name = %{name} select * from wms_t where nameselect * from wms_t where nameselect * from wms_t where nameselect * from wms_t where nameselect * from wms_t where name";
		
		Map map=new HashMap();
		map.put("name", "");
		map.put("age", 12);
		map.put("address", "123");
		BoundSql bound=new MappingBoundSql(sql, map);
		long start=System.currentTimeMillis();
		for(int i=0;i<100000;i++){
			bound=new MappingBoundSql(sql, map);
		}
		System.out.println(System.currentTimeMillis()-start);
	}
}
