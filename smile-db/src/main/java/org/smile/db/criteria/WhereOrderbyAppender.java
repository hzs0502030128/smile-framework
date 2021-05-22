package org.smile.db.criteria;

import java.util.List;

public class WhereOrderbyAppender {
	/**
	 *	向sql中添加查询条件和排序字段的处理
	 * @param criterion
	 */
	public String doAppend(String sql,List<Criterion> where,List<OrderbyCriterion> criterion) {
		return sql;
	}
	
}
