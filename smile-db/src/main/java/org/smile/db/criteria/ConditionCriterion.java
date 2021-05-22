package org.smile.db.criteria;

public class ConditionCriterion implements  Criterion{
	
	private  Criterion left;
	
	private  Criterion right;
	
	private  String op;
	
	public ConditionCriterion(Criterion left,Criterion right,String op) {
		this.left=left;
		this.right=right;
		this.op=op;
	}
	
	@Override
	public void accept(CriterionVisitor visitor) {
		visitor.visit(this);
	}

	public Criterion getLeft() {
		return left;
	}

	public Criterion getRight() {
		return right;
	}

	public String getOp() {
		return op;
	}

	@Override
	public String toString() {
		return new StringBuilder().append("(").append(left).append(")").append(op).append("(").append(right).append(")").toString();
	}

}
