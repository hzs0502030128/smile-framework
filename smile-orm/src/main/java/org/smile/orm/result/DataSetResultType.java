package org.smile.orm.result;

import java.sql.SQLException;

import org.smile.dataset.DataSet;
import org.smile.db.handler.RowHandler;
import org.smile.db.sql.BoundSql;
import org.smile.db.sql.SQLRunner;
/**
 *      查询结果返回dataset类型
 * @author 胡真山
 *
 */
public class DataSetResultType implements ResultType{

	@Override
	public Class getGenericType() {
		return null;
	}

	@Override
	public Class getType() {
		return DataSet.class;
	}

	@Override
	public boolean isSingleObj() {
		return false;
	}

	@Override
	public boolean isOneFieldType() {
		return false;
	}

	@Override
	public boolean isBooleanType() {
		return false;
	}

	@Override
	public void onInit() {
		
	}
	
	@Override
	public RowHandler createRowHandler() {
		//在executeQuery方法中使用的是queryDataSet方法，无需使用handler对数据集进行封装
		return null;
	}

	@Override
	public Object executeQuery(SQLRunner runner, BoundSql boundSql) throws SQLException {
		return runner.queryDataSet(boundSql);
	}
	
}
