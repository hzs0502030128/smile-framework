package org.smile.db.meta;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.smile.collection.KeyNoCaseHashMap;
import org.smile.collection.NoCaseStringSet;
import org.smile.log.LoggerHandler;
import org.smile.util.StringUtils;

/**
 * 表结构信息
 * @author 胡真山
 * 2015年10月14日
 */
public class TableMetaInfo implements LoggerHandler{
	/**
	 * 表名
	 */
	protected String tableName;
	/**
	 * 表注释
	 */
	protected String remark;
	/**
	 * 列信息
	 */
	protected Map<String,ColumnInfo> columns=new KeyNoCaseHashMap<ColumnInfo>();
	
	protected Set<String> keys=new NoCaseStringSet();
	
	protected DatabaseMetaData databaseMetaData;
	
	protected ResultSetMetaData rsmd;
	
	public TableMetaInfo(String tableName){
		this.tableName=tableName.toLowerCase();
	}
	/**
	 * 获取字段的java类型信息
	 * @throws SQLException
	 */
	protected void initResultSetMetaData(Connection conn) throws SQLException{
		 databaseMetaData=conn.getMetaData();
		 Statement st=conn.createStatement();
		 try{
			String sql=getMetaSql(tableName);
			logger.debug(sql);
			ResultSet rs=st.executeQuery(sql);
			rsmd=rs.getMetaData();
			int columnCount=rsmd.getColumnCount();
			for(int s=1;s<=columnCount;s++){
				ColumnInfo columnInfo=new ColumnInfo();
				String column=rsmd.getColumnLabel(s).toLowerCase();
				columnInfo.setName(column);
				String type=rsmd.getColumnClassName(s);
				columnInfo.setClassName(type);
				columnInfo.setType(rsmd.getColumnTypeName(s));
				columnInfo.setLength(rsmd.getColumnDisplaySize(s));
				columns.put(column, columnInfo);
			}
		 }finally{
			 st.close();
		 }
	}
	
	/**
	  * 字段注释
	  * @param conn
	  * @param info
	  * @throws SQLException
	  */
	 protected void initTableFieldRemark(Connection conn) throws SQLException{
		//得到列注释
		ResultSet columnSet = databaseMetaData.getColumns(conn.getCatalog(), "%", tableName, "%");
		while(columnSet.next()){
			String column =columnSet.getString("COLUMN_NAME");
			ColumnInfo columninfo=getColumnInfo(column);
			if(columninfo!=null){
				String remark=columnSet.getString("REMARKS");
				if(StringUtils.notEmpty(remark)){
					columninfo.setRemark(remark);
				}
			}
		}
	 }
	 /**
	  * 表注释
	  * @param conn
	  * @throws SQLException
	  */
	 protected void initTableRemark(Connection conn) throws SQLException{
		//表的注释
		 ResultSet tableRs=databaseMetaData.getTables(conn.getCatalog(), "%", tableName, new String[]{"TABLE"});
		 while(tableRs.next()){
			 remark=tableRs.getString("REMARKS");
		 }
	 }
	 
	 protected void initTableKeys(Connection conn) throws SQLException{
		//得到主键
		ResultSet keySet=databaseMetaData.getPrimaryKeys(conn.getCatalog(), "dbo", tableName);
		while(keySet.next()){
			keys.add(keySet.getString("COLUMN_NAME"));
		}
	 }
	
	public ColumnInfo getColumnInfo(String name){
		return columns.get(name);
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Map<String, ColumnInfo> getColumns() {
		return columns;
	}

	public void setColumns(Map<String, ColumnInfo> columns) {
		this.columns = columns;
	}

	public ResultSetMetaData getRsmd() {
		return rsmd;
	}

	public void setRsmd(ResultSetMetaData rsmd) {
		this.rsmd = rsmd;
	}
	
	 protected String getMetaSql(String tableName){
		 return "SELECT * FROM "+tableName+" WHERE 0=1 ";
	 }
	
	public Collection<ColumnInfo> getAllColumns(){
		return columns.values();
	}
	
	public boolean isKey(String column){
		return this.keys.contains(column);
	}
}
