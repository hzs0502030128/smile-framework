package org.smile.db.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;

import org.smile.db.sql.parameter.ArrayParameterFiller;
/***
 * 基本的数据参数绑定
 * 数组的值直接对应语句中的占位符
 * @author 胡真山
 */
public class ArrayBoundSql extends BoundSql{
	
	protected Object[] arrayParams;
	
	protected ArrayParameterFiller filler;
	
	public ArrayBoundSql(String sql){
		this.sql=sql;
		filler=new ArrayParameterFiller();
	}
	
	public ArrayBoundSql(String sql,Object[] params){
		this.sql=sql;
		this.arrayParams=params;
		filler=new ArrayParameterFiller();
	}

	@Override
	public void fillStatement(PreparedStatement ps) throws SQLException {
		filler.fillObject(ps, arrayParams);
	}

	@Override
	public Object getParams() {
		return arrayParams;
	}

	@Override
	public void fillBatchStatement(PreparedStatement ps, Object param) throws SQLException {
		if(param instanceof Object[]){
			filler.fillObject(ps, param);
		}else{
			filler.fillObject(ps, 1, param);
		}
	}

	@Override
	public Iterator iteratorMapping() {
		return new Iterator<Object>() {
			int index=0;
			@Override
			public boolean hasNext() {
				return index<arrayParams.length-1;
			}

			@Override
			public Object next() {
				return arrayParams[index++];
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("can't remove ");
			}
		};
	}
}
