package org.smile.db.sql.page;
/**
 * 方言分页语句接口
 * @author strive
 *
 */
public interface DialectPage {
	/**
	 * 总条数语句
	 * @return
	 */
	public  abstract String getCountSql();
	/**
	 * 数据库句
	 * @return
	 */
	public abstract  String getDataSql(int page,int size);
	/**
	 * 
	 * @param offset 偏移行数
	 * @param limit 查询条数
	 * @return
	 */
	public String getLimitSql(long offset,int limit);
	
	public abstract String getTopSql(int top);
	
	public abstract void setTotal(long total);
	
	public String getSql();
}
