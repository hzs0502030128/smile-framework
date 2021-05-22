package org.smile.db.sql.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import org.smile.db.JsonDbSerializable;
import org.smile.db.result.ColumnUtils;
import org.smile.json.JSON;

public abstract class AbstractParameterFiller implements ParameterFiller{
	/**
	 * 把一个值设置到一个列中
	 * @param ps
	 * @param index 列索引
	 * @param columnValue 列的值
	 * @throws SQLException
	 */
	public void fillObject(PreparedStatement ps,int index ,Object columnValue)  throws SQLException{
		if (columnValue != null) {
        	Object valueStr=columnValue;
        	//可转成json字符串保存的对象
        	if(columnValue instanceof Collection||columnValue instanceof Map||columnValue instanceof JsonDbSerializable){
        		valueStr=JSON.toJSONString(columnValue);
        	}
            ColumnUtils.setColumn(ps,index, valueStr);
        } else {
        	ColumnUtils.setNull(ps,index);
        }
	}
	
	@Override
	public void fillBatchObject(PreparedStatement ps, Object value) throws SQLException {
		fillObject(ps, value);
	}
}
