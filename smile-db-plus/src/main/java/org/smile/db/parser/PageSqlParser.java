package org.smile.db.parser;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.ValuesList;
import net.sf.jsqlparser.statement.select.WithItem;

import org.smile.collection.CollectionUtils;
import org.smile.db.Dialect;
import org.smile.db.sql.page.DialectPage;
import org.smile.log.LoggerHandler;

/**
 * sql解析类，
 * 提供更智能的count查询sql
 *
 * @author 胡真山
 */
public class PageSqlParser implements DialectPage,LoggerHandler{
	//列字段
    private static final List<SelectItem> COUNT_ITEM;
    
    private static final Alias TABLE_ALIAS;
    
    private static final String FOR_UPDATE="FOR UPDATE";
    
    protected DialectPage dialectPage;
    /**解析出来的select对象*/
    protected Select select;

    static {
        COUNT_ITEM = new ArrayList<SelectItem>();
        //添加一个新列count
        COUNT_ITEM.add(new SelectExpressionItem(new Column("COUNT(0)")));
        TABLE_ALIAS = new Alias("COUNT_TABLE_T");
        TABLE_ALIAS.setUseAs(false);
    }
    //缓存已经修改过的sql
    private String newSql;
    
    public PageSqlParser(Dialect dialect,String sql) throws SQLException{
    	this(Dialect.newDialectPage(dialect, sql));
    }
    
    public PageSqlParser(DialectPage dialectPage){
    	this.dialectPage=dialectPage;
    	//解析SQL
        try {
			this.select=(Select)CCJSqlParserUtil.parse(dialectPage.getSql());
		} catch (JSQLParserException e) {}
    }

	/**
     * 是否是支持的count sql
     * @param sql
     */
    public boolean isSupportedSql() {
        if (dialectPage.getSql().trim().toUpperCase().endsWith(FOR_UPDATE)) {
        	return false;
        }
        return true;
    }

    /**
     * 获取智能的 条数查询SQL语句
     * countSql 
     * @param sql 原Sql语句
     * @return  条数查询语句
     */
    @Override
    public String getCountSql() {
        if (newSql!=null) {
            return newSql;
        }else if(select!=null){
            SelectBody selectBody = select.getSelectBody();
            //处理body-去order by
            processSelectBody(selectBody);
            //处理with-去order by
            processWithItemsList(select.getWithItemsList());
            //处理为count查询
            sqlToCount(select);
            newSql = select.toString();
        }else{
	        //无法解析的用一般方法返回count语句
	        newSql=dialectPage.getCountSql();
        }
        return newSql;
    }

    /**
     * 将sql转换为count查询
     *
     * @param select
     */
    protected void sqlToCount(Select select) {
        SelectBody selectBody = select.getSelectBody();
        // 是否能简化count查询
        if (selectBody instanceof PlainSelect && isSimpleCount((PlainSelect) selectBody)) {
            ((PlainSelect) selectBody).setSelectItems(COUNT_ITEM);
        } else {
            PlainSelect plainSelect = new PlainSelect();
            SubSelect subSelect = new SubSelect();
            subSelect.setSelectBody(selectBody);
            subSelect.setAlias(TABLE_ALIAS);
            plainSelect.setFromItem(subSelect);
            plainSelect.setSelectItems(COUNT_ITEM);
            select.setSelectBody(plainSelect);
        }
    }

    /**
     * 是否可以用简单的count查询方式
     * @param select
     * @return
     */
    public boolean isSimpleCount(PlainSelect select) {
        //包含group by的时候不可以
        if (select.getGroupBy() != null) {
            return false;
        }
        //包含distinct的时候不可以
        if (select.getDistinct() != null) {
            return false;
        }
        for (SelectItem item : select.getSelectItems()) {
            //select列中包含参数的时候不可以，否则会引起参数个数错误
            if (item.toString().contains("?")) {
                return false;
            }
            //如果查询列中包含函数，也不可以，函数可能会聚合列
            if (item instanceof SelectExpressionItem) {
                if(((SelectExpressionItem)item).getExpression() instanceof Function){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 处理selectBody
     * 去除
     * Order by
     * @param selectBody
     */
    public void processSelectBody(SelectBody selectBody) {
        if (selectBody instanceof PlainSelect) {
            processPlainSelect((PlainSelect) selectBody);
        } else if (selectBody instanceof WithItem) {
            WithItem withItem = (WithItem) selectBody;
            if (withItem.getSelectBody() != null) {
                processSelectBody(withItem.getSelectBody());
            }
        } else {
            SetOperationList operationList = (SetOperationList) selectBody;
            if (CollectionUtils.notEmpty(operationList.getSelects())) {
                List<SelectBody> plainSelects = operationList.getSelects();
                for (SelectBody plainSelect : plainSelects) {
                    processPlainSelect((PlainSelect)plainSelect);
                }
            }
            if (!orderByHashParameters(operationList.getOrderByElements())) {
                operationList.setOrderByElements(null);
            }
        }
    }

    /**
     * 处理PlainSelect类型的selectBody
     *
     * @param plainSelect
     */
    public void processPlainSelect(PlainSelect plainSelect) {
        if (!orderByHashParameters(plainSelect.getOrderByElements())) {
            plainSelect.setOrderByElements(null);
        }
        if (plainSelect.getFromItem() != null) {
            processFromItem(plainSelect.getFromItem());
        }
        if (CollectionUtils.notEmpty(plainSelect.getJoins())) {
            List<Join> joins = plainSelect.getJoins();
            for (Join join : joins) {
                if (join.getRightItem() != null) {
                    processFromItem(join.getRightItem());
                }
            }
        }
    }

    /**
     * 处理WithItem
     *
     * @param withItemsList
     */
    public void processWithItemsList(List<WithItem> withItemsList) {
        if (withItemsList != null && withItemsList.size() > 0) {
            for (WithItem item : withItemsList) {
                processSelectBody(item.getSelectBody());
            }
        }
    }

    /**
     * 处理子查询
     * @param fromItem
     */
    public void processFromItem(FromItem fromItem) {
        if (fromItem instanceof SubJoin) {
            SubJoin subJoin = (SubJoin) fromItem;
            if (subJoin.getJoinList()!= null) {
            	for(Join j:subJoin.getJoinList()) {
	                if (j.getRightItem() != null) {
	                    processFromItem(j.getRightItem());
	                }
            	}
            }
            if (subJoin.getLeft() != null) {
                processFromItem(subJoin.getLeft());
            }
        } else if (fromItem instanceof SubSelect) {
            SubSelect subSelect = (SubSelect) fromItem;
            if (subSelect.getSelectBody() != null) {
                processSelectBody(subSelect.getSelectBody());
            }
        } else if (fromItem instanceof ValuesList) {

        } else if (fromItem instanceof LateralSubSelect) {
            LateralSubSelect lateralSubSelect = (LateralSubSelect) fromItem;
            if (lateralSubSelect.getSubSelect() != null) {
                SubSelect subSelect = lateralSubSelect.getSubSelect();
                if (subSelect.getSelectBody() != null) {
                    processSelectBody(subSelect.getSelectBody());
                }
            }
        }
    }

    /**
     * 判断Orderby是否包含参数?，有参数的不能去掉orderby
     * @param orderByElements
     * @return
     */
    public boolean orderByHashParameters(List<OrderByElement> orderByElements) {
        if (orderByElements == null) {
            return false;
        }
        for (OrderByElement orderByElement : orderByElements) {
            if (orderByElement.toString().contains("?")) {
                return true;
            }
        }
        return false;
    }

	@Override
	public String getDataSql(int page, int size) {
		return this.dialectPage.getDataSql(page, size);
	}

	@Override
	public String getTopSql(int top) {
		return this.dialectPage.getTopSql(top);
	}

	@Override
	public void setTotal(long total) {
		this.dialectPage.setTotal(total);
	}

	@Override
	public String getSql() {
		return this.dialectPage.getSql();
	}

	@Override
	public String getLimitSql(long offset, int limit) {
		return this.dialectPage.getLimitSql(offset, limit);
	}
}
