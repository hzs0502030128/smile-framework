package org.smile.orm.xml.execut;

import java.lang.reflect.Method;
import java.sql.SQLException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.smile.db.DbConstans;
import org.smile.db.Transaction;
import org.smile.db.sql.BoundSql;
import org.smile.db.sql.SQLRunner;
import org.smile.orm.OrmApplication;
import org.smile.orm.dao.TargetImplementMethodException;
import org.smile.orm.executor.MappedOperator;
import org.smile.orm.mapping.BoundOrmTableMapping;
import org.smile.orm.mapping.OrmTableMapping;
import org.smile.orm.result.ResultType;
import org.smile.orm.result.VoidResultType;
import org.smile.template.StringTemplate;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "select")
public class SelectOperator extends Operator {
	@Override
	public SQLRunner createSQLRunner(Transaction conn,MappedOperator operator,Object param) {
		ResultType type = operator.getResultType();
		if(type instanceof VoidResultType){
			throw new TargetImplementMethodException(DbConstans.SELECT+" not support void method "+operator.getId());
		}else{
			return new SQLRunner(conn,type.createRowHandler());
		}
	}

	@Override
	public Object execute(SQLRunner runner, ResultType pType, BoundSql boundSql) throws SQLException {
		return pType.executeQuery(runner, boundSql);
	}

	@Override
	protected void initAnnotationSql(OrmApplication application, OrmTableMapping mapping, Method method) {
		sql=mapping.getSelectByIdSql();
	}

	@Override
	public StringTemplate initSimpleSqlTemplateFromExeParam(String scriptType, BoundOrmTableMapping tableMapper) {
		return new StringTemplate(scriptType, tableMapper.getSelectByIdSql());
	}

}
