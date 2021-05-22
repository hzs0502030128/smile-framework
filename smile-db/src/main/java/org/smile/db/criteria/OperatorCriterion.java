package org.smile.db.criteria;
/**
 *      操作符表达式 AND OR
 * @author 胡真山
 *
 */
public class OperatorCriterion implements Criterion{
	
	private String value;
	
	public OperatorCriterion(String value) {
		this.value=value;
	}
	
	@Override
	public void accept(CriterionVisitor visitor) {
		visitor.visit(this);
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return " "+value+" ";
	}

}
