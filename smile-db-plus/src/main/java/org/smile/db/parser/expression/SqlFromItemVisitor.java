package org.smile.db.parser.expression;

import java.util.List;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItemVisitor;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.ParenthesisFromItem;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.TableFunction;
import net.sf.jsqlparser.statement.select.ValuesList;

import org.smile.collection.CollectionUtils;
import org.smile.log.LoggerHandler;

public class SqlFromItemVisitor extends BaseParserVisitor implements FromItemVisitor,LoggerHandler {

	public SqlFromItemVisitor(ParserResult result){
		super(result);
	}
	@Override
	public void visit(Table tableName) {
		result.addTable(tableName);
	}

	@Override
	public void visit(SubSelect subSelect) {
		SelectBody body=subSelect.getSelectBody();
		SubParserResult subResult=result.newSubResult();
		Alias alias=subSelect.getAlias();
		if(alias!=null){
			result.addSubResult(alias.getName(), subResult);
		}
		body.accept(new SqlSelectVisitor(subResult));
	}

	@Override
	public void visit(SubJoin subjoin) {
		doVisitorFromItem(subjoin.getLeft());
		doVisitorJoin(subjoin.getJoinList());
	}

	@Override
	public void visit(LateralSubSelect arg0) {
		doVisitorSubSelect(arg0.getSubSelect());
	}

	@Override
	public void visit(ValuesList valuesList) {
		MultiExpressionList mlist=valuesList.getMultiExpressionList();
		if(mlist!=null){
			List<ExpressionList> list=mlist.getExprList();
			if(CollectionUtils.notEmpty(list)){
				for(ExpressionList e:list){
					doVisitorExpressions(e.getExpressions());
				}
			}
		}
	}
	@Override
	public void visit(TableFunction tableFunction) {
		
	}
	@Override
	public void visit(ParenthesisFromItem aThis) {
		
	}

}
