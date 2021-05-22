package org.smile.db.sql.parameter;

import java.sql.SQLException;
import java.util.Map;
/**
 * 对参数设置
 * @author 胡真山
 *
 */
public interface ParameterHandler extends ParameterFiller{
	/**
	 * 设置批量操作
	 * @param batchMap
	 */
	public abstract void setBatchMap(Map batchMap);
	/**
	 * 获取属性值
	 * @param value
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public abstract Object getValue(Object value, String name);

}