package org.smile.orm.result;

import java.sql.SQLException;

import org.smile.db.sql.BoundSql;
import org.smile.db.sql.SQLRunner;

/**
 * 单对象反回类型
 * @author 胡真山
 * 2015年10月29日
 */
public class ObjectResultType extends BaseResultType{
	
	public ObjectResultType(Class type){
		this.type=type;
	}

	@Override
	public Class getGenericType() {
		return type;
	}

	@Override
	public boolean isSingleObj() {
		return true;
	}

	@Override
	public Object executeQuery(SQLRunner runner, BoundSql boundSql) throws SQLException {
		return runner.queryFirst(boundSql);
	}
	
}
