package org.smile.db.criteria;

public class SimpleCriterion implements Criterion{
	
	protected  String fieldName;
	protected  Object value;
	protected  String op;
	
	public SimpleCriterion(String fieldName,Object value,String operat) {
		this.fieldName=fieldName;
		this.value=value;
		this.op=operat;
	}
	
	@Override
	public void accept(CriterionVisitor visitor) {
		visitor.visit(this);
	}


	public Object getValue() {
		return value;
	}

	public String getOp() {
		return op;
	}

	public String getFieldName() {
		return fieldName;
	}

	@Override
	public String toString() {
		return fieldName+" "+op+" "+value;
	}

}
