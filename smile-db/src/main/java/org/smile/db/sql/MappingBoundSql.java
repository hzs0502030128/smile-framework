package org.smile.db.sql;

import java.sql.SQLException;
import java.util.List;

import org.smile.util.RegExp;
import org.smile.util.RegExp.MatchInfo;
/**
 * 参数解析
 * %{name} 参数形式
 * @author 胡真山
 *
 */
public class MappingBoundSql extends AbstractMappingBoundSql{
	/**占位符查找的正则表达式*/
	protected static RegExp paramExp=new RegExp("%\\{ *[_A-Za-z0-9:\\$\\.\u4e00-\u9fa5]+ *\\}");
	
	public MappingBoundSql(String sql, Object params) throws SQLException {
		super(sql, params);
	}

	@Override
	protected String parseParamName(String exp) {
		return  exp.substring(2, exp.length() - 1).trim();
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
	protected int getParamEndIndex(MatchInfo info,SqlParseInfo sqlParseInfo ) {
		return info.getEnd();
	}
	
}
