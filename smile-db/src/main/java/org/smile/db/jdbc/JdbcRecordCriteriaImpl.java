package org.smile.db.jdbc;

/**
 * 使用jdbc实现的一个Criteria操作
 * @author 胡真山
 *
 */
public class JdbcRecordCriteriaImpl extends RecordCriteriaImpl<JdbcMapRecord>{
	
	public JdbcRecordCriteriaImpl(JdbcMapRecordDao recordDao) {
		super(recordDao);
	}
}
