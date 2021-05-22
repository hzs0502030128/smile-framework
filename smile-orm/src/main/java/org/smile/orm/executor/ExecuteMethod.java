package org.smile.orm.executor;

import java.sql.SQLException;

import org.smile.beans.converter.BeanException;
import org.smile.orm.dao.Executor;
/**
 * 操作执行  可以通过plugin拦截这个接口的方法
 * update insert select batch 
 * @author 胡真山
 * 2015年10月29日
 */
public interface  ExecuteMethod {
	/**
	 * 操作
	 * @param executor
	 * @param operator
	 * @param param
	 * @return
	 * @throws SQLException
	 * @throws BeanException
	 */
	public Object execute(Executor executor,MappedOperator operator,Object param) throws SQLException, BeanException;
}
