package org.smile.db.parser.expression;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;

public class ParserResult implements SelectItemVisitor{
	
	public static final String DEFAULT="default";
	/**所有的别名对应的表*/
	protected Map<String,Table> tables=new HashMap<String,Table>();
	/**所有表达式的集合*/
	protected List<Column> allColumns=new LinkedList<Column>();
	/**所有的函数*/
	protected List<Function> allFuctions=new LinkedList<Function>();
	
	protected Set<String> tableNames=new HashSet<String>();
	/**
	 * 字查询解析结果
	 */
	protected Map<String,SubParserResult> aliasSubResult; 
	
	protected Set<SubParserResult> noAliasSubResults=new HashSet<SubParserResult>();
	/**
	 * 没有写别名的表
	 */
	private Map<String,Table> noAliasTables=new HashMap<String,Table>();
	
	protected String sql;
	
	private Statement statement;
	/**所有用到的表名
	 * */
	public Map<String, Table> getTables() {
		return tables;
	}
	/**
	 * 用到的列名
	 * @param name 以名称查询
	 * @return
	 */
	public Table getTable(String name){
		return tables.get(name);
	}
	/**
	 * 所有的列
	 * @return
	 */
	public List<Column> getAllColumn() {
		return allColumns;
	}
	
	public void addSubResult(String name, SubParserResult result){
		if(aliasSubResult==null){
			aliasSubResult=new HashMap<String, SubParserResult>();
		}
		aliasSubResult.put(name, result);
		result.parent=this;
	}
	
	public void addColumn(Column exp){
		allColumns.add(exp);
	}
	
	public Map<String, SubParserResult> getAliasSubResults() {
		return aliasSubResult;
	}
	
	public void addFunction(Function f){
		this.allFuctions.add(f);
	}
	@Override
	public void visit(AllColumns allColumns) {
		
	}
	@Override
	public void visit(AllTableColumns allTableColumns) {
		
	}
	@Override
	public void visit(SelectExpressionItem selectExpressionItem) {
		
	}
	
	public boolean isSubResult(){
		return false;
	}
	
	public SubParserResult newSubResult(){
		return new SubParserResult();
	}
	
	public boolean hashSubResult(String name){
		if(aliasSubResult==null){
			return false;
		}
		return aliasSubResult.containsKey(name);
	}
	
	public SubParserResult getSubResult(String name){
		return aliasSubResult.get(name);
	}
	
	/**
	 * 添加一个表记录
	 * @param table
	 */
	public void addTable(Table table){
		String name;
		Alias alias=table.getAlias();
		if(alias!=null){
			name=alias.getName();
		}else{
			name=table.getName();
			noAliasTables.put(name, table);
		}
		tables.put(name, table);
		tableNames.add(table.getName());
	}
	/**
	 * 没有别名的表
	 * @return
	 */
	public Collection<Table> getNoAliasTables(){
		return noAliasTables.values();
	}
	
	public void addNoAliasSubResult(SubParserResult result){
		noAliasSubResults.add(result);
		result.parent=this;
	}

	public Set<SubParserResult> getNoAliasSubResults() {
		return noAliasSubResults;
	}
	
	public ParserResult getParent() {
		return null;
	}
	public List<Function> getAllFuctions() {
		return allFuctions;
	}
	public Statement getStatement() {
		return statement;
	}
	public void setStatement(Statement statement) {
		this.statement = statement;
	}
	
	public boolean isTableName(String name){
		return tableNames.contains(name);
	}
}
