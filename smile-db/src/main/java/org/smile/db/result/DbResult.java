package org.smile.db.result;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.smile.collection.DataGetter;

public interface DbResult extends DataGetter<String>{
	/**
	 * 
	 * @param column
	 * @return
	 */
	public Object getObject(String column);
	/**
	 * 
	 * @param column
	 * @return
	 * @throws SQLException 
	 */
	@Override
	public String getString(String column);
	/**
	 * 
	 * @param column
	 * @return
	 */
	@Override
	public BigDecimal getBigDecimal(String column);
	/**
	 * 
	 * @param column
	 * @return
	 */
	@Override
	public Long getLong(String column);
	/**
	 * 
	 * @param column
	 * @return
	 */
	@Override
	public Integer getInt(String column);
	/**
	 * 
	 * @param column
	 * @return
	 */
	public byte[] getBytes(String column);
	
}
