package org.smile.db.parser.expression;

import java.util.List;

import org.smile.collection.CollectionUtils;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.FromItemVisitor;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.WithItem;

public class BaseParserVisitor extends StatementVisitorAdapter{

	protected ParserResult result;

	protected SqlExpressionVisitor expressVisitor;

	public BaseParserVisitor(ParserResult result) {
		this.result = result;
		this.expressVisitor = new SqlExpressionVisitor(this,result);
	}

	/**
	 * 处理列
	 * @param columns
	 * @param result
	 */
	public void doVisitorColumns(List<Column> columns) {
		if (CollectionUtils.notEmpty(columns)) {
			for (Column c : columns) {
				c.accept(expressVisitor);
			}
		}
	}

	/**
	 * 处理表达式
	 * @param exps
	 * @param result
	 */
	public void doVisitorExpressions(List<Expression> exps) {
		if (CollectionUtils.notEmpty(exps)) {
			for (Expression c : exps) {
				c.accept(expressVisitor);
			}
		}
	}

	/**
	 * 处理查询
	 * @param fromItem
	 * @param result
	 */
	public void doVisitorFromItem(FromItem fromItem) {
		if (fromItem != null) {
			fromItem.accept(new SqlFromItemVisitor(result));
		}
	}

	public void doVisitorTables(List<Table> tables) {
		for (Table t : tables) {
			result.addTable(t);
		}
	}

	/**
	 * 处理表达式
	 * @param exp
	 */
	public void doVisitorExpression(Expression exp) {
		if (exp != null) {
			exp.accept(expressVisitor);
		}
	}

	public void doVistorWithItem(List<WithItem> withItems) {
		// 处理with
		if (CollectionUtils.notEmpty(withItems)) {
			for (WithItem item : withItems) {
				// with的子结果
				SubParserResult withResult = result.newSubResult();
				result.addSubResult(item.getName(), withResult);
				item.accept(new SqlSelectVisitor(withResult));
			}
		}
	}

	public void doVisitorJoin(List<Join> joins) {
		if (CollectionUtils.notEmpty(joins)) {
			FromItemVisitor fromItemVisitor = new SqlFromItemVisitor(result);
			for (Join j : joins) {
				FromItem joinItem = j.getRightItem();
				joinItem.accept(fromItemVisitor);
				doVisitorExpression(j.getOnExpression());
			}
		}
	}

	public void doVisitorJoin(Join join) {
		if (join != null) {
			FromItem joinItem = join.getRightItem();
			joinItem.accept(new SqlFromItemVisitor(result));
			doVisitorExpression(join.getOnExpression());
		}
	}

	public void doVisitorTable(Table table) {
		if (table != null) {
			result.addTable(table);
		}
	}

	public void doVisitorOrderBy(List<OrderByElement> orderbys) {
		// 处理orderby
		if (CollectionUtils.notEmpty(orderbys)) {
			for (OrderByElement ob : orderbys) {
				doVisitorExpression(ob.getExpression());
			}
		}
	}

	public void doVisitorSubSelect(SubSelect subSelect) {
		SelectBody selectBody = subSelect.getSelectBody();
		SubParserResult subResult = result.newSubResult();
		Alias alias = subSelect.getAlias();
		if (alias == null) {
			result.addNoAliasSubResult(subResult);
		} else {
			result.addSubResult(alias.getName(), subResult);
		}
		selectBody.accept(new SqlSelectVisitor(subResult));
	}
	
}
