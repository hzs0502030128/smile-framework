package org.smile.db.parser.expression;

import java.util.List;

import org.smile.collection.CollectionUtils;
import org.smile.log.LoggerHandler;

import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnalyticExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.CastExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.ExtractExpression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.JsonExpression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.SignedExpression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Modulo;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperator;
import net.sf.jsqlparser.expression.operators.relational.RegExpMySQLOperator;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SubSelect;

public class SqlExpressionVisitor extends ExpressionVisitorAdapter implements ExpressionVisitor,LoggerHandler{
	
	ParserResult result;
	
	BaseParserVisitor base;
	
	public SqlExpressionVisitor(BaseParserVisitor base, ParserResult result){
		this.result=result;
		this.base=base;
	}
	
	public SqlExpressionVisitor(ParserResult result){
		this.result=result;
		this.base=new BaseParserVisitor(result);
	}
	
	
	@Override
	public void visit(NullValue nullValue) {
		
	}

	@Override
	public void visit(Function function){
		result.addFunction(function);
		ExpressionList list=function.getParameters();
		if(list!=null){
			List<Expression> explist=list.getExpressions();
			if(CollectionUtils.notEmpty(explist)){
				for(Expression e:explist){
					e.accept(this);
				}
			}
		}
	}

	@Override
	public void visit(SignedExpression arg0) {
		Expression exp=arg0.getExpression();
		exp.accept(this);
	}


	@Override
	public void visit(Parenthesis parenthesis) {
		Expression exp=parenthesis.getExpression();
		exp.accept(this);
	}

	@Override
	public void visit(StringValue stringValue) {
	}

	@Override
	public void visit(Addition addition) {
		visitBinaryExp(addition);
	}

	@Override
	public void visit(Division division) {
		visitBinaryExp(division);
	}

	@Override
	public void visit(Multiplication multiplication) {
		visitBinaryExp(multiplication);
	}

	@Override
	public void visit(Subtraction subtraction) {
		visitBinaryExp(subtraction);
	}

	@Override
	public void visit(AndExpression andExpression) {
		visitBinaryExp(andExpression);
	}
	
	
	public void visitBinaryExp(BinaryExpression expression){
		Expression left=expression.getLeftExpression();
		left.accept(this);
		Expression right=expression.getRightExpression();
		right.accept(this);
	}

	@Override
	public void visit(OrExpression orExpression) {
		visitBinaryExp(orExpression);
	}

	@Override
	public void visit(Between between) {
		Expression left=between.getLeftExpression();
		left.accept(this);
		Expression right=between.getBetweenExpressionStart();
		right.accept(this);
		Expression end=between.getBetweenExpressionEnd();
		end.accept(this);
	}

	@Override
	public void visit(EqualsTo equalsTo) {
		visitBinaryExp(equalsTo);
	}

	@Override
	public void visit(GreaterThan greaterThan) {
		visitBinaryExp(greaterThan);
	}

	@Override
	public void visit(GreaterThanEquals greaterThanEquals) {
		visitBinaryExp(greaterThanEquals);
	}

	@Override
	public void visit(InExpression inExpression) {
		Expression exp=inExpression.getLeftExpression();
		exp.accept(this);
		

	}

	@Override
	public void visit(IsNullExpression isNullExpression) {
		isNullExpression.getLeftExpression().accept(this);
	}

	@Override
	public void visit(LikeExpression likeExpression) {
		visitBinaryExp(likeExpression);
	}

	@Override
	public void visit(MinorThan minorThan) {
		visitBinaryExp(minorThan);
	}

	@Override
	public void visit(MinorThanEquals minorThanEquals) {
		visitBinaryExp(minorThanEquals);

	}

	@Override
	public void visit(NotEqualsTo notEqualsTo) {
		visitBinaryExp(notEqualsTo);
	}

	@Override
	public void visit(Column tableColumn) {
		result.addColumn(tableColumn);
	}

	@Override
	public void visit(SubSelect subSelect) {
		base.doVisitorSubSelect(subSelect);
	}

	@Override
	public void visit(CaseExpression caseExpression) {
		List<WhenClause> list=caseExpression.getWhenClauses();
		if(CollectionUtils.notEmpty(list)){
			for(Expression e:list){
				e.accept(this);
			}
		}
		Expression elsee=caseExpression.getElseExpression();
		if(elsee!=null){
			elsee.accept(this);
		}
	}

	@Override
	public void visit(WhenClause whenClause) {
		Expression when=whenClause.getWhenExpression();
		Expression then=whenClause.getThenExpression();
		when.accept(this);
		then.accept(this);
	}

	@Override
	public void visit(ExistsExpression existsExpression) {
		Expression exp=existsExpression.getRightExpression();
		exp.accept(this);
	}

	@Override
	public void visit(AllComparisonExpression allComparisonExpression) {
		SubSelect ss=allComparisonExpression.getSubSelect();
		SelectBody body=ss.getSelectBody();
		body.accept(new SqlSelectVisitor(result));
	}

	@Override
	public void visit(AnyComparisonExpression anyComparisonExpression) {
		SubSelect ss=anyComparisonExpression.getSubSelect();
		SelectBody body=ss.getSelectBody();
		body.accept(new SqlSelectVisitor(result));
	}

	@Override
	public void visit(Concat concat) {
		visitBinaryExp(concat);

	}

	@Override
	public void visit(Matches matches) {
		visitBinaryExp(matches);
	}

	@Override
	public void visit(BitwiseAnd bitwiseAnd) {
		visitBinaryExp(bitwiseAnd);
	}

	@Override
	public void visit(BitwiseOr bitwiseOr) {
		visitBinaryExp(bitwiseOr);

	}

	@Override
	public void visit(BitwiseXor bitwiseXor) {
		visitBinaryExp(bitwiseXor);

	}

	@Override
	public void visit(CastExpression arg0) {
		Expression exp=arg0.getLeftExpression();
		exp.accept(this);
	}

	@Override
	public void visit(Modulo arg0) {
		visitBinaryExp(arg0);
	}

	@Override
	public void visit(AnalyticExpression exp) {
		ExpressionList el=exp.getPartitionExpressionList();
		if(el!=null){
			base.doVisitorExpressions(el.getExpressions());
		}
		base.doVisitorOrderBy(exp.getOrderByElements());
	}

	@Override
	public void visit(ExtractExpression arg0) {
		Expression exp=arg0.getExpression();
		exp.accept(this);
	}


	@Override
	public void visit(RegExpMatchOperator arg0) {
		visitBinaryExp(arg0);
	}

	@Override
	public void visit(JsonExpression arg0) {
		Expression exp=arg0.getColumn();
		exp.accept(this);
	}

	@Override
	public void visit(RegExpMySQLOperator arg0) {
		visitBinaryExp(arg0);
	}
	
}
