package org.smile.orm.plugin;

import java.util.List;

import org.smile.db.criteria.Criterion;

public class WhereHelper {
	/***
	 * where 查询条件
	 */
	private static final ThreadLocal<List<Criterion>> whereCriterions=new ThreadLocal<List<Criterion>>();
	
	public static void startWhere(List<Criterion> criterions) {
		whereCriterions.set(criterions);
	}
	
	public static void remove() {
		whereCriterions.remove();
	}
	
	public static List<Criterion> getWhere(){
		return whereCriterions.get();
	}
	
}
