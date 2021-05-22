package org.smile.orm.plugin;

import java.util.ArrayList;
import java.util.List;

import org.smile.commons.ann.Intercept;
import org.smile.db.ListPageModel;
import org.smile.db.PageHelper;
import org.smile.db.PageModel;
import org.smile.db.PageParam;
import org.smile.log.LoggerHandler;
import org.smile.orm.OrmInitException;
import org.smile.orm.dao.Executor;
import org.smile.orm.executor.ExecuteMethod;
import org.smile.orm.executor.MappedOperator;
import org.smile.orm.executor.SelectMethod;
import org.smile.plugin.Interceptor;
import org.smile.plugin.Invocation;
import org.smile.plugin.Plugin;
import org.smile.util.StringUtils;
@Intercept(type=ExecuteMethod.class,method="execute")
public class PageQueryInterceptor implements Interceptor,LoggerHandler{

	private static final String COUNT_EXTENSION="Counts";
	
	@Override
	public Object plugin(Object target) {
		if(target instanceof SelectMethod){
			return Plugin.wrap(target, this);
		}
		return target;
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		PageParam pageParam=PageHelper.getPageParam();
		if(pageParam==null){
			return invocation.proceed();
		}else{
			try{
				Object[] args=invocation.getArgs();
				Executor executor=(Executor)args[0];
				MappedOperator operator=(MappedOperator)args[1];
				String countOperatorId=getCountMappedOperatorId(operator, pageParam);
				if(countOperatorId!=null){//存在自定义语句的时候
					MappedOperator countOperator=operator.getDaoMapper().getOperator(countOperatorId);
					if(countOperator==null){
						throw new OrmInitException("not exits method "+countOperatorId+" in "+operator.getDaoMapper().getName());
					}
					ListPageModel pageModel=new ListPageModel(pageParam.getPage(),pageParam.getPageSize());
					//移出分页信息执行行数计算语句,不移去会进入再次分页拦截
					PageHelper.remove();
					Number count=(Number)executor.execute(countOperator, args[2]);
					pageModel.setTotal(count.longValue());
					if(count.intValue()>0){
						List rows=(List)executor.queryLimit(operator, args[2], pageParam.getOffset(), pageParam.getPageSize());
						pageModel.setRows(rows);
					}else{
						pageModel.setRows(new ArrayList());
					}
					return pageModel;
				}else{
					PageModel pageModel=(PageModel)executor.queryPage(operator, args[2],pageParam.getPage() ,pageParam.getPageSize());
					return pageModel.toListPageModel();
				}
			}finally{
				PageHelper.remove();
			}
		}
	}
	/**
	 * 获取分页自定义方法ID
	 * @param operator
	 * @param pageParam
	 * @return
	 */
	protected String getCountMappedOperatorId(MappedOperator operator,PageParam pageParam){
		if(StringUtils.notEmpty(pageParam.getCountMethod())){
			return pageParam.getCountMethod();
		}else if(pageParam.isCountExtension()){
			return operator.getId()+COUNT_EXTENSION;
		}
		return null;
	}

}
