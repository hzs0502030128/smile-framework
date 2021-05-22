package org.smile.db.meta;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.smile.db.DbConstans;
import org.smile.db.DbManager;
import org.smile.db.DbUtils;
import org.smile.db.Dialect;
import org.smile.db.config.JdbcConfig;
import org.smile.log.LoggerHandler;
import org.smile.util.RegExp;

/**
 * 获取表结构信息
 * @author 胡真山
 * 2015年10月28日
 */
public class TableMetaUtil implements LoggerHandler {

	private Dialect dialect = DbConstans.DIALECT;

	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}

	private JdbcConfig jdbcConfig;

	private DataSource ds;

	public TableMetaUtil() {
	}

	public TableMetaUtil(DataSource ds) {
		this.ds = ds;
	}

	/**
	 * 以jdbc配置
	 * @param config
	 */
	public TableMetaUtil(JdbcConfig config) {
		if (config == null) {
			try {
				this.ds = DbManager.getDefaultDataSource();
			} catch (NamingException e) {
				logger.error("获取默认连接失败", e);
			}
		}
		this.jdbcConfig = config;
	}

	/**
	 * 打开数据库连接
	 * @return
	 * @throws SQLException
	 */
	protected Connection openConnection() throws SQLException {
		if (ds != null) {
			return ds.getConnection();
		}
		return DbManager.getJdbcConnection(jdbcConfig);
	}

	/**
	 * 获取一个表名的信息
	 * @param conn
	 * @param tableName 表名
	 * @return
	 * @throws SQLException
	 */
	public TableMetaInfo getOneTableInfo(Connection conn, String tableName) throws SQLException {
		TableMetaInfo info = dialect.newTableMetaInfo(tableName);
		info.initResultSetMetaData(conn);
		info.initTableRemark(conn);
		info.initTableFieldRemark(conn);
		info.initTableKeys(conn);
		return info;
	}

	/**
	 * 获取表的信息
	 * @param tablenames  多个表名
	 * @return 
	 * @throws SQLException
	 */
	public List<TableMetaInfo> getTableMetaInfo(String... tablenames) throws SQLException {
		List<TableMetaInfo> infos = new LinkedList<TableMetaInfo>();
		Connection conn = null;
		try {
			conn = openConnection();
			for (String table : tablenames) {
				infos.add(getOneTableInfo(conn, table));
			}
		} finally {
			DbUtils.close(conn);
		}
		return infos;
	}

	/** 获取数据库中的所有表 */
	public List<String> getTableNames(String pattern) throws SQLException {
		List<String> tables = new ArrayList<String>();
		Connection conn = openConnection();
		try {
			DatabaseMetaData dbMetaData = conn.getMetaData();
			// 可为:"TABLE", "VIEW", "SYSTEM   TABLE",
			// "GLOBAL   TEMPORARY", "LOCAL   TEMPORARY", "ALIAS", "SYNONYM"
			String[] types = { "TABLE" };
			/* 只要表就好了 */
			ResultSet tabs = dbMetaData.getTables(null, null, null, types);
			RegExp tableNameReg = null;
			if (pattern != null) {
				tableNameReg = new RegExp(pattern, false);
			}
			/*
			 * 记录集的结构如下: TABLE_CAT String => table catalog (may be null)
			 * TABLE_SCHEM String => table schema (may be null) TABLE_NAME
			 * String => table name TABLE_TYPE String => table type. REMARKS
			 * String => explanatory comment on the table TYPE_CAT String => the
			 * types catalog (may be null) TYPE_SCHEM String => the types schema
			 * (may be null) TYPE_NAME String => type name (may be null)
			 * SELF_REFERENCING_COL_NAME String => name of the designated
			 * "identifier" column of a typed table (may be null) REF_GENERATION
			 * String => specifies how values in SELF_REFERENCING_COL_NAME are
			 * created. Values are "SYSTEM", "USER", "DERIVED". (may be null)
			 */
			while (tabs.next()) {
				// 只要表名这一列
				String tableName = String.valueOf(tabs.getObject("TABLE_NAME"));
				if (tableNameReg != null && tableNameReg.test(tableName)) {
					tables.add(tableName);
				}
			}
			return tables;
		} finally {
			DbUtils.close(conn);
		}
	}
}
