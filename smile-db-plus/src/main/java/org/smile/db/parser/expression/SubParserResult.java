package org.smile.db.parser.expression;

import java.util.HashMap;
import java.util.Map;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;


public class SubParserResult extends ParserResult{
	
	ParserResult parent;
	
	private Map<String,Column> noAliasColumns;
	
	@Override
	public boolean isSubResult() {
		return true;
	}
	
	public void addNoAliasColumn(Column column){
		if(noAliasColumns==null){
			noAliasColumns=new HashMap<String,Column>();
		}
		noAliasColumns.put(column.getColumnName(), column);
	}
	
	@Override
	public void visit(SelectExpressionItem selectExpressionItem) {
		Expression exp=selectExpressionItem.getExpression();
		if(exp instanceof Column){
			if(selectExpressionItem.getAlias()==null){
				addNoAliasColumn((Column)exp);
			}
		}
	}
	
	public Column getNoAliasColumn(String name){
		if(noAliasColumns==null){
			return null;
		}
		return noAliasColumns.get(name);
	}
	
	public ParserResult getParent() {
		return parent;
	}
}
