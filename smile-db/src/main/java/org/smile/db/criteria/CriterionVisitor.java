package org.smile.db.criteria;

public interface CriterionVisitor {
	public void visit(ConditionCriterion criterion);
	public void visit(OtherFieldCriterion criterion);
	public void visit(SimpleCriterion criterion);
	/**
	 * 
	 * @param criterion
	 */
	public void visit(OperatorCriterion criterion);
	/**
	 * 	访问orderby表达式
	 * @param criterion
	 */
	public void visit(OrderbyCriterion criterion);
	/**
	 * between 查询条件
	 * @param criterion
	 */
	public void visit(BetweenCriterion criterion);
	/**
	 * 	is null查询条件
	 * @param criterion
	 */
	public void visit(IsNullCriterion criterion);
	/**
	 *      查询字段表达式
	 * @param criterion
	 */
	public void visit(FieldCriterion criterion);
}
