package org.smile.db.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.smile.collection.FixedLenHashMap;
import org.smile.collection.LockedMap;
import org.smile.db.sql.parameter.MappingParameterHandler;
import org.smile.db.sql.parameter.Parameter;
import org.smile.db.sql.parameter.ParameterBuilder;
import org.smile.db.sql.parameter.ParameterHandler;
import org.smile.db.sql.parameter.ParameterMapping;
import org.smile.util.ObjectLenUtils;
import org.smile.util.RegExp.MatchInfo;

public abstract class AbstractMappingBoundSql extends BoundSql {
	/**
	 * 对语句参数表达式进行解析后对象缓存
	 **/
	protected static Map<String,SqlParseInfo> SqlParseInfoCache=new LockedMap<String,SqlParseInfo>(new FixedLenHashMap<String,SqlParseInfo>(5000));
	/**
	 * 用来对参数填充对象获取属性操作
	 */
	protected ParameterHandler handler;
	
	/**当前执行与语绑定的用与参数设置的对象*/
	protected Object params;
	/**
	 * 解析之后的参数的映射对象
	 */
	protected ParameterMapping mapping;
	
	/**
	 * 保存替换内容  %{name} --> ? 单个问号占位
	 * 或 %{name} --> ?,?,? 多个问号占位
	 */
	protected Map<String,String> replaceExp=new HashMap<String, String>();

	@Override
	public Object getParams() {
		return params;
	}
	
	protected void init(String sql){
		SqlParseInfo sqlParseInfo=parseSqlInfo(sql);
		sqlParseInfo.parseSql(this);
	}
	/**
	 * 	从表达式中解析参数名称
	 * %{name} -- > name
	 * @param exp
	 * @return
	 */
	protected abstract String parseParamName(String exp);
	/**
	 * 初始化占位符名称配置信息
	 */
	protected abstract SqlParseInfo  parseSqlInfo(String sql);
	
	/**
	 * 参数占位结果索引
	 * @return
	 */
	protected abstract int getParamEndIndex(MatchInfo info,SqlParseInfo sqlParseInfo);

	/**
	 * 构造方法
	 * @param sql 要执行的sql语句
	 * @param params 语句赋值参数
	 */
	protected AbstractMappingBoundSql(String sql,Object params){
		this.params=params;
		if(this.params!=null){
			this.mapping=new ParameterMapping(params.getClass());
			this.handler=new MappingParameterHandler(params, mapping);
		}
		init(sql);
	}
	/**
	 * 继承父类  批量操作时传 list 之外的数据在这个map中
	 */
	@Override
	public void setBatchMap(Map batchMap){
		handler.setBatchMap(batchMap);
	}
	
	
	@Override
	public void fillBatchStatement(PreparedStatement ps, Object param) throws SQLException {
		handler.fillBatchObject(ps, param);
	}

	@Override
	public void fillStatement(PreparedStatement ps) throws SQLException {
		if(params!=null){
			handler.fillObject(ps, params);
		}
	}

	@Override
	public Iterator<Parameter> iteratorMapping() {
		return mapping.iterator();
	}
	
	/**
	 * 获取占位被替换为的占  ?  表达式
	 * @param exp
	 * @return
	 */
	protected String getReplaceStr(String exp){
		return replaceExp.get(exp);
	}
	
	/**
	 * 设置替换表达式
	 * @param exp
	 * @param parseStr
	 */
	protected  void setReplaceStr(String exp,String parseStr){
		replaceExp.put(exp, parseStr);
	}
	
	
	
	static class SqlParseInfo{
		/**表达式匹配*/
		protected List<MatchInfo> mateinfos;
		/***
		 * 解析出来的参数占位的内容 如：%{name}
		 */
		protected String[] paramRegString;
		/**
		 * 表达式与参数名称对应  %{name} --> name
		 */
		protected Map<String,String> expParams;
		
		protected String sourceSql;
		
		protected SqlParseInfo(String sourceSql,List<MatchInfo> mateinfos) {
			this.mateinfos=mateinfos;
			this.sourceSql=sourceSql;
		}
		
		protected void init(AbstractMappingBoundSql boundSql){
			paramRegString=new String[mateinfos.size()];
			expParams=new HashMap<String, String>();
			int index=0;
			for(MatchInfo i:mateinfos){
				String exp=i.getContext();
				paramRegString[index]=exp;
				expParams.put(exp, boundSql.parseParamName(exp));
				index++;
			}
		}
		
		/***
		 * 对sql语句进行解析  
		 * 解析出其中包含的参数 ，解析后的语句包含占位符
		 * 解析出参数与值对应的mapping
		 * @throws SQLException
		 */
		protected void parseSql(AbstractMappingBoundSql boundSql){
			String[] paramsString=findParamRegString();
			if(paramsString!=null&&boundSql.params!=null){
				Object paramValue;
				String name;
				for(String p:paramsString){
					ParameterBuilder pb=new ParameterBuilder(getParamName(p));
					name=pb.getParameterName();
					paramValue=boundSql.handler.getValue(boundSql.params, name);
					if(paramValue!=null&&ObjectLenUtils.hasLength(paramValue)){
						int len=ObjectLenUtils.len(paramValue);
						StringBuilder sb=new StringBuilder();
						for(int j=0;j<len;j++){
							sb.append(" ? ");
							if(j<len-1){
								sb.append(",");
							}
							Object indexVal=ObjectLenUtils.get(paramValue,j);
							boundSql.mapping.addParameter(name, indexVal);
						}
						boundSql.setReplaceStr(p,sb.toString());
					}else{
						boundSql.setReplaceStr(p,"?");
						if(pb.isFunction()){
							//函数转换
							paramValue=pb.getFunction().getFunctionValue(paramValue);
							Parameter parameter=new Parameter(name, paramValue);
							parameter.setFunction(pb.getFunction());
							boundSql.mapping.addParameter(parameter);
						}else{
							boundSql.mapping.addParameter(name, paramValue);
						}
					}
				}
			}
			replaceExpParam(boundSql);
		}
		
		/**
		 * 替换占位名称
		 */
		protected void replaceExpParam(AbstractMappingBoundSql boundSql){
			if(mateinfos.size()>0){//有占位参数时
				StringBuilder sqlBuilder=new StringBuilder(sourceSql.length());
				int index=0;
				int infoIndex=0;
				for(MatchInfo info:mateinfos){
					sqlBuilder.append(sourceSql.substring(index, info.getStart()));
					String replace=boundSql.getReplaceStr(info.getContext());
					sqlBuilder.append(replace);
					index=boundSql.getParamEndIndex(info,this);
					infoIndex++;
					if(infoIndex==mateinfos.size()){
						//最后一个处理
						sqlBuilder.append(sourceSql.substring(index));
					}
				}
				boundSql.sql=sqlBuilder.toString();
			}else {//无占位参数时
				boundSql.sql=sourceSql;
			}
		}
		
		/***
		 * 查找出语句中的所有的参数表达式
		 * @return 所有参数表达式的数组 
		 */
		protected  String[] findParamRegString(){
			return paramRegString;
		}
		
		
		
		/**
		 * 参数名称 如：${  name }   返回结果 name
		 * @param exp 表达式
		 * @return
		 */
		protected  String getParamName(String exp){
			return expParams.get(exp);
		}
		
	}
}
