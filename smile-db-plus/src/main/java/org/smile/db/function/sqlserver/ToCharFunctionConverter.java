package org.smile.db.function.sqlserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.schema.Column;

import org.smile.db.function.JsqlParserFunctionConverter;

public class ToCharFunctionConverter  extends JsqlParserFunctionConverter{

	private static Map<String,LongValue> format=new HashMap<String,LongValue>();
	
	static{
		format.put("yyyy-mm-dd hh:mi:ss", new LongValue(120));
		format.put("yyyy-mm-dd hh:mi:ss.SSS", new LongValue(121));
		format.put("yyyy-mm-dd", new LongValue(110));
		format.put("yyyy.mm.dd", new LongValue(102));
		format.put("yyyy/mm/dd", new LongValue(111));
		format.put("yymmdd", new LongValue(112));
		format.put("mm/dd/yyyy", new LongValue(101));
	}
	
	private Expression getFormat(StringValue f){
		LongValue v=format.get(f.getValue());
		if(v!=null){
			return v;
		}
		return f;
	}
	@Override
	protected void doConvert(Function f) {
		f.setName("convert");
		//convert(varchar, birthday, 120) 
		//增加一个varchar列
		Column first=new Column();
		first.setColumnName("varchar");
		List<Expression> fparam=f.getParameters().getExpressions();
		fparam.add(0, first);
		//处理格式代码
		StringValue formattxt=(StringValue)fparam.get(2);
		Expression v=getFormat(formattxt);
		fparam.set(2, v);
	}


}
