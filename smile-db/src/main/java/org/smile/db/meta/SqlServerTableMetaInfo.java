package org.smile.db.meta;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.smile.db.handler.ResultSetMap;
import org.smile.db.sql.BasicTransaction;
import org.smile.db.sql.SQLRunner;
import org.smile.util.StringUtils;

public class SqlServerTableMetaInfo extends TableMetaInfo {

	public SqlServerTableMetaInfo(String tableName) {
		super(tableName);
	}

	@Override
	protected void initTableFieldRemark(Connection conn) throws SQLException {
		SQLRunner runner=new SQLRunner();
		runner.setTransaction(new BasicTransaction(conn));
		List<ResultSetMap> list=runner.query(getFieldRemarkSql(), tableName);
		for(ResultSetMap map:list){
			ColumnInfo columninfo=getColumnInfo(map.getString("name"));
			if(columninfo!=null){
				String remark=map.getString("remark");
				if(StringUtils.notEmpty(remark)){
					columninfo.setRemark(remark);
				}
			}
		}
	}

	@Override
	protected void initTableRemark(Connection conn) throws SQLException {
		SQLRunner runner=new SQLRunner();
		runner.setTransaction(new BasicTransaction(conn));
		List<ResultSetMap> list=runner.query(getTableRemarkSql(), tableName);
		for(ResultSetMap map:list){
			remark=map.getString("remark");
		}
	}
	
	protected String getFieldRemarkSql(){
		String sql="SELECT a.name,CAST (isnull(e.[value], '') AS nvarchar (100) ) AS remark FROM sys.columns a "+
		"INNER JOIN sys.objects c ON a.object_id = c.object_id "+
		"AND c.type = 'u' LEFT JOIN sys.extended_properties e ON e.major_id = c.object_id AND e.minor_id = a.column_id "+
		"AND e.class = 1 WHERE c.name =? ";
		return sql;
	}
	
	protected String getTableRemarkSql(){
		String sql="SELECT c.name, CAST ( isnull(f.[value], '') AS nvarchar (100) ) AS remark "+
		"FROM sys.objects c LEFT JOIN sys.extended_properties f ON f.major_id = c.object_id "+
		"AND f.minor_id = 0 AND f.class = 1 "+
		"WHERE c.type = 'u' and c.name =?";
		return sql;
	}

}
