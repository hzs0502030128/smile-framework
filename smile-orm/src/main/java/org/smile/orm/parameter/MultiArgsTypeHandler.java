package org.smile.orm.parameter;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.smile.collection.ArrayUtils;
import org.smile.collection.ExtendsMap;
import org.smile.collection.WrapBeanAsMap;
import org.smile.db.sql.BoundSql;
import org.smile.orm.executor.SqlBuilder;
/***
 * 多参数方法参数处理类
 * @author 胡真山
 * @date 2019年5月25日
 *
 */
public class MultiArgsTypeHandler implements ParameterTypeHandler {

	@Override
	public BoundSql handleBoundSql(SqlBuilder builder,ParameterType parameterType, Object param) throws SQLException {
		Object[] args=(Object[])param;
		Map<String,Object> argsMap;
		//第一个参数可做为主参数
		Object mainArgs=args[0];
		if(mainArgs==null||ParameterType.isOneFieldParameter(mainArgs)){
			argsMap=new HashMap<String,Object>();
		}else{
			if(mainArgs instanceof Map){
				argsMap=(Map) mainArgs;
			}else{
				argsMap=new WrapBeanAsMap(mainArgs);
			}
		}
		String[] names=parameterType.getParamNames();
		if(ArrayUtils.notEmpty(names)){
			if(names.length!=args.length){
				throw new SQLException("args "+args+" not support method parameters ");
			}
			argsMap=new ExtendsMap<String, Object>(argsMap);
			for(int i=0;i<names.length;i++){
				argsMap.put(names[i], args[i]);
			}
		}
		return builder.buildSql(argsMap, argsMap);
	}

}
