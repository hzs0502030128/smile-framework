package org.smile.orm.plugin;

import org.smile.commons.ann.Intercept;
import org.smile.db.sql.BoundSql;
import org.smile.db.sql.page.DialectPage;
import org.smile.log.LoggerHandler;
import org.smile.orm.dao.ExecutorBoundHandler;
import org.smile.plugin.Interceptor;
import org.smile.plugin.Invocation;
import org.smile.plugin.Plugin;
@Intercept(type=ExecutorBoundHandler.class,method="buildBoundSql")
public class TopInterceptor implements Interceptor,LoggerHandler{

	@Override
	public Object plugin(Object target) {
		if(target instanceof ExecutorBoundHandler){
			return Plugin.wrap(target, this);
		}
		return target;
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Integer topParam=TopHelper.getTop();
		if(topParam==null){
			return invocation.proceed();
		}else{
			try{
				BoundSql boundSql=invocation.proceed();
				String sql=boundSql.getSql();
				ExecutorBoundHandler executor=invocation.getTarget();
				DialectPage dialectPage=executor.getExecutor().getDialect().getDialectPage(sql);
				sql=dialectPage.getTopSql(topParam);
				boundSql.setSql(sql);
				return boundSql;
			}finally{
				TopHelper.remove();
			}
		}
	}

}
