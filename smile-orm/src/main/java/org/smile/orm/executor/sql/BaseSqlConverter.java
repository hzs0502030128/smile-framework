package org.smile.orm.executor.sql;

import java.sql.SQLException;

import org.smile.db.Dialect;
import org.smile.db.sql.ArrayBoundSql;
import org.smile.db.sql.BoundSql;
import org.smile.db.sql.MappingBoundSql;
import org.smile.orm.OrmApplication;
import org.smile.orm.executor.BoundSqlBuilder;
import org.smile.orm.executor.MappedOperator;
/**
 * 基本sql转换
 * 使用%{name} 作为占位符的
 * @author 胡真山
 *
 */
public class BaseSqlConverter implements SqlConverter {
	
	protected OrmApplication application;
	
	@Override
	public String convertToSql(String osql,MappedOperator operator,Dialect dialect) {
		return osql;
	}

	@Override
	public void setOrmApplication(OrmApplication application) {
		this.application=application;
	}

	@Override
	public BoundSql buildBoundSql(BoundSqlBuilder builder, String sql, Object param) throws SQLException {
		return new MappingBoundSql(sql, param);
	}

	@Override
	public BoundSql buildBoundSql(BoundSqlBuilder builder, String sql, Object[] param) {
		return new ArrayBoundSql(sql,param);
	}
}
