package org.smile.db.criteria;

public class OtherFieldCriterion implements Criterion{
	
	protected  String fieldName;
	protected  String otherField;
	protected  String op;
	
	public OtherFieldCriterion(String fieldName,String otherProperty,String operat) {
		this.fieldName=fieldName;
		this.otherField=otherProperty;
		this.op=operat;
	}
	
	@Override
	public void accept(CriterionVisitor visitor) {
		visitor.visit(this);
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getOtherField() {
		return otherField;
	}

	public String getOp() {
		return op;
	}

	@Override
	public String toString() {
		return this.fieldName+" "+this.op+" "+this.otherField;
	}
	
	

}
