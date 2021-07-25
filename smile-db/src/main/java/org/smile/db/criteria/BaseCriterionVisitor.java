package org.smile.db.criteria;

public class BaseCriterionVisitor implements CriterionVisitor{
	/**条件语句*/
	protected  StringBuilder whereSql;
	@Override
	public void visit(ConditionCriterion criterion) {
		whereSql.append("(");
		criterion.getLeft().accept(this);
		whereSql.append(" ").append(criterion.getOp()).append(" ");
		criterion.getRight().accept(this);
		whereSql.append(")");
	}

	@Override
	public void visit(OtherFieldCriterion criterion) {
		
	}

	@Override
	public void visit(SimpleCriterion criterion) {
		
	}

	@Override
	public void visit(OperatorCriterion criterion) {
		whereSql.append(" ").append(criterion.getValue()).append(" ");
	}

	@Override
	public void visit(OrderbyCriterion criterion) {
		
	}
	
	@Override
	public void visit(BetweenCriterion criterion) {
		
	}

	@Override
	public void visit(IsNullCriterion criterion) {
		
	}

	@Override
	public void visit(FieldCriterion criterion) {
		
	}

	@Override
	public void visit(SetCriterion criterion) {

	}

}
