package org.smile.db.parser.expression;

import org.smile.log.LoggerHandler;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;

public class SqlSelectItemVisitor extends BaseParserVisitor implements SelectItemVisitor,LoggerHandler{
	public SqlSelectItemVisitor(ParserResult result){
		super(result);
	}

	@Override
	public void visit(AllColumns allColumns) {
	}

	@Override
	public void visit(AllTableColumns allTableColumns) {
	}

	@Override
	public void visit(SelectExpressionItem selectExpressionItem) {
		result.visit(selectExpressionItem);
		Expression exp=selectExpressionItem.getExpression();
		doVisitorExpression(exp);
	}

}
