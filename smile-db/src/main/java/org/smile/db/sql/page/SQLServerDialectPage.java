package org.smile.db.sql.page;

import java.sql.SQLException;
import java.util.regex.Matcher;

import org.smile.commons.Strings;
import org.smile.util.RegExp;
import org.smile.util.RegExp.MatchInfo;
import org.smile.util.StringUtils;

public class SQLServerDialectPage extends  AbstractDialectPage implements Strings{
	/**把排序子句分离出来 */
	protected String orderby;
	/**with子句*/
	protected String withSql;
	/**匹配with语句的正则表达式*/
	protected static final RegExp withReg=new RegExp("with .+ as ?\\(.+\\) ?select ",false);
	
	protected boolean hasOrderby=true;
	
	public SQLServerDialectPage(String querySql) throws SQLException{
		super(querySql);
		//是否是with开头
		if(this.sql.substring(0, 4).equalsIgnoreCase(WITH)){
			doWithSql();
		}
		//最后匹配的结束
		MatchInfo info=ORDER_BY_REG.lastMate(sql);
		//处理后的sql
		if(info!=null){
			orderby=sql.substring(info.getEnd(),sql.length());
			sql=sql.substring(0,info.getStart());
		}else{
			hasOrderby=false;
			orderby="CURRENT_TIMESTAMP";
		}
	}
	/**
	 * 处理with子句 把with子句提取出来
	 */
	private void doWithSql(){
		Matcher m=withReg.matcher(sql);
		MatchInfo info=withReg.firstMatch(m, 0);
		withSql=info.getContext().substring(0,info.getEnd()-7);
		sql=sql.substring(info.getEnd()-7);
	}
	
	public String getCountSql() {
		StringBuffer querySql=new StringBuffer(sql.length()+50);
		if(StringUtils.notEmpty(withSql)){
			querySql.append(withSql);
		}
		querySql.append("SELECT COUNT(0) FROM (").append(sql).append(") COUNT_TABLE_T");
		//条数sql
		return querySql.toString();
	}
	@Override
	public String getDataSql(int page,int size) {
		int offset=(page-1)*size;
		return getLimitSql(offset, size);
	}

	@Override
	public String getTopSql(int top) {
		StringBuilder querySql=new StringBuilder(sql.length()+50);
		if(StringUtils.notEmpty(withSql)){
			querySql.append(withSql);
		}
		if(hasOrderby){
			int index=SELECT_REG.firstIndexEnd(sql);
			querySql.append("SELECT TOP ").append(top).append(" ").append(sql.substring(index)).append(" ORDER BY ").append(orderby);
		}else{
			querySql.append("SELECT TOP ").append(top).append(" * FROM (").append(sql).append(") DATA_TABLE_T");
		}
		return querySql.toString();
	}
	
	@Override
	public String getLimitSql(long offset, int limit) {
		StringBuffer querySql=new StringBuffer(sql.length()+100);
		if(StringUtils.notEmpty(withSql)){
			querySql.append(withSql);
		}
		querySql.append("SELECT * FROM (SELECT TOP ").append(offset+limit).append(" DATA_TABLE_T.*,ROW_NUMBER() OVER (ORDER BY ").append(orderby);
		querySql.append(") ROW_NUM FROM (").append(sql).append(") DATA_TABLE_T) RESULT_TABLE_T WHERE ROW_NUM>").append(offset);
		//数据sql
		return querySql.toString();
	}
}
