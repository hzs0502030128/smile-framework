package org.smile.db.sql.page;


/**
 * MySQL分页方言
 * @author 胡真山
 *
 */
public class MySQLDialectPage extends  AbstractDialectPage {

	public MySQLDialectPage(String sql){
		super(sql);
	}
	@Override
	public String getDataSql(int page,int size) {
		int firstRow=(page-1)*size;
		return getLimitSql(firstRow, size);
	}

	@Override
	public String getLimitSql(long offset, int limit) {
		StringBuilder querySql=new StringBuilder(sql.length()+30);
		querySql.append(sql);
		querySql.append(" LIMIT ").append(offset).append(",").append(limit);
		return querySql.toString();
	}


}
