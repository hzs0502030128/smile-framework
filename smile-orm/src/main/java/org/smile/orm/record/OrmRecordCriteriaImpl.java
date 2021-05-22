package org.smile.orm.record;

import org.smile.db.jdbc.RecordCriteriaImpl;
/**
 * 使用jdbc实现的一个Criteria操作
 * @author 胡真山
 *
 */
public class OrmRecordCriteriaImpl<E> extends RecordCriteriaImpl<E>{
	
	public OrmRecordCriteriaImpl(RecordDaoImpl<E> recordDao) {
		super(recordDao);
	}

}
