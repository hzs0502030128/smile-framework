package org.smile.orm.xml.execut;

import java.lang.reflect.Method;
import java.sql.SQLException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.smile.db.Transaction;
import org.smile.db.sql.BoundSql;
import org.smile.db.sql.SQLRunner;
import org.smile.orm.OrmApplication;
import org.smile.orm.OrmInitException;
import org.smile.orm.executor.MappedOperator;
import org.smile.orm.mapping.BoundOrmTableMapping;
import org.smile.orm.mapping.OrmTableMapping;
import org.smile.orm.result.ResultType;
import org.smile.template.StringTemplate;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "batch")
public class BatchOperator extends Operator{
	@XmlAttribute
	private String type;
	
	@Override
	public SQLRunner createSQLRunner(Transaction conn,MappedOperator operator,Object param) {
		return new SQLRunner(conn);
	}

	@Override
	public Object execute(SQLRunner runner, ResultType pType, BoundSql boundSql)
			throws SQLException {
		throw new SQLException("not support method ");
	}

	@Override
	protected void initAnnotationSql(OrmApplication application, OrmTableMapping mapping, Method method) {
		initSql(mapping);
	}
	
	private void initSql(OrmTableMapping tableMapping){
		if(type.equals(UPDATE)){
			sql=tableMapping.getUpdateByIdSql();
		}else if(type.equals(INSERT)){
			sql=tableMapping.getInsertSql();
		}else if(type.equals(DELETE)){
			sql=tableMapping.getDeleteByIdSql();
		}else{
			throw new OrmInitException("when sql is null then type must not null ,type may designate insert update delete ");
		}
	}

	@Override
	public StringTemplate initSimpleSqlTemplateFromExeParam(String scriptType, BoundOrmTableMapping tableMapper) {
		initSql(tableMapper);
		return new StringTemplate(scriptType, sql);
	}


}
