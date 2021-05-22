package org.smile.orm.executor.sql;

import java.sql.SQLException;

import org.smile.db.sql.BoundSql;
import org.smile.db.sql.NamedBoundSql;
import org.smile.orm.executor.BoundSqlBuilder;

/**
 * 使用:name 文件占位符
 * @author 胡真山
 *
 */
public class NamedBaseSqlConverter extends BaseSqlConverter {

	@Override
	public BoundSql buildBoundSql(BoundSqlBuilder builder, String sql, Object param) throws SQLException {
		return new NamedBoundSql(sql, param);
	}
}
