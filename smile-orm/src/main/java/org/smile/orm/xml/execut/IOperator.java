/**
 * 
 */
package org.smile.orm.xml.execut;

import java.lang.reflect.Method;
import java.sql.SQLException;

import org.smile.db.Transaction;
import org.smile.db.sql.BoundSql;
import org.smile.db.sql.SQLRunner;
import org.smile.orm.OrmApplication;
import org.smile.orm.ann.Sql;
import org.smile.orm.executor.MappedOperator;
import org.smile.orm.mapping.BoundOrmTableMapping;
import org.smile.orm.result.ResultType;
import org.smile.template.StringTemplate;

/**
 * @author 胡真山
 *
 */
public interface IOperator {
	/***
	 * 创建sql runner 
	 * 会根据不同的操作实现设置数据转换的处理类
	 * @param transaction 事务
	 * @param operator 一个方法操作
	 * @return
	 */
	public abstract SQLRunner createSQLRunner(Transaction transaction,MappedOperator operator,Object param);
	/**
	 * 执行语句操作
	 * @param runner
	 * @param pType
	 * @param boundSql
	 * @return
	 * @throws SQLException
	 */
	public abstract Object execute(SQLRunner runner,ResultType pType,BoundSql boundSql)  throws SQLException;
	
	/**
	 * 操作方法的id
	 * @return
	 */
	public abstract String getId();
	/**
	 * 操作反回的mapper配置
	 * @return
	 */
	public abstract String getMapper();
	/**
	 * 返回sql语句属性的配置
	 * @return
	 */
	public abstract String getSql();
	/**
	 * 模板类型配置
	 * @return
	 */
	public abstract String getTemplate();
	/**
	 * 包含文件配置属性
	 * @return
	 */
	public abstract String getInclude();
	/**
	 * 返回语句类型的配置
	 * @return
	 */
	public abstract String getSqlType();

	/**
	 * 初始化一个注解操作  
	 * 从注解初始化方式初始化  
	 * 如果 不能初始化出sql 语句  会抛出一个 OrmInitException
	 * @param application 
	 * @param sqlAnn
	 * @param method
	 */
	public abstract void initFormAnnotation(OrmApplication application, Sql sqlAnn, Method method);

	/**
	 * 初始化简单的SQL语句从执行时的对象的tableMapper
	 * @param scriptType 模板的脚本语言类型
	 * @param tableMapper 
	 * @return
	 */
	public abstract StringTemplate initSimpleSqlTemplateFromExeParam(String scriptType, BoundOrmTableMapping tableMapper);
	/**
	 * 更新sqlType属性的值
	 * @param sqlType
	 */
	public abstract void updateSqlType(String sqlType);

}
