package org.smile.db.sql.page;


public class RowNumberDialectPage extends  AbstractDialectPage {

	
	public RowNumberDialectPage(String sql){
		super(sql);
	}
	
	public String getDataSql(int page,int size) {
		int offset=(page-1)*size;
		return getLimitSql(offset, size);
	}

	@Override
	public String getLimitSql(long offset, int limit) {
		StringBuffer querySql=new StringBuffer(sql.length()+100);
		querySql.append("SELECT * FROM (SELECT DATA_TABLE_T.*,ROW_NUMBER() OVER () ROW_NUM FROM (");
		querySql.append(sql).append(") DATA_TABLE_T) RESULT_TABLE_T WHERE ROW_NUM>");
		querySql.append(offset).append(" AND ROW_NUM<=").append(offset+limit);
		return querySql.toString();
	}

}
