package org.smile.db.parser.expression;

import java.util.List;

import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.FromItemVisitor;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.statement.values.ValuesStatement;

import org.smile.collection.CollectionUtils;
import org.smile.log.LoggerHandler;

public class SqlSelectVisitor extends BaseParserVisitor implements SelectVisitor,LoggerHandler{
	
	protected SelectItemVisitor itemVisitor;
	
	protected FromItemVisitor fromItemVisitor;
	
	public SqlSelectVisitor(ParserResult result){
		super(result);
		itemVisitor=new SqlSelectItemVisitor(result);
		fromItemVisitor=new SqlFromItemVisitor(result);
	}
	
	@Override
	public void visit(PlainSelect plainSelect) {
		//处理表
		FromItem from=plainSelect.getFromItem();
		from.accept(fromItemVisitor);
		//处理连接
		doVisitorJoin(plainSelect.getJoins());
		
		//处理字段
		List<SelectItem> selectItems=plainSelect.getSelectItems();
		if(CollectionUtils.notEmpty(selectItems)){
			for(SelectItem item:selectItems){
				item.accept(itemVisitor);
			}
		}
		//处理where 
		doVisitorExpression(plainSelect.getWhere());
		if(plainSelect.getGroupBy()!=null) {
			//处理group
			doVisitorExpressions(plainSelect.getGroupBy().getGroupByExpressions());
		}
		//处理orderby
		doVisitorOrderBy(plainSelect.getOrderByElements());
		
	}

	@Override
	public void visit(SetOperationList selectList) {
		List<SelectBody> selects=selectList.getSelects();
		if(CollectionUtils.notEmpty(selects)){
			for(SelectBody s:selects){
				s.accept(this);
			}
		}
		//处理orderby
		doVisitorOrderBy(selectList.getOrderByElements());
		
	}

	@Override
	public void visit(WithItem with) {
		SelectBody body=with.getSelectBody();
		body.accept(this);
	}

	@Override
	public void visit(ValuesStatement aThis) {
		
	}

}
