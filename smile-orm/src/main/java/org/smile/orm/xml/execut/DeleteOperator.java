package org.smile.orm.xml.execut;

import java.lang.reflect.Method;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.smile.db.Transaction;
import org.smile.db.sql.SQLRunner;
import org.smile.orm.OrmApplication;
import org.smile.orm.executor.MappedOperator;
import org.smile.orm.mapping.BoundOrmTableMapping;
import org.smile.orm.mapping.OrmTableMapping;
import org.smile.template.StringTemplate;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "delete")
public class DeleteOperator extends Operator{

	@Override
	public SQLRunner createSQLRunner(Transaction conn,MappedOperator operator,Object param) {
		return new SQLRunner(conn);
	}

	@Override
	protected void initAnnotationSql(OrmApplication application, OrmTableMapping mapping, Method method) {
		sql=mapping.getDeleteByIdSql();
	}

	@Override
	public StringTemplate initSimpleSqlTemplateFromExeParam(String scriptType, BoundOrmTableMapping tableMapper) {
		return new StringTemplate(scriptType, tableMapper.getDeleteByIdSql());
	}


}
