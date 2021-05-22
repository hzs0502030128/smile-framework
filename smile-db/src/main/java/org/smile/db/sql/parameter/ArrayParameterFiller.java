package org.smile.db.sql.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 * 数组参数占位填充
 * @author 胡真山
 *
 */
public class ArrayParameterFiller extends AbstractParameterFiller{
	
	@Override
	public final void fillObject(PreparedStatement ps, Object param) throws SQLException {
		if (param != null) {
			Object[] params=(Object[])param;
	        //循环设置值
	        for (int i = 0; i < params.length; i++) {
	            fillObject(ps, i+1, params[i]);
	        }
        }
	}
}
