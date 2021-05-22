package org.smile.orm.base.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.smile.collection.CollectionUtils;
import org.smile.collection.SoftHashMap;
import org.smile.db.sql.ArrayBoundSql;
import org.smile.db.sql.BoundSql;
import org.smile.expression.EvaluateException;
import org.smile.expression.Expression;
import org.smile.expression.FieldNameExpression;
import org.smile.expression.NamedExpression;
import org.smile.expression.SpecialParameterExpression;
import org.smile.expression.visitor.BaseExpressionVisitor;
import org.smile.log.LoggerHandler;
import org.smile.orm.mapping.adapter.OrmMapperAdapter;
import org.smile.orm.mapping.adapter.OrmTableMapperAdapter;
import org.smile.orm.parser.OrmWhereSqlParser;
import org.smile.orm.parser.ParseException;
import org.smile.template.StringTemplateParser;
import org.smile.template.StringTemplateParser.BaseMacroResolver;
import org.smile.util.ObjectLenUtils;
import org.smile.util.RegExp;
import org.smile.util.RegExp.MatchInfo;
import org.smile.util.StringUtils;



public class OrmWhereSqlBoundBuilder extends BaseWhereSqlBoundBuilder implements LoggerHandler{
	/**对解析出的表达式进行缓存*/
	protected Map<String,Expression<?>> cacheExpression= SoftHashMap.newConcurrentInstance();
	
	protected final static RegExp ORDER_BY_REG=new RegExp(" order +by ",false); 
	
	protected final static RegExp BLANK_REG=new RegExp(" +");
	
	protected OrmMapperAdapter adapter=OrmTableMapperAdapter.instance;
	/**用来解析名称参数的where语句*/
	protected StringTemplateParser templateParser=new StringTemplateParser("#{", "}");
	
	@Override
	public BoundSql build(Class clazz,StringBuilder sql,String whereSql,Object[] params,Object[] newParams){
		try {
			MatchInfo info=ORDER_BY_REG.firstMatch(whereSql);
			String orderby=null;
			if(info!=null) {
				orderby=whereSql.substring(info.getEnd());
				whereSql=whereSql.substring(0,info.getStart());
			}
			Expression whereExpression = parseExpression(whereSql);
			WhereParserVisitor visitor=new WhereParserVisitor(clazz,whereExpression);
			whereExpression.accept(visitor);
			return visitor.buildBoundSql(sql, orderby,params, newParams);
		}catch(Exception e) {
			logger.error(e);
		}
		return super.build(clazz, sql, whereSql, params,newParams);
	}

	public void setAdapter(OrmMapperAdapter adapter) {
		this.adapter = adapter;
	}
	/**
	 * 对where语句进行解析成表达式
	 * #name :name 都支持对参数设置值
	 * @param exp
	 * @return
	 */
	public  Expression parseExpression(String exp){
		Expression<?> expression=cacheExpression.get(exp);
		if(expression==null){
			try {
				expression=OrmWhereSqlParser.parse(exp);
				cacheExpression.put(exp, expression);
			} catch (ParseException e) {
				throw new EvaluateException("解析异常-->"+exp, e);
			}
		}
		return expression;
	}
	/**
	 *	对order by 后的字段转换成列名
	 * @param clazz
	 * @param orderby
	 * @return
	 */
	protected String convertOrderby(Class clazz,String orderby) {
		String[] strs=StringUtils.splitc(orderby,',');
		for(int i=0;i<strs.length;i++) {
			String oneOrderby=StringUtils.trim(strs[i]);
			String name=BLANK_REG.split(oneOrderby)[0];
			String columnName=this.adapter.getColumnName(clazz.getName(), name);
			if(StringUtils.notEquals(name, columnName)) {
				orderby=orderby.replace(name, columnName);
			}
		}
		return orderby;
	}
	
	class WhereParserVisitor extends BaseExpressionVisitor{
		private Class clazz;
		Expression exp;
		/**是否存在使用名称的参数*/
		private boolean hasNamedParams=false;
		private WhereParserVisitor(Class clazz,Expression exp) {
			this.clazz=clazz;
			this.exp=exp;
		}

		@Override
		public void visit(NamedExpression expression) {
			if(expression instanceof FieldNameExpression) {
				String name=expression.getName();
				String column=adapter.getColumnName(clazz.getName(), name);
				if(column!=null) {
					expression.setName(column);
				}
			}else if(expression instanceof SpecialParameterExpression) {
				if(!"?".equals(expression.toString())) {
					hasNamedParams=true;
				}
			}
		}
		
		public BoundSql buildBoundSql(StringBuilder sql,String orderby,Object[] params,Object[] newParams) {
			String whereSql=exp.toString();
			Object[] whereSqlParams=newParams;
			if(hasNamedParams) {
				//用于参数分解的对象
				Object resolverValue=null;
				//动态的参数列表
				final List<Object> dynParamList=new ArrayList<Object>();
				if(params.length==1) {
					resolverValue=params[0];
				}else {
					resolverValue=CollectionUtils.arrayList(params);
				}
				whereSql=templateParser.parse(whereSql, new BaseMacroResolver(resolverValue) {
					@Override
					public String resolve(String macroName) {
						Object value = getValue(macroName);
						if (value == null) {
							dynParamList.add(null);
							return "?";
						}else if(ObjectLenUtils.hasLength(value)){
							int len=ObjectLenUtils.len(value);
							StringBuilder sb=new StringBuilder("(");
							for(int j=0;j<len;j++){
								sb.append(" ? ");
								if(j<len-1){
									sb.append(",");
								}
								Object indexVal=ObjectLenUtils.get(value,j);
								dynParamList.add(indexVal);
							}
							sb.append(")");
							return sb.toString();
						}else {
							dynParamList.add(value);
							return "?";
						}
					}
				});
				if(params.length!=newParams.length) {//存在其它参数时,拼接其它参数在前面
					int otherParamsLen=newParams.length-params.length;
					whereSqlParams=new Object[otherParamsLen+dynParamList.size()];
					System.arraycopy(newParams, 0, whereSqlParams, 0, otherParamsLen);
					for(int i=0;i<dynParamList.size();i++) {
						whereSqlParams[i+otherParamsLen]=dynParamList.get(i);
					}
				}else {
					whereSqlParams=dynParamList.toArray();
				}
			}
			sql.append(whereSql);
			if(orderby!=null) {
				sql.append(" ORDER BY ").append(convertOrderby(clazz,orderby));
			}
			return new ArrayBoundSql(sql.toString(), whereSqlParams);
		}
	}
	
	
}
