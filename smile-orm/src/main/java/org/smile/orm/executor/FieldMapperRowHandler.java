package org.smile.orm.executor;


import java.sql.SQLException;
import java.util.Collection;

import org.smile.db.Transaction;
import org.smile.orm.base.impl.OrmTableRowHandler;
import org.smile.orm.mapping.OrmObjMapping;
/**
 *      把结果集转成Bean的RowHandler
 * @author strive
 */
public class FieldMapperRowHandler extends OrmTableRowHandler {
	
	protected static final String INFO="not implements this method ,do no thing";
	
	public FieldMapperRowHandler(OrmObjMapping ormMapper) {
		this.ormMapping=ormMapper;
	}
	
	@Override
	public boolean needDoRelate() {
		return false;
	}

	@Override
	public <E> void doRelate(Collection<E> dataList,Transaction conn) throws SQLException {
		logger.debug(INFO);
	}

}
