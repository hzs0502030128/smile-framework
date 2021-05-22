package org.smile.db.criteria;

public class IsNullCriterion implements Criterion{
	
	private String fieldName;
	
	private boolean isnull=true;
	
	public IsNullCriterion(String fieldName,boolean isnull) {
		this.fieldName=fieldName;
		this.isnull=isnull;
	}
	
	@Override
	public void accept(CriterionVisitor visitor) {
		visitor.visit(this);
	}

	public String getFieldName() {
		return fieldName;
	}

	public boolean isIsnull() {
		return isnull;
	}

	@Override
	public String toString() {
		return fieldName+(isnull?" is null ":" is not null ");
	}
	
	

}
