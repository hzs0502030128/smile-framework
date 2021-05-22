package org.smile.db.handler;

import java.sql.SQLException;
import java.util.Map;

import org.smile.beans.BeanUtils;
import org.smile.beans.converter.BeanException;
/**
 * 把一行结果集处理成一个Map的实现
 * 
 * @author 胡真山
 *
 */
public class ExpressMapRowHandler extends  MapRowHandler {

	protected ExpressMapRowHandler(Class mapClass) {
		super(mapClass);
	}

	@Override
	protected void setValue(Map<String,Object> result, String columnName, Object value) throws SQLException {
		if(columnName.indexOf('.')>0){//对带点的表达式的支持
        	try {
				BeanUtils.setExpValue(result, columnName, value);
			} catch (BeanException e) {
				throw new SQLException(e);
			}
        }else{
        	result.put(columnName, value);
        }
	}
	
	
}
