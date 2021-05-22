package org.smile.db.criteria;

public class FieldCriterion implements Criterion{
	
	private String fieldName;
	/**别名*/
	private String alias;
	
	public FieldCriterion(String fieldName) {
		this.fieldName=fieldName;
	}
	
	public FieldCriterion(String fieldName,String alias) {
		this.fieldName=fieldName;
		this.alias=alias;
	}
	
	@Override
	public void accept(CriterionVisitor visitor) {
		visitor.visit(this);
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getAlias() {
		return alias;
	}

	@Override
	public String toString() {
		return alias==null?fieldName:fieldName+" "+this.alias;
	}

	
}
