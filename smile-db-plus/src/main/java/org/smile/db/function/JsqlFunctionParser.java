package org.smile.db.function;

import java.util.List;

import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;

import org.smile.db.Dialect;
import org.smile.db.parser.SqlStatmentVistitor;
import org.smile.db.parser.SqlParser;
import org.smile.db.parser.SqlParserException;
import org.smile.db.parser.expression.ParserResult;
import org.smile.log.LoggerHandler;
/**
 * 对使用自定义的函数的sql语句进行解析转换
 * @author 胡真山
 *
 */
public class JsqlFunctionParser implements SqlParser,LoggerHandler {
	/**
	 * 处理所有的函数
	 * @param result
	 * @param dialect
	 */
	protected void doAllFunctions(ParserResult result,Dialect dialect){
		List<Function> functions=result.getAllFuctions();
		for(Function f:functions){
			dialect.convert(new JsqlParserFunction(f));
		}
	}
	
	/**
	 * 解析sql语句对函数进行转换
	 * @param sql
	 * @param dialect
	 * @return
	 */
	@Override
	public ParserResult parse(String sql,Dialect dialect){
		try {
			Statement st = CCJSqlParserUtil.parse(sql);
			if(logger.isDebugEnabled()){
				logger.debug("osql:"+st.toString());
			}
			ParserResult result=new ParserResult();
			SqlStatmentVistitor parser = new SqlStatmentVistitor(result);
			st.accept(parser);
			doAllFunctions(result, dialect);
			result.setStatement(st);
			return result;
		} catch (Exception e) {
			throw new SqlParserException("函数解析出错",e);
		}
	}
}
