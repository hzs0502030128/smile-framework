package org.smile.orm.base.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.smile.orm.mapping.OrmTableMapping;
/**
 * 用于插入orm对象时数据封装
 * @author 胡真山
 */
public class OrmTableInsertBoundSql extends OrmTableBoundSql{
	/**
	 * 如果是批量操作时候可以不设置当前的操作参数
	 * 会对批量时列表的每一个元素进行设置
	 * @param type 表映射 
	 */
	public OrmTableInsertBoundSql(OrmTableMapping type) {
		super(type);
		this.sql = type.getInsertSql();
	}
	/**
	 * @param type 表映射
	 * @param param 要插入的对象
	 */
	public OrmTableInsertBoundSql(OrmTableMapping type,Object param) {
		super(type, param);
		this.sql = type.getInsertSql();
	}

	@Override
	public void fillStatement(PreparedStatement ps) throws SQLException {
		filler.fillInsertPreparedStatement(type, ps, getParams());
	}

	@Override
	public void fillBatchStatement(PreparedStatement ps, Object param) throws SQLException {
		filler.fillInsertPreparedStatement(type, ps, param);
	}

}
