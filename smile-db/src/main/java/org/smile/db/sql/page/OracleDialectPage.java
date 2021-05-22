package org.smile.db.sql.page;

/**
 * oracle分页方言
 * @author Administrator
 *
 */
public class OracleDialectPage extends  AbstractDialectPage {

	
	public OracleDialectPage(String sql){
		super(sql);
	}
	
	public String getDataSql(int page,int size) {
		int offset=(page-1)*size;
		return getLimitSql(offset, size);
	}

	@Override
	public String getTopSql(int top) {
		return "select * from ("+sql+") where rownum <="+top;
	}

	@Override
	public String getLimitSql(long offset, int limit) {
		StringBuilder querySql=new StringBuilder(sql.length()+50);
		querySql.append("SELECT * FROM(SELECT DATA_TABLE_T.*, rownum ROW_NUM FROM(");
		querySql.append(sql).append(") DATA_TABLE_T WHERE rownum <=");
		querySql.append(offset+limit).append(") WHERE ROW_NUM >").append(offset);
		return querySql.toString();
	}

}
