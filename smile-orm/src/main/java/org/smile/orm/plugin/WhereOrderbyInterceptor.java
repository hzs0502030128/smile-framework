package org.smile.orm.plugin;

import java.util.List;

import org.smile.collection.CollectionUtils;
import org.smile.commons.ann.Intercept;
import org.smile.db.criteria.Criterion;
import org.smile.db.criteria.OrderbyCriterion;
import org.smile.db.criteria.WhereOrderbyAppender;
import org.smile.log.LoggerHandler;
import org.smile.orm.executor.sql.SqlConverter;
import org.smile.plugin.Interceptor;
import org.smile.plugin.Invocation;
import org.smile.plugin.Plugin;
@Intercept(type=SqlConverter.class,method="convertToSql")
public class WhereOrderbyInterceptor implements Interceptor,LoggerHandler{
	/**
	 * 用于处理添加where条件和orderby排序字段
	 */
	private WhereOrderbyAppender whereOrderbyAppender=new WhereOrderbyAppender(); 
	
	@Override
	public Object plugin(Object target) {
		if(target instanceof SqlConverter) {
			return Plugin.wrap(target, this);
		}
		return target;
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		//查询语句
		String sql=invocation.proceed();
		List<OrderbyCriterion> orderby=OrderbyHelper.getOrderby();
		List<Criterion> where=WhereHelper.getWhere();
		if(CollectionUtils.isEmpty(orderby)&&CollectionUtils.isEmpty(where)){
			return invocation.proceed();
		}else{
			try{
				return whereOrderbyAppender.doAppend(sql, where, orderby);
			}finally{
				OrderbyHelper.remove();
				WhereHelper.remove();
			}
		}
	}

	public void setWhereOrderbyAppender(WhereOrderbyAppender whereOrderbyAppender) {
		this.whereOrderbyAppender = whereOrderbyAppender;
	}
	
	
}
