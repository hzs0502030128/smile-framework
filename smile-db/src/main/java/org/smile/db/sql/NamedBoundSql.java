package org.smile.db.sql;

import java.util.List;

import org.smile.commons.Strings;
import org.smile.util.RegExp;
import org.smile.util.RegExp.MatchInfo;

/**
 * 参数解析 :name  参数方式
 * @author 胡真山
 *
 */
public class NamedBoundSql extends AbstractMappingBoundSql {

	protected static RegExp paramExp = new RegExp("(:[a-zA-Z_]+:?[a-zA-Z0-9_\\$\\.\\u4e00-\\u9fa5]*$)|(:[a-zA-Z_]+:?[a-zA-Z0-9_\\$\\.\\u4e00-\\u9fa5]*[ ,\\)\r\n\t])");

	protected static RegExp endReg = new RegExp("[, $\\)\r\n\t]+");
	
	public NamedBoundSql(String sql, Object params){
		super(sql, params);
	}

	@Override
	protected String parseParamName(String exp) {
		return endReg.replaceAll(exp, Strings.BLANK).substring(1).trim();
	}

	@Override
	protected SqlParseInfo parseSqlInfo(String sql) {
		SqlParseInfo sqlParseInfo=SqlParseInfoCache.get(sql);
		if(sqlParseInfo==null) {
			List<MatchInfo> list=paramExp.findAll(sql);
			sqlParseInfo=new SqlParseInfo(sql,list);
			sqlParseInfo.init(this);
			SqlParseInfoCache.put(sql, sqlParseInfo);
		}
		return sqlParseInfo;
	}


	@Override
	protected int getParamEndIndex(MatchInfo info,SqlParseInfo sqlParseInfo){
		String exp=info.getContext();
		return info.getEnd()-(exp.length()-sqlParseInfo.getParamName(exp).length()-1);
	}

}
