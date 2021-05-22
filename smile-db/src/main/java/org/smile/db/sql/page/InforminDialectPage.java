package org.smile.db.sql.page;


/**
 * MySQL分页方言
 * @author Administrator
 *
 */
public class InforminDialectPage extends  AbstractDialectPage {

	public InforminDialectPage(String sql){
		super(sql);
	}
	@Override
	public String getDataSql(int page,int size) {
		int firstRow=(page-1)*size;
		return getLimitSql(firstRow, size);
	}

	@Override
	public String getLimitSql(long offset, int limit) {
		StringBuilder sqlBuilder = new StringBuilder(sql.length() + 40);
        sqlBuilder.append("SELECT SKIP ").append(offset).append(" FIRST ").append(limit).append(" * FROM ( ");
        sqlBuilder.append(sql);
        sqlBuilder.append(" ) RESULT_TABLE_T");
        return sqlBuilder.toString();
	}


}
