package org.smile.db.parser.expression;

import java.util.List;

import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.expression.operators.relational.NamedExpressionList;
import net.sf.jsqlparser.statement.select.SubSelect;

import org.smile.collection.CollectionUtils;

public class SqlItemsListVisitor extends BaseParserVisitor implements ItemsListVisitor{
	
	public SqlItemsListVisitor(ParserResult result){
		super(result);
	}
	
	@Override
	public void visit(SubSelect subSelect) {
		doVisitorSubSelect(subSelect);
	}

	@Override
	public void visit(ExpressionList expressionList) {
		doVisitorExpressions(expressionList.getExpressions());
	}

	@Override
	public void visit(MultiExpressionList arg0) {
		List<ExpressionList> list=arg0.getExprList();
		if(CollectionUtils.notEmpty(list)){
			for(ExpressionList e:list){
				visit(e);
			}
		}
	}

	@Override
	public void visit(NamedExpressionList namedExpressionList) {
		
	}

}
