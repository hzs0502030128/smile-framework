package org.smile.db.parser;

import java.util.List;

import org.smile.db.parser.expression.BaseParserVisitor;
import org.smile.db.parser.expression.ParserResult;
import org.smile.db.parser.expression.SqlItemsListVisitor;
import org.smile.db.parser.expression.SqlSelectVisitor;
import org.smile.db.parser.expression.SubParserResult;

import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;

public class SqlStatmentVistitor extends BaseParserVisitor{
	
	public SqlStatmentVistitor(ParserResult result){
		super(result);
	}
	
	@Override
	public void visit(Select select) {
		//处理with
		doVistorWithItem(select.getWithItemsList());
		//处理查询表体
		SelectBody body=select.getSelectBody();
		body.accept(new SqlSelectVisitor(result));
	}

	@Override
	public void visit(Delete delete) {
		Table table=delete.getTable();
		result.addTable(table);
		doVisitorExpression(delete.getWhere());
	}

	@Override
	public void visit(Update update) {
		List<Table> tables=update.getTables();
		for(Table table:tables) {
			result.addTable(table);
		}
		doVisitorFromItem(update.getFromItem());
		//处理where
		doVisitorExpression(update.getWhere());
		//更新的列
		doVisitorColumns(update.getColumns());
		//设置表达式
		doVisitorExpressions(update.getExpressions());
		
		doVisitorJoin(update.getJoins());
	}

	@Override
	public void visit(Insert insert) {
		Select select=insert.getSelect();
		if(select!=null){
			SubParserResult subResult=result.newSubResult();
			result.addNoAliasSubResult(subResult);
			select.accept(new SqlStatmentVistitor(subResult));
		}
		
		doVisitorTable(insert.getTable());
		
		doVisitorColumns(insert.getColumns());
		
		ItemsList list=insert.getItemsList();
		if(list!=null){
			list.accept(new SqlItemsListVisitor(result));
		}
	}

	@Override
	public void visit(Drop drop) {
		Table table=new Table(drop.getName().getName());
		result.addTable(table);
	}

	@Override
	public void visit(Truncate truncate) {
		Table table=truncate.getTable();
		result.addTable(table);
	}



	public ParserResult getResult() {
		return result;
	}

	
	
	
}
