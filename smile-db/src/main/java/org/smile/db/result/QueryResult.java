package org.smile.db.result;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.smile.beans.property.PropertyConverter;
import org.smile.commons.Column;
import org.smile.log.LoggerHandler;
import org.smile.util.RegExp;
import org.smile.util.StringUtils;

/**
 * 数据库查询的结果集 
 * 是对 {@link ResultSet} 的一个简单封闭
 * @author 胡真山
 * @Date 2016年1月8日
 */
public class QueryResult implements LoggerHandler {
	/**列名是否需要小写*/
	private static boolean ColumsNameLowerCase=false;
	/**大写字母列正则*/
	private RegExp upperColumnNameReg=new RegExp("[A-Z0-9_]+");
	/**真正的结果集*/
	protected ResultSet rs;
	/**结果集的元数据 结构信息*/
	protected ResultSetMetaData rsmd;
	/**列数*/
	protected int columnCount;
	/**转换的的映射列对象*/
	protected List<Column> indexColumns;
	/**所有的列名*/
	private List<String> columnsNames;
	/**列名映射*/
	private Map<Integer,String> labelNameIndexs=new HashMap<Integer,String>();
	/**
	 * 从数据库结果集构建查询结果
	 * @param rs
	 * @throws SQLException
	 */
	public QueryResult(ResultSet rs) throws SQLException {
		this.rs = rs;
		this.rsmd = rs.getMetaData();
		this.columnCount = rsmd.getColumnCount();
	}

	/**
	 * 以列名获取获果集字段
	 * @param columnName
	 * @return
	 * @throws SQLException
	 */
	public Object getObject(String columnName) throws SQLException {
		return rs.getObject(columnName);

	}

	/**
	 * 以索引获取结果集字段
	 * @param index
	 * @return
	 * @throws SQLException
	 */
	public Object getObject(int index) throws SQLException {
		return rs.getObject(index);
	}

	/**
	 * 获取一列的值
	 * @param key 列名
	 * @param type 转换的类型
	 * @return
	 * @throws SQLException
	 */
	public <T> T getObject(String key, Class<T> type) throws SQLException {
		return ColumnUtils.getColumn(type, rs, key);
	}

	/**
	 * 获取一个列的值
	 * @param index 列索引
	 * @param type 转换类型
	 * @return
	 * @throws SQLException
	 */
	public <T> T getObject(int index, Class<T> type) throws SQLException {
		return ColumnUtils.getColumn(type, rs, index);
	}

	public String getString(int index) throws SQLException {
		return rs.getString(index);
	}

	public Long getLong(int index) throws SQLException {
		return rs.getLong(index);
	}

	public Integer getInteger(int index) throws SQLException {
		return rs.getInt(index);
	}

	public Boolean getBoolean(int index) throws SQLException {
		return rs.getBoolean(index);
	}

	/***
	 * 获取结果集的列数
	 * @return
	 */
	public int getColumnCount() {
		return columnCount;
	}

	/**
	 * 以索引获取列名
	 * @param index
	 * @return
	 * @throws SQLException
	 */
	public String getColumnName(int index) throws SQLException {
		return rsmd.getColumnName(index);
	}

	/**
	 * 获取索引列的label名
	 * @param index
	 * @return
	 * @throws SQLException
	 */
	public String getColumnLabel(int index) throws SQLException {
		String columnLabel=labelNameIndexs.get(index);
		if(columnLabel!=null){
			return columnLabel;
		}
		columnLabel = rsmd.getColumnLabel(index);
		if (StringUtils.isEmpty(columnLabel)) {
			columnLabel=rsmd.getColumnName(index);
		}
		if(upperColumnNameReg.matches(columnLabel)||ColumsNameLowerCase){
			columnLabel=columnLabel.toLowerCase();
		}
		labelNameIndexs.put(index, columnLabel);
		return columnLabel;
	}

	public ResultSet getResultSet() {
		return rs;
	}

	public ResultSetMetaData getRsmd() {
		return rsmd;
	}

	/**
	 * 对数据库的列与属性做一个映射
	 * @param propertyConverter key 与 属性的一个 转换器
	 * @throws SQLException
	 */
	protected void mapColumnsToProperties(PropertyConverter propertyConverter) throws SQLException {
		int cols = rsmd.getColumnCount();
		List<String> columnsNames = new LinkedList<String>();
		List<Column> columnList = new LinkedList<Column>();
		for (int index = 1; index <= cols; index++) {
			String columnName = getColumnLabel(index);
			Column column;
			columnsNames.add(columnName);
			if(columnName.indexOf('.')>0){
				column=new ExpressDataBaseColumn(index, columnName);
			}else{
				column = propertyConverter.newColumn(index, columnName);
			}
			if (column != null) {
				columnList.add(column);
			} else if (logger.isDebugEnabled()) {
				logger.debug("not mapping column " + index + "->" + columnName);
			}
		}
		this.columnsNames=columnsNames;
		this.indexColumns = columnList;
		if (logger.isDebugEnabled()) {
			logger.debug("return columns:" + columnsNames);
		}
	}
	
	public List<String> getColumnsNames() throws SQLException{
		List<String> columns=this.columnsNames;
		if(columns!=null){
			return columns;
		}else{
			columns = new LinkedList<String>();
			int cols = rsmd.getColumnCount();
			for (int index = 1; index <= cols; index++) {
				String columnName = getColumnLabel(index);
				columns.add(columnName);
			}
		}
		return this.columnsNames==null?(this.columnsNames=columns):this.columnsNames;
	}

	public List<Column> getIndexedColumns(PropertyConverter propertyConverter) throws SQLException {
		if (this.indexColumns == null) {
			mapColumnsToProperties(propertyConverter);
		}
		return indexColumns;
	}

	public int getColumnType(int index) throws SQLException {
		return rsmd.getColumnType(index);
	}

	public String getString(String column) throws SQLException {
		return rs.getString(column);
	}

	public BigDecimal getBigDecimal(String column) throws SQLException {
		return rs.getBigDecimal(column);
	}

	public Long getLong(String column) throws SQLException {
		return rs.getLong(column);
	}

	public Integer getInt(String column) throws SQLException {
		return rs.getInt(column);
	}

	public byte[] getBytes(String column) throws SQLException {
		return rs.getBytes(column);
	}
}
