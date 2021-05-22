package org.smile.orm.plugin;

import java.util.List;

import org.smile.collection.CollectionUtils;
import org.smile.commons.ann.Intercept;
import org.smile.db.criteria.OrderbyCriterion;
import org.smile.log.LoggerHandler;
import org.smile.orm.executor.MappedOperator;
import org.smile.orm.executor.sql.SqlConverter;
import org.smile.orm.xml.execut.SelectOperator;
import org.smile.plugin.Interceptor;
import org.smile.plugin.Invocation;
import org.smile.plugin.Plugin;
@Intercept(type=SqlConverter.class,method="convertToSql")
public class OrderbyInterceptor implements Interceptor,LoggerHandler{

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		List<OrderbyCriterion> pageParam=OrderbyHelper.getOrderby();
		if(CollectionUtils.isEmpty(pageParam)){
			return invocation.proceed();
		}else{
			try{
				Object[] args=invocation.getArgs();
				MappedOperator operator=(MappedOperator)args[1];
				if(operator.getOperator() instanceof SelectOperator){
					String sql=(String)(args[0]);
					invocation.setArgs(new Object[]{doOrderby(sql, pageParam),args[1],args[2]});
				}
				return invocation.proceed();
			}finally{
				OrderbyHelper.remove();
			}
		}
	}

	private String doOrderby(String sql,List<OrderbyCriterion> orderby){
		StringBuffer sb=new StringBuffer(sql);
		sb.append(" ORDER BY ");
		int index=0;
		for(OrderbyCriterion criterion:orderby){
			sb.append(criterion.getFieldName()).append(" ").append(criterion.getDesc());
			if(index<orderby.size()-1){
				sb.append(",");
			}
		}
		return sb.toString();
	}
}
