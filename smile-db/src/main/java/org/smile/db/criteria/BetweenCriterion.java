package org.smile.db.criteria;

public class BetweenCriterion extends SimpleCriterion{
	
	protected  Object value2;
	
	public BetweenCriterion(String field,Object value,Object value2,String operat) {
		super(field, value2, operat);
		this.value2=value2;
	}
	
	@Override
	public void accept(CriterionVisitor visitor) {
		visitor.visit(this);
	}

	public Object getValue2() {
		return value2;
	}

	@Override
	public String toString() {
		return fieldName+" between "+value+","+value2;
	}

}
