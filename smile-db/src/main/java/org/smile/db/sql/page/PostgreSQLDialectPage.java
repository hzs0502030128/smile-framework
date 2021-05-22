package org.smile.db.sql.page;

/**
 * MySQL分页方言
 * @author 胡真山
 *
 */
public class PostgreSQLDialectPage extends  AbstractDialectPage {

	public PostgreSQLDialectPage(String sql){
		super(sql);
	}
	@Override
	public String getDataSql(int page,int size) {
		int offset=(page-1)*size;
		return getLimitSql(offset, size);
	}

	@Override
	public String getTopSql(int top) {
		return getDataSql(1, top);
	}

	@Override
	public String getLimitSql(long offset, int limit) {
		StringBuilder sqlBuilder = new StringBuilder(sql.length() + 14);
		sqlBuilder.append("SELECT * FROM (");
		sqlBuilder.append(sql);
        sqlBuilder.append(") T_DATA LIMIT ").append(limit).append(" OFFSET ").append(offset);
        return sqlBuilder.toString();
	}

}
